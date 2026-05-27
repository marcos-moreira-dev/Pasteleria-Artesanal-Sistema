# La UX/UI actual de la pastelería se respeta

## Estado

Vigente.

## Regla

La UX/UI propia de la pastelería se conserva. Cedro se usa como referencia de ingeniería, no como plantilla visual.

## Implicaciones

- No copiar estética Cedro.
- No reemplazar el shell visual por el shell de Cedro.
- No cambiar paleta, layout o componentes visuales sin justificación explícita.
- No convertir la app de pastelería en una app visual de restaurante.
- Sí se puede mejorar arquitectura Angular por debajo.
- Sí se pueden partir componentes gigantes.
- Sí se pueden crear facades, servicios por dominio y guards.
- Sí se pueden mejorar estados de carga, error, vacío y sin permiso si respetan el estilo actual.

## Criterio práctico

Si un cambio mejora arquitectura pero altera la experiencia visual actual, debe tratarse como cambio de UX/UI y no entrar de forma accidental.
