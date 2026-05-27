package com.pasteleria.produccion.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pasteleria.abastecimiento.application.CreateInventarioMovimientoRequest;
import com.pasteleria.abastecimiento.application.InventarioMovimientoCommandService;
import com.pasteleria.abastecimiento.application.InventarioMovimientoSummary;
import com.pasteleria.abastecimiento.application.port.DetalleRecetaRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InventarioMovimientoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.RecetaRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DetalleRecetaEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.RecetaEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderDetailEntity;
import com.pasteleria.produccion.application.port.FinishedProductEntryRepositoryPort;
import com.pasteleria.produccion.application.port.ProductionBatchRepositoryPort;
import com.pasteleria.produccion.application.port.ProductionMaterialConsumptionRepositoryPort;
import com.pasteleria.produccion.infrastructure.persistence.entity.FinishedProductEntryEntity;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionBatchEntity;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionEntity;
import com.pasteleria.produccion.infrastructure.persistence.entity.ProductionMaterialConsumptionEntity;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Genera consumos de materiales, lotes y entradas documentales al finalizar una producción.
 *
 * <p>Esta tanda conserva compatibilidad V1: las recetas siguen en abastecimiento.receta
 * y los productos terminados no alteran todavía un stock formal de producto terminado.
 * El consumo de ingredientes sí descuenta inventario usando InventarioMovimientoService.</p>
 */
@Service
public class ProductionMaterialService {

  private final RecetaRepositoryPort recetaRepository;
  private final DetalleRecetaRepositoryPort detalleRecetaRepository;
  private final InventarioMovimientoRepositoryPort inventarioMovimientoRepository;
  private final InventarioMovimientoCommandService inventarioMovimientoCommandService;
  private final ProductionMaterialConsumptionRepositoryPort consumptionRepository;
  private final ProductionBatchRepositoryPort batchRepository;
  private final FinishedProductEntryRepositoryPort entryRepository;
  private final ProductionMaterialPolicy materialPolicy;
  private final AuditTrailService auditTrailService;

