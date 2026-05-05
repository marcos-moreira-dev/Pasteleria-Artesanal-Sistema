export interface BusinessShellBrandConfig {
  readonly kicker: string;
  readonly title: string;
  readonly copy: string;
  readonly logoSquareFileName: string;
  readonly logoAlt: string;
}

export interface BusinessShellLoginConfig {
  readonly eyebrow: string;
  readonly title: string;
  readonly copy: string;
  readonly localAccessLabel: string;
  readonly localUsers: readonly string[];
  readonly supportHint: string;
}

export interface BusinessShellPageMeta {
  readonly title: string;
  readonly eyebrow: string;
}

export interface BusinessShellConfig {
  readonly brand: BusinessShellBrandConfig;
  readonly login: BusinessShellLoginConfig;
  readonly defaultPage: BusinessShellPageMeta;
  readonly pageMetaByRoute: Readonly<Record<string, BusinessShellPageMeta>>;
  readonly dashboard: {
    readonly dailyRevenueTarget: number;
    readonly weeklyRevenueTarget: number;
  };
}

/**
 * Configuración central de la carcasa del admin.
 *
 * Para adaptar esta base a otro negocio, primero cambia este archivo y evita
 * perseguir textos de marca dentro de componentes sueltos.
 */
export const businessShellConfig: BusinessShellConfig = {
  brand: {
    kicker: "Pastelería artesanal",
    title: "Casa de Producción",
    copy: "Clientes, pedidos, vitrinas y cocina coordinados para que cada entrega salga a tiempo.",
    logoSquareFileName: "logo-cuadrado.png",
    logoAlt: "Logo de la pastelería"
  },
  login: {
    eyebrow: "Panel operativo",
    title: "Pastelería en marcha",
    copy: "Ingresa con tu usuario operativo para gestionar clientes, pedidos y producción.",
    localAccessLabel: "Accesos locales de revisión",
    localUsers: ["admin", "atencion1", "produccion1"],
    supportHint: "Si el panel no responde, verifica que el servicio local esté encendido y vuelve a intentar."
  },
  defaultPage: {
    title: "Pulso del negocio",
    eyebrow: "Jornada del día"
  },
  pageMetaByRoute: {
    "/clientes": { title: "Clientes", eyebrow: "Gestión comercial" },
    "/productos": { title: "Catálogo", eyebrow: "Catálogo de productos" },
    "/cotizaciones": { title: "Cotizaciones", eyebrow: "Ventas y presupuestos" },
    "/reportes": { title: "Reportes", eyebrow: "Inteligencia de negocio" },
    "/pedidos": { title: "Pedidos", eyebrow: "Operaciones" },
    "/produccion": { title: "Producción", eyebrow: "Planificación" },
    "/guia-operativa": { title: "Guía operativa", eyebrow: "Manual vivo" },
    "/abastecimiento": { title: "Abastecimiento", eyebrow: "Cadena de suministro" }
  },
  dashboard: {
    dailyRevenueTarget: 90,
    weeklyRevenueTarget: 420
  }
};
