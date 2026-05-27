# Backend — Fiscalidad preparada

T20 agrega el módulo `com.pasteleria.fiscal` con controller, servicios, mapper, entidad JPA y repositorio.

## Endpoints

- `GET /api/v1/fiscal/documentos`
- `GET /api/v1/fiscal/documentos/{id}`
- `POST /api/v1/fiscal/documentos`
- `POST /api/v1/fiscal/documentos/{id}/emitir-interno`
- `POST /api/v1/fiscal/documentos/{id}/anular`

## Seguridad

- Consulta: `FISCAL_VER`.
- Preparación/emisión/anulación: `DOCUMENTOS_FISCALES_EMITIR`.

## Diseño

El servicio fiscal usa `ErpFinancialPolicy` para validar origen y totales. No llama al SRI, no firma XML y no genera RIDE final.
