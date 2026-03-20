UPDATE notificacion
SET titulo = replace(
    titulo,
    convert_from(decode('c383c692c382c2b3', 'hex'), 'UTF8'),
    'ó'
)
WHERE encode(convert_to(titulo, 'UTF8'), 'hex') LIKE '%c383c692c382c2b3%';

UPDATE notificacion
SET mensaje = replace(
    mensaje,
    convert_from(decode('c383c692c382c2b3', 'hex'), 'UTF8'),
    'ó'
)
WHERE encode(convert_to(mensaje, 'UTF8'), 'hex') LIKE '%c383c692c382c2b3%';
