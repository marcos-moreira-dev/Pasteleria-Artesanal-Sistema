package com.pasteleria.abastecimiento.domain.model;

/**
 * Enum que representa los tipos de movimiento de inventario.
 * 
 * <h2>DESCRIPCIÓN</h2>
 * Este enum define todos los tipos posibles de movimientos que pueden
 * registrarse en el sistema de inventario. Cada tipo tiene una semántica
 * específica y reglas de negocio asociadas.
 * 
 * <h2>CATEGORÍAS</h2>
 * 
 * <h3>1. ENTRADAS (Aumentan el stock)</h3>
 * 
 * <b>ENTRADA_COMPRA</b>
 * <ul>
 *   <li>Cuándo: Al recibir mercadería de proveedores</li>
 *   <li>Origen: Órdenes de compra</li>
 *   <li>Cantidad: Siempre positiva</li>
 *   <li>Referencia: Número de orden de compra</li>
 *   <li>Auditoría: Requiere documento de soporte</li>
 * </ul>
 * 
 * <b>ENTRADA_AJUSTE</b>
 * <ul>
 *   <li>Cuándo: Correcciones positivas de inventario</li>
 *   <li>Origen: Inventario físico, conteos, hallazgos</li>
 *   <li>Cantidad: Siempre positiva</li>
 *   <li>Referencia: Número de ajuste</li>
 *   <li>Auditoría: Requiere justificación y autorización</li>
 * </ul>
 * 
 * <h3>2. SALIDAS (Disminuyen el stock)</h3>
 * 
 * <b>SALIDA_PRODUCCION</b>
 * <ul>
 *   <li>Cuándo: Consumo para órdenes de producción</li>
 *   <li>Origen: Órdenes de producción aprobadas</li>
 *   <li>Cantidad: Siempre positiva; el tipo define que resta stock</li>
 *   <li>Referencia: Número de orden de producción</li>
 *   <li>Auditoría: Automática al iniciar producción</li>
 * </ul>
 * 
 * <b>SALIDA_MERMA</b>
 * <ul>
 *   <li>Cuándo: Pérdidas por vencimiento, daño, robos, etc.</li>
 *   <li>Origen: Detección de productos no utilizables</li>
 *   <li>Cantidad: Siempre positiva; el tipo define que resta stock</li>
 *   <li>Referencia: Acta de merma, foto evidencia</li>
 *   <li>Auditoría: Requiere autorización de supervisor</li>
 * </ul>
 * 
 * <b>SALIDA_AJUSTE</b>
 * <ul>
 *   <li>Cuándo: Correcciones negativas de inventario</li>
 *   <li>Origen: Inventario físico, conteos</li>
 *   <li>Cantidad: Siempre positiva; el tipo define que resta stock</li>
 *   <li>Referencia: Número de ajuste</li>
 *   <li>Auditoría: Requiere justificación y autorización</li>
 * </ul>
 * 
 * <h2>CONVENCIONES</h2>
 * <ul>
 *   <li>Todos los valores usan SNAKE_CASE (mayúsculas y guiones bajos)</li>
 *   <li>Prefijo indica dirección: ENTRADA_ (positivo) o SALIDA_ (negativo)</li>
 *   <li>Sufijo indica motivo: COMPRA, PRODUCCION, MERMA, AJUSTE</li>
 * </ul>
 * 
 * <h2>VALIDACIÓN DE CANTIDAD</h2>
 * <pre>
 * ENTRADA_*: cantidad > 0
 * SALIDA_*: cantidad > 0; la naturaleza SALIDA resta stock
 * </pre>
 * 
 * <h2>MAPEO A BASE DE DATOS</h2>
 * Se almacena como VARCHAR(30) en la columna tipo_movimiento.
 * 
 * <h2>EJEMPLOS DE USO</h2>
 * 
 * <h3>Crear movimiento de entrada</h3>
 * <pre>
 * InventarioMovimientoEntity mov = new InventarioMovimientoEntity();
 * mov.setTipoMovimiento(TipoMovimiento.ENTRADA_COMPRA.name());
 * mov.setCantidad(new BigDecimal("1000.00")); // Positivo
 * </pre>
 * 
 * <h3>Crear movimiento de salida</h3>
 * <pre>
 * InventarioMovimientoEntity mov = new InventarioMovimientoEntity();
 * mov.setTipoMovimiento(TipoMovimiento.SALIDA_PRODUCCION.name());
 * mov.setCantidad(new BigDecimal("500.00")); // Cantidad positiva; SALIDA_PRODUCCION resta stock
 * </pre>
 * 
 * <h3>Validar tipo</h3>
 * <pre>
 * public void validarMovimiento(TipoMovimiento tipo, BigDecimal cantidad) {
 *     if (tipo.name().startsWith("ENTRADA") && cantidad.compareTo(BigDecimal.ZERO) <= 0) {
 *         throw new BusinessRuleException("Las entradas deben tener cantidad positiva");
 *     }
 *     if (tipo.name().startsWith("SALIDA") && cantidad.compareTo(BigDecimal.ZERO) >= 0) {
 *         throw new BusinessRuleException("Las salidas deben tener cantidad positiva; la naturaleza SALIDA resta stock");
 *     }
 * }
 * </pre>
 * 
 * <h2>REPORTING Y ANÁLISIS</h2>
 * <pre>
 * -- Consumo mensual por tipo
 * SELECT tipo_movimiento, SUM(ABS(cantidad)) as total
 * FROM inventario_movimiento
 * WHERE fecha_movimiento >= DATE_TRUNC('month', CURRENT_DATE)
 * GROUP BY tipo_movimiento;
 * 
 * -- Mermas por producto
 * SELECT item_id, SUM(ABS(cantidad)) as merma_total
 * FROM inventario_movimiento
 * WHERE tipo_movimiento = 'SALIDA_MERMA'
 * GROUP BY item_id
 * ORDER BY merma_total DESC;
 * </pre>
 * 
 * @see InventarioMovimientoEntity
 * @author Pastelería Development Team
 */
public enum TipoMovimiento {
    
    /**
     * Entrada de mercadería por compra a proveedores.
     * Aumenta el stock. Cantidad siempre positiva.
     */
    ENTRADA_COMPRA, 
    
    /**
     * Entrada por ajuste positivo de inventario.
     * Aumenta el stock. Cantidad siempre positiva.
     * Usado para correcciones luego de inventario físico.
     */
    ENTRADA_AJUSTE, 
    
    /**
     * Salida por consumo en producción.
     * Disminuye el stock. Cantidad siempre positiva; la naturaleza SALIDA resta stock.
     * Automáticamente generado al iniciar orden de producción.
     */
    SALIDA_PRODUCCION, 
    
    /**
     * Salida por merma (vencimiento, daño, pérdida).
     * Disminuye el stock. Cantidad siempre positiva; la naturaleza SALIDA resta stock.
     * Requiere justificación y autorización.
     */
    SALIDA_MERMA, 
    
    /**
     * Salida por ajuste negativo de inventario.
     * Disminuye el stock. Cantidad siempre positiva; la naturaleza SALIDA resta stock.
     * Usado para correcciones luego de inventario físico.
     */
    SALIDA_AJUSTE;
}
