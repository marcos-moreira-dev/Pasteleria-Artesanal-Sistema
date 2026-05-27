package com.pasteleria.common.security;

import java.util.List;

/**
 * Catalogo tecnico central de permisos usados por backend y contratos API.
 *
 * <p>La base de datos sigue siendo la fuente persistente de permisos; esta clase
 * evita strings dispersos en controladores, servicios y pruebas. Los nombres
 * son del dominio de Pasteleria ERP, no del proyecto Cedro ni del dominio
 * restaurante.</p>
 */
public final class Permisos {

  public static final String CLIENTES_VER = "CLIENTES_VER";
  public static final String CLIENTES_GESTIONAR = "CLIENTES_GESTIONAR";
  public static final String PRODUCTOS_VER = "PRODUCTOS_VER";
  public static final String PRODUCTOS_GESTIONAR = "PRODUCTOS_GESTIONAR";
  public static final String COTIZACIONES_VER = "COTIZACIONES_VER";
  public static final String COTIZACIONES_GESTIONAR = "COTIZACIONES_GESTIONAR";
  public static final String PEDIDOS_VER = "PEDIDOS_VER";
  public static final String PEDIDOS_GESTIONAR = "PEDIDOS_GESTIONAR";
  public static final String PRODUCCION_VER = "PRODUCCION_VER";
  public static final String PRODUCCION_OPERAR = "PRODUCCION_OPERAR";
  public static final String RECETAS_VER = "RECETAS_VER";
  public static final String RECETAS_GESTIONAR = "RECETAS_GESTIONAR";
  public static final String INVENTARIO_VER = "INVENTARIO_VER";
  public static final String INVENTARIO_OPERAR = "INVENTARIO_OPERAR";
  public static final String TERCEROS_VER = "TERCEROS_VER";
  public static final String TERCEROS_GESTIONAR = "TERCEROS_GESTIONAR";
  public static final String COMPRAS_VER = "COMPRAS_VER";
  public static final String COMPRAS_GESTIONAR = "COMPRAS_GESTIONAR";
  public static final String CAJA_VER = "CAJA_VER";
  public static final String CAJA_OPERAR = "CAJA_OPERAR";
  public static final String CARTERA_VER = "CARTERA_VER";
  public static final String COBRANZAS_REGISTRAR = "COBRANZAS_REGISTRAR";
  public static final String CUENTAS_PAGAR_VER = "CUENTAS_PAGAR_VER";
  public static final String PAGOS_PROVEEDOR_REGISTRAR = "PAGOS_PROVEEDOR_REGISTRAR";
  public static final String TESORERIA_VER = "TESORERIA_VER";
  public static final String CONTABILIDAD_VER = "CONTABILIDAD_VER";
  public static final String ASIENTOS_REGISTRAR = "ASIENTOS_REGISTRAR";
  public static final String FISCAL_VER = "FISCAL_VER";
  public static final String DOCUMENTOS_FISCALES_EMITIR = "DOCUMENTOS_FISCALES_EMITIR";
  public static final String ARCHIVOS_DESCARGAR = "ARCHIVOS_DESCARGAR";
  public static final String REPORTES_VER = "REPORTES_VER";
  public static final String REPORTES_SOLICITAR = "REPORTES_SOLICITAR";
  public static final String AUDITORIA_VER = "AUDITORIA_VER";
  public static final String GUIA_OPERATIVA_VER = "GUIA_OPERATIVA_VER";
  public static final String SOPORTE_VER = "SOPORTE_VER";
  public static final String SOPORTE_GESTIONAR = "SOPORTE_GESTIONAR";
  public static final String SEGURIDAD_ADMINISTRAR = "SEGURIDAD_ADMINISTRAR";
  public static final String USUARIOS_GESTIONAR = "USUARIOS_GESTIONAR";
  public static final String CONTRATOS_API_VER = "CONTRATOS_API_VER";

  private Permisos() {
  }

  public static List<String> all() {
    return List.of(
        CLIENTES_VER,
        CLIENTES_GESTIONAR,
        PRODUCTOS_VER,
        PRODUCTOS_GESTIONAR,
        COTIZACIONES_VER,
        COTIZACIONES_GESTIONAR,
        PEDIDOS_VER,
        PEDIDOS_GESTIONAR,
        PRODUCCION_VER,
        PRODUCCION_OPERAR,
        RECETAS_VER,
        RECETAS_GESTIONAR,
        INVENTARIO_VER,
        INVENTARIO_OPERAR,
        TERCEROS_VER,
        TERCEROS_GESTIONAR,
        COMPRAS_VER,
        COMPRAS_GESTIONAR,
        CAJA_VER,
        CAJA_OPERAR,
        CARTERA_VER,
        COBRANZAS_REGISTRAR,
        CUENTAS_PAGAR_VER,
        PAGOS_PROVEEDOR_REGISTRAR,
        TESORERIA_VER,
        CONTABILIDAD_VER,
        ASIENTOS_REGISTRAR,
        FISCAL_VER,
        DOCUMENTOS_FISCALES_EMITIR,
        ARCHIVOS_DESCARGAR,
        REPORTES_VER,
        REPORTES_SOLICITAR,
        AUDITORIA_VER,
        GUIA_OPERATIVA_VER,
        SOPORTE_VER,
        SOPORTE_GESTIONAR,
        SEGURIDAD_ADMINISTRAR,
        USUARIOS_GESTIONAR,
        CONTRATOS_API_VER
    );
  }
}
