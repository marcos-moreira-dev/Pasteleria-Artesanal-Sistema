package com.pasteleria.contratos.application;

import static com.pasteleria.common.security.Permisos.ARCHIVOS_DESCARGAR;
import static com.pasteleria.common.security.Permisos.ASIENTOS_REGISTRAR;
import static com.pasteleria.common.security.Permisos.CAJA_OPERAR;
import static com.pasteleria.common.security.Permisos.CAJA_VER;
import static com.pasteleria.common.security.Permisos.CARTERA_VER;
import static com.pasteleria.common.security.Permisos.COBRANZAS_REGISTRAR;
import static com.pasteleria.common.security.Permisos.CONTABILIDAD_VER;
import static com.pasteleria.common.security.Permisos.CUENTAS_PAGAR_VER;
import static com.pasteleria.common.security.Permisos.DOCUMENTOS_FISCALES_EMITIR;
import static com.pasteleria.common.security.Permisos.FISCAL_VER;
import static com.pasteleria.common.security.Permisos.PAGOS_PROVEEDOR_REGISTRAR;
import static com.pasteleria.common.security.Permisos.SEGURIDAD_ADMINISTRAR;
import static com.pasteleria.common.security.Permisos.SOPORTE_GESTIONAR;
import static com.pasteleria.common.security.Permisos.SOPORTE_VER;
import static com.pasteleria.common.security.Permisos.TESORERIA_VER;
import static com.pasteleria.common.security.Permisos.TERCEROS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.TERCEROS_VER;
import static com.pasteleria.common.security.Permisos.USUARIOS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.AUDITORIA_VER;
import static com.pasteleria.common.security.Permisos.CLIENTES_GESTIONAR;
import static com.pasteleria.common.security.Permisos.CLIENTES_VER;
import static com.pasteleria.common.security.Permisos.COMPRAS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.COMPRAS_VER;
import static com.pasteleria.common.security.Permisos.CONTRATOS_API_VER;
import static com.pasteleria.common.security.Permisos.COTIZACIONES_GESTIONAR;
import static com.pasteleria.common.security.Permisos.COTIZACIONES_VER;
import static com.pasteleria.common.security.Permisos.GUIA_OPERATIVA_VER;
import static com.pasteleria.common.security.Permisos.INVENTARIO_OPERAR;
import static com.pasteleria.common.security.Permisos.INVENTARIO_VER;
import static com.pasteleria.common.security.Permisos.PEDIDOS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.PEDIDOS_VER;
import static com.pasteleria.common.security.Permisos.PRODUCTOS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.PRODUCTOS_VER;
import static com.pasteleria.common.security.Permisos.PRODUCCION_OPERAR;
import static com.pasteleria.common.security.Permisos.PRODUCCION_VER;
import static com.pasteleria.common.security.Permisos.RECETAS_GESTIONAR;
import static com.pasteleria.common.security.Permisos.RECETAS_VER;
import static com.pasteleria.common.security.Permisos.REPORTES_SOLICITAR;
import static com.pasteleria.common.security.Permisos.REPORTES_VER;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Registro central y humano de contratos API.
 *
 * <p>No intenta escanear automaticamente controladores: la decision es
 * deliberada para que cada endpoint importante tenga modulo, resumen, permiso,
 * alcance y forma de paginacion declarados. Esto reduce rutas fantasma entre
 * Angular y backend y prepara la futura matriz endpoint-permiso.</p>
 */
@Service
public class ApiContractRegistry {

  private static final PaginationContract PAGINATION = new PaginationContract(
      "page",
      "size",
      0,
      8,
      25,
      "PageResponseDto<T>"
  );

  private static final String GLOBAL = "GLOBAL";
  private static final String SUCURSAL = "SUCURSAL";
  private static final String PUBLICO = "PUBLICO";

  public ApiContractSnapshot snapshot() {
    return new ApiContractSnapshot(endpoints(), permissions(), enums(), pagination());
  }

