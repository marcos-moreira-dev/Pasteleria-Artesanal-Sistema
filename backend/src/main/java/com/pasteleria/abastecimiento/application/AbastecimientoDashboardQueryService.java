package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.AlertaDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.MovimientoRecienteDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.ProveedorConOCDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.SugerenciaReposicionDto;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.IngredienteRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.InsumoRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.InventarioMovimientoRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AbastecimientoDashboardQueryService {

  private final IngredienteRepository ingredienteRepository;
  private final InsumoRepository insumoRepository;
  private final InventarioMovimientoRepository movimientoRepository;
  private final JdbcTemplate jdbcTemplate;

  public AbastecimientoDashboardQueryService(
      IngredienteRepository ingredienteRepository,
      InsumoRepository insumoRepository,
      InventarioMovimientoRepository movimientoRepository,
      JdbcTemplate jdbcTemplate) {
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.movimientoRepository = movimientoRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  public AbastecimientoDashboardDto buildDashboard() {
    List<IngredienteEntity> ingredientes = ingredienteRepository.findAll();
    List<InsumoEntity> insumos = insumoRepository.findAll();

    int itemsCriticos = 0;
    int itemsBajoMinimo = 0;
    BigDecimal costoReposicion = BigDecimal.ZERO;
    List<AlertaDto> alertas = new ArrayList<>();
    List<SugerenciaReposicionDto> sugerencias = new ArrayList<>();

    for (IngredienteEntity ing : ingredientes) {
      if (ing.getStockActual().compareTo(ing.getStockMinimo()) < 0) {
        itemsBajoMinimo++;

        BigDecimal stockMin = ing.getStockMinimo();
        BigDecimal stockAct = ing.getStockActual();
        BigDecimal deficit = stockMin.subtract(stockAct);

        int coberturaDias = stockAct.compareTo(BigDecimal.ZERO) > 0
            ? stockAct.divide(
                stockMin.divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP),
                0,
                RoundingMode.HALF_UP)
                .intValue()
            : 0;

        if (stockAct.compareTo(stockMin.multiply(BigDecimal.valueOf(0.5))) < 0) {
          itemsCriticos++;
        }

        alertas.add(new AlertaDto(
            stockAct.compareTo(BigDecimal.ZERO) == 0 ? "CRITICO" : "BAJO",
            "INGREDIENTE",
            ing.getId(),
            ing.getName(),
            ing.getCode(),
            stockAct.compareTo(BigDecimal.ZERO) == 0
                ? "Stock agotado - requiere reposicion urgente"
                : "Stock por debajo del minimo establecido",
            stockAct,
            stockMin,
            coberturaDias));

        costoReposicion = costoReposicion.add(deficit.multiply(ing.getCostoReferencial()));

        sugerencias.add(new SugerenciaReposicionDto(
            "INGREDIENTE",
            ing.getId(),
            ing.getName(),
            ing.getCode(),
            stockAct,
            stockMin,
            deficit.multiply(BigDecimal.valueOf(1.5)),
            null,
            ing.getCostoReferencial()));
      }
    }

    for (InsumoEntity ins : insumos) {
      if (ins.getStockActual().compareTo(ins.getStockMinimo()) < 0) {
        itemsBajoMinimo++;

        BigDecimal stockMin = ins.getStockMinimo();
        BigDecimal stockAct = ins.getStockActual();
        BigDecimal deficit = stockMin.subtract(stockAct);

        if (stockAct.compareTo(stockMin.multiply(BigDecimal.valueOf(0.5))) < 0) {
          itemsCriticos++;
        }

        alertas.add(new AlertaDto(
            stockAct.compareTo(BigDecimal.ZERO) == 0 ? "CRITICO" : "BAJO",
            "INSUMO",
            ins.getId(),
            ins.getName(),
            ins.getCode(),
            stockAct.compareTo(BigDecimal.ZERO) == 0
                ? "Stock agotado - requiere reposicion urgente"
                : "Stock por debajo del minimo establecido",
            stockAct,
            stockMin,
            null));

        costoReposicion = costoReposicion.add(deficit.multiply(ins.getCostoReferencial()));

        sugerencias.add(new SugerenciaReposicionDto(
            "INSUMO",
            ins.getId(),
            ins.getName(),
            ins.getCode(),
            stockAct,
            stockMin,
            deficit.multiply(BigDecimal.valueOf(1.5)),
            null,
            ins.getCostoReferencial()));
      }
    }

    Integer ordenesPendientesCount = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM orden_compra WHERE estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL')",
        Integer.class);

    List<ProveedorConOCDto> proveedoresDto = jdbcTemplate.query(
        """
        SELECT p.proveedor_id, p.nombre, p.telefono,
               COUNT(oc.orden_compra_id) AS ordenes_count,
               MAX(oc.created_at) AS ultima_fecha
        FROM proveedor p
        INNER JOIN orden_compra oc ON p.proveedor_id = oc.proveedor_id
        WHERE oc.estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL')
        GROUP BY p.proveedor_id, p.nombre, p.telefono
        """,
        (rs, rowNum) -> new ProveedorConOCDto(
            rs.getLong("proveedor_id"),
            rs.getString("nombre"),
            rs.getString("telefono"),
            rs.getInt("ordenes_count"),
            rs.getTimestamp("ultima_fecha") != null
                ? rs.getTimestamp("ultima_fecha").toLocalDateTime().toString()
                : null));

    Map<Long, String> nombresIngredientes = ingredientes.stream()
        .collect(Collectors.toMap(IngredienteEntity::getId, IngredienteEntity::getName));
    Map<Long, String> nombresInsumos = insumos.stream()
        .collect(Collectors.toMap(InsumoEntity::getId, InsumoEntity::getName));

    List<MovimientoRecienteDto> movimientosDto = movimientoRepository.findTop8ByOrderByFechaMovimientoDesc()
        .stream()
        .map(movimiento -> toMovimientoDto(movimiento, nombresIngredientes, nombresInsumos))
        .toList();

    OffsetDateTime inicioDelDia = LocalDate.now()
        .atStartOfDay()
        .atOffset(ZoneId.systemDefault().getRules().getOffset(OffsetDateTime.now().toInstant()));
    OffsetDateTime finDelDia = inicioDelDia.plusDays(1).minusNanos(1);
    long recepcionesHoy = movimientoRepository.countByTipoMovimientoStartingWithAndFechaMovimientoBetween(
        "ENTRADA",
        inicioDelDia,
        finDelDia);

    return new AbastecimientoDashboardDto(
        itemsCriticos,
        itemsBajoMinimo,
        ordenesPendientesCount != null ? ordenesPendientesCount : 0,
        (int) recepcionesHoy,
        costoReposicion,
        alertas.stream().limit(10).toList(),
        sugerencias.stream().limit(8).toList(),
        movimientosDto,
        proveedoresDto);
  }

  private MovimientoRecienteDto toMovimientoDto(
      InventarioMovimientoEntity mov,
      Map<Long, String> nombresIngredientes,
      Map<Long, String> nombresInsumos) {
    String registradoPor = "Sistema";
    if (mov.getRegistradoPor() != null) {
      String firstName = mov.getRegistradoPor().getFirstName();
      String lastName = mov.getRegistradoPor().getLastName();
      registradoPor = (firstName != null ? firstName : "")
          + (lastName != null ? " " + lastName : "");
      registradoPor = registradoPor.trim();
      if (registradoPor.isEmpty()) {
        registradoPor = "Sistema";
      }
    }

    return new MovimientoRecienteDto(
        mov.getId(),
        mov.getItemTipo(),
        mov.getItemId(),
        resolveItemNombre(mov, nombresIngredientes, nombresInsumos),
        mov.getTipoMovimiento(),
        mov.getCantidad(),
        mov.getSaldoPosterior(),
        mov.getFechaMovimiento() != null
            ? mov.getFechaMovimiento().format(DateTimeFormatter.ISO_DATE_TIME)
            : null,
        registradoPor);
  }

  private String resolveItemNombre(
      InventarioMovimientoEntity mov,
      Map<Long, String> nombresIngredientes,
      Map<Long, String> nombresInsumos) {
    if ("INGREDIENTE".equals(mov.getItemTipo())) {
      return nombresIngredientes.getOrDefault(
          mov.getItemId(),
          "Ingrediente #" + mov.getItemId());
    }
    if ("INSUMO".equals(mov.getItemTipo())) {
      return nombresInsumos.getOrDefault(
          mov.getItemId(),
          "Insumo #" + mov.getItemId());
    }
    return "Desconocido";
  }
}
