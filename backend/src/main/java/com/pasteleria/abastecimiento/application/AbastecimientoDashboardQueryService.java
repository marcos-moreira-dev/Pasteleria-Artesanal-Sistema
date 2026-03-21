package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.AlertaDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.MovimientoRecienteDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.ProveedorConOCDto;
import com.pasteleria.abastecimiento.application.AbastecimientoDashboardDto.SugerenciaReposicionDto;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.IngredienteRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.InsumoRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.InventarioMovimientoRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.ProveedorRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AbastecimientoDashboardQueryService {

  private final IngredienteRepository ingredienteRepository;
  private final InsumoRepository insumoRepository;
  private final InventarioMovimientoRepository movimientoRepository;
  private final ProveedorRepository proveedorRepository;
  private final JdbcTemplate jdbcTemplate;

  public AbastecimientoDashboardQueryService(
      IngredienteRepository ingredienteRepository,
      InsumoRepository insumoRepository,
      InventarioMovimientoRepository movimientoRepository,
      ProveedorRepository proveedorRepository,
      JdbcTemplate jdbcTemplate) {
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.movimientoRepository = movimientoRepository;
    this.proveedorRepository = proveedorRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  public AbastecimientoDashboardDto buildDashboard() {
    var ingredientes = ingredienteRepository.findAll();
    var insumos = insumoRepository.findAll();
    
    int itemsCriticos = 0;
    int itemsBajoMinimo = 0;
    BigDecimal costoReposicion = BigDecimal.ZERO;
    List<AlertaDto> alertas = new ArrayList<>();
    List<SugerenciaReposicionDto> sugerencias = new ArrayList<>();

    // Procesar ingredientes
    for (IngredienteEntity ing : ingredientes) {
      if (ing.getStockActual().compareTo(ing.getStockMinimo()) < 0) {
        itemsBajoMinimo++;
        
        var stockMin = ing.getStockMinimo();
        var stockAct = ing.getStockActual();
        var deficit = stockMin.subtract(stockAct);
        
        // Calcular cobertura aproximada (simplificado)
        int coberturaDias = stockAct.compareTo(BigDecimal.ZERO) > 0 
            ? stockAct.divide(stockMin.divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP), 0, RoundingMode.HALF_UP).intValue()
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
                ? "Stock agotado - requiere reposición urgente" 
                : "Stock por debajo del mínimo establecido",
            stockAct,
            stockMin,
            coberturaDias
        ));

        costoReposicion = costoReposicion.add(deficit.multiply(ing.getCostoReferencial()));

        sugerencias.add(new SugerenciaReposicionDto(
            "INGREDIENTE",
            ing.getId(),
            ing.getName(),
            ing.getCode(),
            stockAct,
            stockMin,
            deficit.multiply(BigDecimal.valueOf(1.5)), // Sugerir 50% más del déficit
            null, // Proveedor principal - podría buscarse
            ing.getCostoReferencial()
        ));
      }
    }

    // Procesar insumos
    for (InsumoEntity ins : insumos) {
      if (ins.getStockActual().compareTo(ins.getStockMinimo()) < 0) {
        itemsBajoMinimo++;
        
        var stockMin = ins.getStockMinimo();
        var stockAct = ins.getStockActual();
        var deficit = stockMin.subtract(stockAct);

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
                ? "Stock agotado - requiere reposición urgente" 
                : "Stock por debajo del mínimo establecido",
            stockAct,
            stockMin,
            null
        ));

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
            ins.getCostoReferencial()
        ));
      }
    }

    // Órdenes de compra - contar pendientes usando JDBC
    int ordenesPendientesCount = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM orden_compra WHERE estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL')", 
        Integer.class);
    
    // Obtener proveedores con órdenes activas
    List<ProveedorConOCDto> proveedoresDto = jdbcTemplate.query(
        """
        SELECT p.proveedor_id, p.nombre, p.telefono, 
               COUNT(oc.orden_compra_id) as ordenes_count,
               MAX(oc.created_at) as ultima_fecha
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
                : null
        ));

    // Obtener movimientos recientes (últimos 8) - usar findAll y ordenar manualmente
    var todosMovimientos = movimientoRepository.findAll();
    List<MovimientoRecienteDto> movimientosDto = todosMovimientos.stream()
        .sorted(Comparator.comparing(InventarioMovimientoEntity::getFechaMovimiento).reversed())
        .limit(8)
        .map(m -> toMovimientoDto(m, ingredientes, insumos))
        .toList();

    // Contar recepciones de hoy (movimientos de entrada hoy)
    LocalDate hoy = LocalDate.now();
    long recepcionesHoy = todosMovimientos.stream()
        .filter(m -> m.getTipoMovimiento() != null && m.getTipoMovimiento().startsWith("ENTRADA"))
        .filter(m -> m.getFechaMovimiento() != null && m.getFechaMovimiento().toLocalDate().equals(hoy))
        .count();

    return new AbastecimientoDashboardDto(
        itemsCriticos,
        itemsBajoMinimo,
        ordenesPendientesCount,
        (int) recepcionesHoy,
        costoReposicion,
        alertas.stream().limit(10).toList(),
        sugerencias.stream().limit(8).toList(),
        movimientosDto,
        proveedoresDto
    );
  }

  private MovimientoRecienteDto toMovimientoDto(InventarioMovimientoEntity mov, 
      List<IngredienteEntity> ingredientes, List<InsumoEntity> insumos) {
    // Buscar el nombre del item basado en tipo e id
    String itemNombre = "Desconocido";
    if ("INGREDIENTE".equals(mov.getItemTipo())) {
      itemNombre = ingredientes.stream()
          .filter(i -> i.getId().equals(mov.getItemId()))
          .findFirst()
          .map(IngredienteEntity::getName)
          .orElse("Ingrediente #" + mov.getItemId());
    } else if ("INSUMO".equals(mov.getItemTipo())) {
      itemNombre = insumos.stream()
          .filter(i -> i.getId().equals(mov.getItemId()))
          .findFirst()
          .map(InsumoEntity::getName)
          .orElse("Insumo #" + mov.getItemId());
    }

    // Obtener nombre completo del usuario
    String registradoPor = "Sistema";
    if (mov.getRegistradoPor() != null) {
      String firstName = mov.getRegistradoPor().getFirstName();
      String lastName = mov.getRegistradoPor().getLastName();
      registradoPor = (firstName != null ? firstName : "") + 
                     (lastName != null ? " " + lastName : "");
      registradoPor = registradoPor.trim();
      if (registradoPor.isEmpty()) {
        registradoPor = "Sistema";
      }
    }

    return new MovimientoRecienteDto(
        mov.getId(),
        mov.getItemTipo(),
        mov.getItemId(),
        itemNombre,
        mov.getTipoMovimiento(),
        mov.getCantidad(),
        mov.getSaldoPosterior(),
        mov.getFechaMovimiento() != null ? mov.getFechaMovimiento().format(DateTimeFormatter.ISO_DATE_TIME) : null,
        registradoPor
    );
  }
}