  public ProductionMaterialService(
      RecetaRepositoryPort recetaRepository,
      DetalleRecetaRepositoryPort detalleRecetaRepository,
      InventarioMovimientoRepositoryPort inventarioMovimientoRepository,
      InventarioMovimientoCommandService inventarioMovimientoCommandService,
      ProductionMaterialConsumptionRepositoryPort consumptionRepository,
      ProductionBatchRepositoryPort batchRepository,
      FinishedProductEntryRepositoryPort entryRepository,
      ProductionMaterialPolicy materialPolicy,
      AuditTrailService auditTrailService
  ) {
    this.recetaRepository = recetaRepository;
    this.detalleRecetaRepository = detalleRecetaRepository;
    this.inventarioMovimientoRepository = inventarioMovimientoRepository;
    this.inventarioMovimientoCommandService = inventarioMovimientoCommandService;
    this.consumptionRepository = consumptionRepository;
    this.batchRepository = batchRepository;
    this.entryRepository = entryRepository;
    this.materialPolicy = materialPolicy;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public ProductionFinalizationResult generarConsumosYLotesSiFaltan(
      ProductionEntity production,
      HttpServletRequest httpRequest
  ) {
    if (consumptionRepository.existsByProductionId(production.getId()) || batchRepository.existsByProductionId(production.getId())) {
      return new ProductionFinalizationResult(
          batchRepository.findByProductionIdOrderByCreatedAtAsc(production.getId()).size(),
          consumptionRepository.findByProductionIdOrderByCreatedAtAsc(production.getId()).size(),
          totalCost(batchRepository.findByProductionIdOrderByCreatedAtAsc(production.getId()))
      );
    }

    int lotes = 0;
    int consumos = 0;
    BigDecimal costoTotalProduccion = BigDecimal.ZERO;

    for (OrderDetailEntity detail : production.getOrder().getDetails()) {
      BigDecimal cantidadProducida = materialPolicy.normalizarCantidadProducida(detail.getQuantity());
      BigDecimal costoDetalle = BigDecimal.ZERO;
      String observacionLote = "Entrada documental de producto terminado generada al finalizar producción.";

      var recetaOpt = recetaRepository.findByProductoIdAndEsActivaTrue(detail.getProduct().getId());
      if (recetaOpt.isPresent()) {
        RecetaEntity receta = recetaOpt.get();
        BigDecimal factor = materialPolicy.calcularFactor(cantidadProducida, receta.getRendimientoBase());
        List<DetalleRecetaEntity> detalles = detalleRecetaRepository.findByRecetaId(receta.getId());
        if (detalles.isEmpty()) {
          observacionLote = "Producto con receta activa sin detalles; lote documental sin consumo automático.";
        }
        for (DetalleRecetaEntity recetaDetalle : detalles) {
          BigDecimal cantidadConsumida = materialPolicy.calcularConsumo(recetaDetalle.getCantidadBase(), factor);
          BigDecimal costoUnitario = recetaDetalle.getIngrediente().getCostoReferencial();
          BigDecimal costoTotal = materialPolicy.calcularCosto(cantidadConsumida, costoUnitario);
          InventarioMovimientoSummary movimiento = inventarioMovimientoCommandService.createMovimiento(
              new CreateInventarioMovimientoRequest(
                  "INGREDIENTE",
                  recetaDetalle.getIngrediente().getId(),
                  "SALIDA_PRODUCCION",
                  cantidadConsumida,
                  "PRODUCCION",
                  production.getId().toString(),
                  "Consumo automático de producción",
                  "Consumo por receta " + receta.getNombre() + " para pedido " + production.getOrder().getCode()
              ),
              null,
              httpRequest
          );

          ProductionMaterialConsumptionEntity consumo = new ProductionMaterialConsumptionEntity();
          consumo.setProduction(production);
          consumo.setOrderDetail(detail);
          consumo.setReceta(receta);
          consumo.setDetalleReceta(recetaDetalle);
          consumo.setIngrediente(recetaDetalle.getIngrediente());
          consumo.setCantidadTeorica(cantidadConsumida);
          consumo.setCantidadConsumida(cantidadConsumida);
          consumo.setCostoUnitario(costoUnitario == null ? BigDecimal.ZERO : costoUnitario);
          consumo.setCostoTotal(costoTotal);
          consumo.setMovimientoInventario(findMovimiento(movimiento.id()));
          consumo.setObservaciones("Consumo generado al finalizar producción.");
          consumptionRepository.save(consumo);
          consumos++;
          costoDetalle = costoDetalle.add(costoTotal);
        }
      } else {
        observacionLote = "Producto sin receta técnica activa; lote documental sin consumo automático.";
      }

      ProductionBatchEntity lote = new ProductionBatchEntity();
      lote.setProduction(production);
      lote.setOrderDetail(detail);
      lote.setProduct(detail.getProduct());
      lote.setCodigoLote("LOT-" + production.getOrder().getCode() + "-" + detail.getId());
      lote.setFechaProduccion(OffsetDateTime.now());
      lote.setCantidadProducida(cantidadProducida);
      lote.setCantidadDisponible(cantidadProducida);
      lote.setCostoTotalEstimado(costoDetalle);
      lote.setEstado("DISPONIBLE");
      lote.setObservaciones(observacionLote);
      ProductionBatchEntity loteGuardado = batchRepository.save(lote);
      lotes++;

      FinishedProductEntryEntity entrada = new FinishedProductEntryEntity();
      entrada.setBatch(loteGuardado);
      entrada.setProduction(production);
      entrada.setProduct(detail.getProduct());
      entrada.setCantidad(cantidadProducida);
      entrada.setCostoTotalEstimado(costoDetalle);
      entrada.setObservaciones("Entrada documental generada desde lote " + loteGuardado.getCodigoLote() + ".");
      entryRepository.save(entrada);

      costoTotalProduccion = costoTotalProduccion.add(costoDetalle);
    }

    ProductionFinalizationResult result = new ProductionFinalizationResult(lotes, consumos, costoTotalProduccion);
    auditTrailService.recordChange(
        "PRODUCCION_CONSUMOS_LOTES_GENERADOS",
        "PRODUCCION",
        "produccion",
        production.getId().toString(),
        "GENERAR_CONSUMOS_LOTES",
        null,
        result,
        "Se generaron consumos de materiales y lotes documentales al finalizar producción.",
        httpRequest
    );
    return result;
  }

  public List<ProductionConsumptionSummary> listConsumptions(Long productionId, ProductionMaterialMapper mapper) {
    return consumptionRepository.findByProductionIdOrderByCreatedAtAsc(productionId).stream()
        .map(mapper::toConsumptionSummary)
        .toList();
  }

  public List<ProductionBatchSummary> listBatches(Long productionId, ProductionMaterialMapper mapper) {
    return batchRepository.findByProductionIdOrderByCreatedAtAsc(productionId).stream()
        .map(mapper::toBatchSummary)
        .toList();
  }

  public List<ProductionFinishedEntrySummary> listEntries(Long productionId, ProductionMaterialMapper mapper) {
    return entryRepository.findByProductionIdOrderByCreatedAtAsc(productionId).stream()
        .map(mapper::toEntrySummary)
        .toList();
  }

  private InventarioMovimientoEntity findMovimiento(Long movimientoId) {
    return inventarioMovimientoRepository.findById(movimientoId).orElse(null);
  }

  private BigDecimal totalCost(List<ProductionBatchEntity> batches) {
    return batches.stream()
        .map(ProductionBatchEntity::getCostoTotalEstimado)
        .filter(value -> value != null)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
