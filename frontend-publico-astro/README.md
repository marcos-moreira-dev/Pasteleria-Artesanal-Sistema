# Frontend Público Astro

Superficie pública comercial para mostrar marca, catálogo, propuesta de valor y solicitudes de cotización.

## Qué ya cubre

- Landing en español e inglés.
- Catálogo público en español e inglés.
- Contacto comercial en español e inglés.
- Formulario público de solicitud de cotización.
- Consumo tolerante del backend para catálogo, categorías y branding.
- Fallback visual cuando el backend local no está encendido.

## Comandos principales

```powershell
npm install
npm run dev
npm run build
```

## Variable de API

```powershell
$env:PUBLIC_API_BASE_URL='http://localhost:8080/api/v1'
```

Si no se define, el fallback del código usa `http://localhost:8080/api/v1`.

## Rutas principales

- `/`
- `/catalogo`
- `/contacto`
- `/en`
- `/en/catalog`
- `/en/contact`

## Validación de cierre

En T12 se corrigió el contrato de `BrandingAssets` para que el fallback incluya `categoryBannerPath`. El build de Astro debe pasar con:

```powershell
npm run build
```