  public List<EndpointContract> endpoints() {
    return List.of(
        // Publico / acceso
        e("GET", "/api/v1/public/health", "Sistema", "Verifica salud publica del backend.", null, PUBLICO, true, false),
        e("POST", "/api/v1/auth/login", "Autenticacion", "Inicia sesion administrativa.", null, PUBLICO, true, false),
        e("GET", "/api/v1/auth/me", "Autenticacion", "Obtiene contexto operativo, permisos y sucursales del usuario autenticado.", null, GLOBAL, false, false),
        e("GET", "/api/v1/public/catalogo/categorias", "Catalogo publico", "Lista categorias publicadas para storefront.", null, PUBLICO, true, false),
        e("GET", "/api/v1/public/catalogo/productos", "Catalogo publico", "Lista productos publicados para storefront.", null, PUBLICO, true, false),
        e("GET", "/api/v1/public/catalogo/branding", "Catalogo publico", "Obtiene branding publico del storefront.", null, PUBLICO, true, false),
        e("POST", "/api/v1/public/cotizaciones", "Cotizaciones publicas", "Recibe solicitud publica de cotizacion.", null, PUBLICO, true, false),
        e("GET", "/api/v1/assets/{type}/{filename}", "Assets publicos", "Sirve assets publicos validados desde storage.", null, PUBLICO, true, false),

        // Archivos internos
        e("GET", "/api/v1/archivos/{archivoId}/descargar", "Archivos", "Descarga archivo interno disponible por id.", ARCHIVOS_DESCARGAR, GLOBAL, false, false),

        // Contratos
        e("GET", "/api/v1/contratos", "Contratos API", "Obtiene foto completa de endpoints, permisos, enums y paginacion.", CONTRATOS_API_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contratos/endpoints", "Contratos API", "Lista endpoints declarados.", CONTRATOS_API_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contratos/permisos", "Contratos API", "Lista permisos declarados.", CONTRATOS_API_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contratos/enums", "Contratos API", "Lista enums/catologos tecnicos declarados.", CONTRATOS_API_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contratos/paginacion", "Contratos API", "Muestra politica de paginacion administrativa.", CONTRATOS_API_VER, GLOBAL, false, false),

        // Terceros
        e("GET", "/api/v1/terceros", "Terceros", "Lista vista unificada de clientes, proveedores y futuros empleados.", TERCEROS_VER, SUCURSAL, false, false),

        // Clientes
        e("GET", "/api/v1/clientes", "Clientes", "Lista clientes.", CLIENTES_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/clientes/paginado", "Clientes", "Lista clientes paginados.", CLIENTES_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/clientes", "Clientes", "Registra cliente.", CLIENTES_GESTIONAR, SUCURSAL, false, false),
        e("PUT", "/api/v1/clientes/{clientId}", "Clientes", "Actualiza cliente.", CLIENTES_GESTIONAR, SUCURSAL, false, false),
        e("DELETE", "/api/v1/clientes/{clientId}", "Clientes", "Elimina cliente sin trazabilidad comercial.", CLIENTES_GESTIONAR, SUCURSAL, false, false),

        // Productos
        e("GET", "/api/v1/productos", "Productos", "Lista productos administrativos.", PRODUCTOS_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/productos/paginado", "Productos", "Lista productos paginados.", PRODUCTOS_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/productos", "Productos", "Registra producto.", PRODUCTOS_GESTIONAR, SUCURSAL, false, false),
        e("PUT", "/api/v1/productos/{productId}", "Productos", "Actualiza producto.", PRODUCTOS_GESTIONAR, SUCURSAL, false, false),
        e("DELETE", "/api/v1/productos/{productId}", "Productos", "Elimina producto si no tiene trazabilidad.", PRODUCTOS_GESTIONAR, SUCURSAL, false, false),
        e("POST", "/api/v1/productos/{productId}/imagen", "Productos", "Actualiza imagen de producto.", PRODUCTOS_GESTIONAR, SUCURSAL, false, false),

        // Cotizaciones / pedidos / produccion
        e("GET", "/api/v1/cotizaciones", "Cotizaciones", "Lista cotizaciones administrativas.", COTIZACIONES_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/cotizaciones/paginado", "Cotizaciones", "Lista cotizaciones paginadas.", COTIZACIONES_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/cotizaciones", "Cotizaciones", "Registra cotizacion administrativa.", COTIZACIONES_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/pedidos", "Pedidos", "Lista pedidos.", PEDIDOS_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/pedidos/paginado", "Pedidos", "Lista pedidos paginados.", PEDIDOS_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/pedidos", "Pedidos", "Registra pedido.", PEDIDOS_GESTIONAR, SUCURSAL, false, false),
        e("PATCH", "/api/v1/pedidos/{orderId}/estado", "Pedidos", "Actualiza estado de pedido.", PEDIDOS_GESTIONAR, SUCURSAL, false, false),
        e("DELETE", "/api/v1/pedidos/{orderId}", "Pedidos", "Cancela/elimina pedido segun reglas actuales.", PEDIDOS_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/produccion", "Produccion", "Lista cola de produccion.", PRODUCCION_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/produccion/paginado", "Produccion", "Lista produccion paginada.", PRODUCCION_VER, SUCURSAL, false, true),
        e("PATCH", "/api/v1/produccion/{productionId}/estado", "Produccion", "Actualiza estado de produccion.", PRODUCCION_OPERAR, SUCURSAL, false, false),
        e("GET", "/api/v1/produccion/{productionId}/consumos", "Produccion", "Lista consumos materiales de una produccion finalizada.", PRODUCCION_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/produccion/{productionId}/lotes", "Produccion", "Lista lotes generados por una produccion.", PRODUCCION_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/produccion/{productionId}/entradas-producto-terminado", "Produccion", "Lista entradas documentales de producto terminado.", PRODUCCION_VER, SUCURSAL, false, false),

        // Abastecimiento e inventario
        e("GET", "/api/v1/abastecimiento/dashboard", "Abastecimiento", "Obtiene dashboard de abastecimiento.", INVENTARIO_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/ingredientes", "Ingredientes", "Lista ingredientes.", INVENTARIO_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/ingredientes/paginado", "Ingredientes", "Lista ingredientes paginados.", INVENTARIO_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/ingredientes", "Ingredientes", "Registra ingrediente.", INVENTARIO_OPERAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/insumos", "Insumos", "Lista insumos.", INVENTARIO_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/insumos/paginado", "Insumos", "Lista insumos paginados.", INVENTARIO_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/insumos", "Insumos", "Registra insumo.", INVENTARIO_OPERAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/inventario", "Inventario", "Lista movimientos de inventario.", INVENTARIO_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/abastecimiento/inventario", "Inventario", "Registra movimiento de inventario.", INVENTARIO_OPERAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/unidades-medida", "Unidades", "Lista unidades de medida.", INVENTARIO_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/unidades-medida/paginado", "Unidades", "Lista unidades de medida paginadas.", INVENTARIO_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/unidades-medida", "Unidades", "Registra unidad de medida.", INVENTARIO_OPERAR, SUCURSAL, false, false),

        // Proveedores / compras / recetas
        e("GET", "/api/v1/abastecimiento/proveedores", "Proveedores", "Lista proveedores.", COMPRAS_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/proveedores/paginado", "Proveedores", "Lista proveedores paginados.", COMPRAS_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/proveedores", "Proveedores", "Registra proveedor.", COMPRAS_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/items-proveedor", "Items proveedor", "Lista items por proveedor.", COMPRAS_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/abastecimiento/items-proveedor", "Items proveedor", "Registra item de proveedor.", COMPRAS_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/ordenes-compra/paginado", "Ordenes de compra", "Lista ordenes de compra paginadas.", COMPRAS_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/ordenes-compra", "Ordenes de compra", "Registra orden de compra.", COMPRAS_GESTIONAR, SUCURSAL, false, false),
        e("POST", "/api/v1/abastecimiento/ordenes-compra/{id}/recibir", "Ordenes de compra", "Recibe orden de compra.", COMPRAS_GESTIONAR, SUCURSAL, false, false),
        e("POST", "/api/v1/abastecimiento/ordenes-compra/{id}/documento-compra", "Compras", "Registra documento de compra y cuenta por pagar desde una orden recibida.", COMPRAS_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/ordenes-compra/{id}/documento-compra", "Compras", "Consulta documento de compra y cuenta por pagar asociados a una orden.", COMPRAS_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/cuentas-pagar", "Cuentas por pagar", "Lista cuentas por pagar preparadas desde compras.", CUENTAS_PAGAR_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/recetas", "Recetas", "Lista recetas tecnicas actuales.", RECETAS_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/recetas/paginado", "Recetas", "Lista recetas paginadas.", RECETAS_VER, SUCURSAL, false, true),
        e("POST", "/api/v1/abastecimiento/recetas", "Recetas", "Registra receta tecnica.", RECETAS_GESTIONAR, SUCURSAL, false, false),
        e("GET", "/api/v1/abastecimiento/recetas/producto/{productoId}/pdf", "Recetas", "Descarga PDF de receta.", RECETAS_VER, SUCURSAL, false, false),

        // Caja operativa
        e("GET", "/api/v1/caja/estado", "Caja", "Consulta estado de caja y turno abierto.", CAJA_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/caja/turnos/abierto", "Caja", "Consulta el turno abierto de caja.", CAJA_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/caja/turnos/{turnoId}/movimientos", "Caja", "Lista movimientos de un turno de caja.", CAJA_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/caja/abrir", "Caja", "Abre un turno de caja.", CAJA_OPERAR, SUCURSAL, false, false),
        e("POST", "/api/v1/caja/movimientos", "Caja", "Registra movimiento de caja.", CAJA_OPERAR, SUCURSAL, false, false),
        e("POST", "/api/v1/caja/cerrar", "Caja", "Cierra turno de caja con arqueo.", CAJA_OPERAR, SUCURSAL, false, false),

        // Cartera y cuentas por pagar
        e("GET", "/api/v1/cartera/documentos-cobrar", "Cartera", "Lista documentos por cobrar.", CARTERA_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/cartera/documentos-cobrar/{id}", "Cartera", "Consulta documento por cobrar.", CARTERA_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/cartera/documentos-cobrar", "Cartera", "Crea documento por cobrar manual o asociado a pedido.", COBRANZAS_REGISTRAR, SUCURSAL, false, false),
        e("GET", "/api/v1/cartera/cobranzas", "Cartera", "Lista cobranzas registradas.", CARTERA_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/cartera/cobranzas", "Cartera", "Registra cobranza aplicada contra documentos por cobrar.", COBRANZAS_REGISTRAR, SUCURSAL, false, false),
        e("GET", "/api/v1/cuentas-pagar/documentos", "Cuentas por pagar", "Lista documentos por pagar.", CUENTAS_PAGAR_VER, SUCURSAL, false, false),
        e("GET", "/api/v1/cuentas-pagar/pagos", "Cuentas por pagar", "Lista pagos a proveedor.", CUENTAS_PAGAR_VER, SUCURSAL, false, false),
        e("POST", "/api/v1/cuentas-pagar/pagos", "Cuentas por pagar", "Registra pago aplicado contra documentos por pagar.", PAGOS_PROVEEDOR_REGISTRAR, SUCURSAL, false, false),

        // Contabilidad aplicada
        e("GET", "/api/v1/contabilidad/cuentas", "Contabilidad", "Lista plan de cuentas activo.", CONTABILIDAD_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contabilidad/diarios", "Contabilidad", "Lista tipos de diario contable.", CONTABILIDAD_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contabilidad/asientos", "Contabilidad", "Lista asientos contables recientes.", CONTABILIDAD_VER, GLOBAL, false, false),
        e("GET", "/api/v1/contabilidad/asientos/{id}", "Contabilidad", "Consulta asiento contable con detalle.", CONTABILIDAD_VER, GLOBAL, false, false),
        e("POST", "/api/v1/contabilidad/asientos", "Contabilidad", "Registra asiento contable manual validando partida doble.", ASIENTOS_REGISTRAR, GLOBAL, false, false),

        // Bridges ERP
        e("POST", "/api/v1/erp-bridges/pedidos/{pedidoId}/documento-cobrar", "ERP Bridges", "Genera documento por cobrar desde pedido sin duplicarlo.", COBRANZAS_REGISTRAR, SUCURSAL, false, false),
        e("POST", "/api/v1/erp-bridges/documentos-cobrar/{documentoCobrarId}/asiento-venta", "ERP Bridges", "Genera asiento de venta desde documento por cobrar sin duplicarlo.", ASIENTOS_REGISTRAR, GLOBAL, false, false),
        e("POST", "/api/v1/erp-bridges/cobranzas/{cobranzaId}/asiento-cobro", "ERP Bridges", "Genera asiento de cobro desde cobranza sin duplicarlo.", ASIENTOS_REGISTRAR, GLOBAL, false, false),
        e("POST", "/api/v1/erp-bridges/documentos-pagar/{documentoPagarId}/asiento-compra", "ERP Bridges", "Genera asiento de compra desde documento por pagar sin duplicarlo.", ASIENTOS_REGISTRAR, GLOBAL, false, false),
        e("POST", "/api/v1/erp-bridges/pagos-proveedor/{pagoProveedorId}/asiento-pago", "ERP Bridges", "Genera asiento de pago a proveedor sin duplicarlo.", ASIENTOS_REGISTRAR, GLOBAL, false, false),

        // Fiscalidad preparada
        e("GET", "/api/v1/fiscal/documentos", "Fiscal", "Lista documentos fiscales internos preparados.", FISCAL_VER, GLOBAL, false, false),
        e("GET", "/api/v1/fiscal/documentos/{id}", "Fiscal", "Consulta documento fiscal interno.", FISCAL_VER, GLOBAL, false, false),
        e("POST", "/api/v1/fiscal/documentos", "Fiscal", "Prepara documento fiscal interno desde documento por cobrar o compra.", DOCUMENTOS_FISCALES_EMITIR, GLOBAL, false, false),
        e("POST", "/api/v1/fiscal/documentos/{id}/emitir-interno", "Fiscal", "Marca documento fiscal como emitido interno sin autorizacion SRI.", DOCUMENTOS_FISCALES_EMITIR, GLOBAL, false, false),
        e("POST", "/api/v1/fiscal/documentos/{id}/anular", "Fiscal", "Anula documento fiscal interno.", DOCUMENTOS_FISCALES_EMITIR, GLOBAL, false, false),

        // Guia / reportes / notificaciones
        e("GET", "/api/v1/casos-uso", "Guia Operativa", "Lista casos de uso.", GUIA_OPERATIVA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/casos-uso/hub", "Guia Operativa", "Obtiene hub de guia operativa.", GUIA_OPERATIVA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/casos-uso/manual.pdf", "Guia Operativa", "Descarga manual PDF de guia operativa.", GUIA_OPERATIVA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/casos-uso/{codigo}", "Guia Operativa", "Obtiene caso de uso por codigo.", GUIA_OPERATIVA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/reportes", "Reportes", "Lista jobs de reporte.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/reportes/paginado", "Reportes", "Lista jobs de reporte paginados.", REPORTES_VER, GLOBAL, false, true),
        e("POST", "/api/v1/reportes", "Reportes", "Solicita reporte.", REPORTES_SOLICITAR, GLOBAL, false, false),
        e("GET", "/api/v1/reportes/{jobId}/descargar", "Reportes", "Descarga reporte generado.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/dashboard", "Inteligencia", "Obtiene resumen ERP para tablero administrativo.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/cartera", "Inteligencia", "Consulta vista semantica de cartera.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/cuentas-pagar", "Inteligencia", "Consulta vista semantica de cuentas por pagar.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/caja", "Inteligencia", "Consulta vista semantica de caja.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/contabilidad", "Inteligencia", "Consulta vista semantica de contabilidad.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/fiscal", "Inteligencia", "Consulta vista semantica fiscal.", REPORTES_VER, GLOBAL, false, false),
        e("GET", "/api/v1/inteligencia/stock-bajo", "Inteligencia", "Consulta vista semantica de stock bajo.", REPORTES_VER, GLOBAL, false, false),

        // Auditoria, soporte y evidencia
        e("GET", "/api/v1/auditoria/resumen", "Auditoria", "Obtiene resumen de eventos auditables para soporte.", AUDITORIA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/auditoria/eventos", "Auditoria", "Lista eventos de auditoria recientes con filtros basicos.", AUDITORIA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/soporte/evidencia", "Soporte", "Obtiene evidencia tecnica-operativa del sistema.", SOPORTE_VER, GLOBAL, false, false),
        e("GET", "/api/v1/soporte/checklist", "Soporte", "Obtiene checklist operativo de soporte y entrega.", SOPORTE_VER, GLOBAL, false, false),
        e("GET", "/api/v1/notificaciones", "Notificaciones", "Lista notificaciones.", AUDITORIA_VER, GLOBAL, false, false),
        e("GET", "/api/v1/notificaciones/resumen", "Notificaciones", "Obtiene resumen de notificaciones.", AUDITORIA_VER, GLOBAL, false, false)
    ).stream()
        .sorted(Comparator.comparing(EndpointContract::path).thenComparing(EndpointContract::method))
        .toList();
  }

  public List<PermissionContract> permissions() {
    return List.of(
        p(CLIENTES_VER, "Clientes", "Consultar clientes.", SUCURSAL),
        p(CLIENTES_GESTIONAR, "Clientes", "Crear, actualizar o eliminar clientes.", SUCURSAL),
        p(TERCEROS_VER, "Terceros", "Consultar vista unificada de clientes, proveedores y empleados.", SUCURSAL),
        p(TERCEROS_GESTIONAR, "Terceros", "Gestionar datos unificados de terceros en fases ERP posteriores.", SUCURSAL),
        p(PRODUCTOS_VER, "Productos", "Consultar productos y catalogo administrativo.", SUCURSAL),
        p(PRODUCTOS_GESTIONAR, "Productos", "Crear, actualizar, eliminar o publicar productos.", SUCURSAL),
        p(COTIZACIONES_VER, "Cotizaciones", "Consultar cotizaciones.", SUCURSAL),
        p(COTIZACIONES_GESTIONAR, "Cotizaciones", "Crear y gestionar cotizaciones.", SUCURSAL),
        p(PEDIDOS_VER, "Pedidos", "Consultar pedidos.", SUCURSAL),
        p(PEDIDOS_GESTIONAR, "Pedidos", "Crear, cancelar o cambiar estados de pedidos.", SUCURSAL),
        p(PRODUCCION_VER, "Produccion", "Consultar produccion.", SUCURSAL),
        p(PRODUCCION_OPERAR, "Produccion", "Operar cola/ordenes de produccion.", SUCURSAL),
        p(RECETAS_VER, "Recetas", "Consultar recetas tecnicas.", SUCURSAL),
        p(RECETAS_GESTIONAR, "Recetas", "Crear y mantener recetas tecnicas.", SUCURSAL),
        p(INVENTARIO_VER, "Inventario", "Consultar stock y movimientos.", SUCURSAL),
        p(INVENTARIO_OPERAR, "Inventario", "Registrar movimientos, ajustes y mermas.", SUCURSAL),
        p(COMPRAS_VER, "Compras", "Consultar proveedores, items y ordenes.", SUCURSAL),
        p(COMPRAS_GESTIONAR, "Compras", "Gestionar proveedores, ordenes y recepciones.", SUCURSAL),
        p(REPORTES_VER, "Reportes", "Consultar reportes generados.", GLOBAL),
        p(REPORTES_SOLICITAR, "Reportes", "Solicitar generacion de reportes.", GLOBAL),
        p(GUIA_OPERATIVA_VER, "Guia Operativa", "Consultar guia operativa y casos de uso.", GLOBAL),
        p(AUDITORIA_VER, "Auditoria", "Consultar eventos de auditoria y notificaciones administrativas.", GLOBAL),
        p(CAJA_VER, "Caja", "Consultar turnos, pagos y movimientos de caja.", SUCURSAL),
        p(CAJA_OPERAR, "Caja", "Abrir, cerrar y operar caja.", SUCURSAL),
        p(CARTERA_VER, "Cartera", "Consultar documentos por cobrar.", SUCURSAL),
        p(COBRANZAS_REGISTRAR, "Cartera", "Registrar cobranzas contra saldos pendientes.", SUCURSAL),
        p(CUENTAS_PAGAR_VER, "Cuentas por pagar", "Consultar obligaciones con proveedores.", SUCURSAL),
        p(PAGOS_PROVEEDOR_REGISTRAR, "Cuentas por pagar", "Registrar pagos a proveedores.", SUCURSAL),
        p(TESORERIA_VER, "Tesoreria", "Consultar bancos y movimientos de tesoreria.", GLOBAL),
        p(CONTABILIDAD_VER, "Contabilidad", "Consultar plan de cuentas, asientos y reportes contables.", GLOBAL),
        p(ASIENTOS_REGISTRAR, "Contabilidad", "Registrar asientos contables.", GLOBAL),
        p(FISCAL_VER, "Fiscal", "Consultar documentos fiscales preparados.", GLOBAL),
        p(DOCUMENTOS_FISCALES_EMITIR, "Fiscal", "Preparar o emitir documentos fiscales segun configuracion.", GLOBAL),
        p(ARCHIVOS_DESCARGAR, "Archivos", "Descargar archivos internos disponibles segun politica vigente.", GLOBAL),
        p(SOPORTE_VER, "Soporte", "Consultar tickets de soporte interno.", GLOBAL),
        p(SOPORTE_GESTIONAR, "Soporte", "Crear, comentar y cerrar tickets de soporte interno.", GLOBAL),
        p(SEGURIDAD_ADMINISTRAR, "Seguridad", "Administrar roles, permisos y parametros de seguridad.", GLOBAL),
        p(USUARIOS_GESTIONAR, "Usuarios", "Gestionar usuarios y asignaciones.", GLOBAL),
        p(CONTRATOS_API_VER, "Contratos API", "Consultar contratos tecnicos API.", GLOBAL)
    ).stream()
        .sorted(Comparator.comparing(PermissionContract::module).thenComparing(PermissionContract::code))
        .toList();
  }

  public List<EnumContract> enums() {
    return List.of(
        new EnumContract("EstadoPedido", List.of("REGISTRADO", "EN_PREPARACION", "LISTO", "ENTREGADO", "CANCELADO")),
        new EnumContract("EstadoProduccion", List.of("PENDIENTE", "PREPARACION", "DECORACION", "EMPAQUE", "FINALIZADO", "CANCELADO")),
        new EnumContract("ProductionArtifactType", List.of("CONSUMO_MATERIAL", "LOTE_PRODUCCION", "ENTRADA_PRODUCTO_TERMINADO")),
        new EnumContract("TipoMovimiento", List.of("ENTRADA_COMPRA", "ENTRADA_AJUSTE", "SALIDA_PRODUCCION", "SALIDA_MERMA", "SALIDA_AJUSTE")),
        new EnumContract("EstadoOrdenCompra", List.of("BORRADOR", "ENVIADA", "RECIBIDA_PARCIAL", "RECIBIDA", "CANCELADA")),
        new EnumContract("EstadoDocumentoCompra", List.of("BORRADOR", "REGISTRADO", "ANULADO")),
        new EnumContract("EstadoDocumentoPagar", List.of("PENDIENTE", "PAGADO_PARCIAL", "PAGADO", "ANULADO")),
        new EnumContract("EstadoDocumentoCobrar", List.of("PENDIENTE", "PARCIAL", "PAGADO", "ANULADO")),
        new EnumContract("EstadoCobranza", List.of("REGISTRADA", "ANULADA")),
        new EnumContract("EstadoPagoProveedor", List.of("REGISTRADO", "ANULADO")),
        new EnumContract("EstadoAsientoContable", List.of("BORRADOR", "REGISTRADO", "ANULADO")),
        new EnumContract("TipoCuentaContable", List.of("ACTIVO", "PASIVO", "PATRIMONIO", "INGRESO", "COSTO", "GASTO")),
        new EnumContract("NaturalezaCuenta", List.of("DEUDORA", "ACREEDORA")),
        new EnumContract("EstadoDocumentoFiscal", List.of("BORRADOR", "EMITIDO_INTERNO", "ANULADO")),
        new EnumContract("TipoComprobanteFiscal", List.of("FACTURA", "NOTA_VENTA", "COMPROBANTE_COMPRA_INTERNO")),
        new EnumContract("OrigenDocumentoFiscal", List.of("DOCUMENTO_COBRAR", "DOCUMENTO_COMPRA")),
        new EnumContract("EstadoChecklistSoporte", List.of("OK", "REVISAR", "REQUERIDO", "CONDICIONAL")),
        new EnumContract("ReportTypeValue", List.of("PEDIDOS", "PRODUCCION", "INVENTARIO", "ABASTECIMIENTO", "CLIENTES", "CATALOGO")),
        new EnumContract("TipoMovimientoCaja", List.of("APERTURA", "VENTA_CONTADO", "COBRO_CLIENTE", "INGRESO_MANUAL", "PAGO_PROVEEDOR", "GASTO_CAJA", "RETIRO_DEPOSITO", "SALIDA_MANUAL", "DIFERENCIA_ARQUEO", "ANULACION")),
        new EnumContract("NaturalezaMovimientoCaja", List.of("ENTRADA", "SALIDA", "AJUSTE")),
        new EnumContract("EstadoTurnoCaja", List.of("ABIERTO", "CERRADO", "ANULADO"))
    );
  }

  public PaginationContract pagination() {
    return PAGINATION;
  }

  private static EndpointContract e(
      String method,
      String path,
      String module,
      String summary,
      String requiredPermission,
      String scope,
      boolean publicEndpoint,
      boolean paginated
  ) {
    return new EndpointContract(method, path, module, summary, requiredPermission, scope, publicEndpoint, paginated);
  }

  private static PermissionContract p(String code, String module, String description, String scope) {
    return new PermissionContract(code, module, description, scope);
  }
}
