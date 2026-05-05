SET client_encoding = 'UTF8';
-- Corrección UTF-8 y normalización de recetas PDF. Se usa guion ASCII en listas para máxima compatibilidad PDF.
UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta de Chocolate',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Mantequilla\n- Ganache de chocolate',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla ingredientes secos.\n2. Integra huevos, leche y mantequilla.\n3. Hornea en molde mediano.\n4. Deja enfriar antes de rellenar.\n5. Cubre con ganache y refrigera.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener refrigerada si lleva relleno o cobertura sensible.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-CHOC-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Vainilla con Frutos',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Azúcar\n- Huevos\n- Mantequilla\n- Leche\n- Vainilla\n- Frutos rojos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Bate mantequilla con azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche.\n4. Hornea hasta que el centro esté firme.\n5. Decora con crema y frutos frescos.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Usar frutos frescos y refrigerar después de decorar.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-VAIN-FRUT';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Red Velvet',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Buttermilk\n- Colorante rojo\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla ingredientes secos.\n2. Integra líquidos, colorante y huevos.\n3. Hornea por capas.\n4. Enfría completamente.\n5. Rellena y cubre con frosting de queso crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Refrigerar por el frosting y sacar unos minutos antes de servir.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-RED-VEL';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Tres Leches',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcocho base\n- Leche evaporada\n- Leche condensada\n- Crema de leche\n- Vainilla\n- Merengue o crema batida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea el bizcocho base.\n2. Mezcla las tres leches.\n3. Perfora el bizcocho y baña lentamente.\n4. Refrigera hasta absorber.\n5. Decora con merengue o crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener siempre refrigerada.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-3-LECH';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Brownie Individual',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Chocolate semiamargo\n- Mantequilla\n- Azúcar\n- Huevos\n- Harina\n- Cacao\n- Nueces opcionales',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Derrite chocolate con mantequilla.\n2. Bate huevos con azúcar.\n3. Integra chocolate, harina y cacao.\n4. Vierte en molde bajo.\n5. Hornea y corta porciones individuales.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- No sobrehornear para conservar textura húmeda.'
)::text, updated_at = NOW() WHERE codigo = 'BROWNIE-IND';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Alfajor Premium',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Maicena\n- Harina\n- Mantequilla\n- Azúcar impalpable\n- Yemas\n- Dulce de leche\n- Coco rallado',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara una masa suave con mantequilla, secos y yemas.\n2. Refrigera la masa antes de estirar.\n3. Corta discos y hornea sin dorar demasiado.\n4. Rellena con dulce de leche.\n5. Pasa los bordes por coco rallado.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Manipular con cuidado porque la masa es frágil.'
)::text, updated_at = NOW() WHERE codigo = 'ALFAJOR-PREM';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cheesecake de Frutos Rojos',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Galleta molida\n- Mantequilla\n- Queso crema\n- Azúcar\n- Huevos\n- Crema de leche\n- Salsa de frutos rojos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara base de galleta y compacta.\n2. Bate queso crema con azúcar.\n3. Agrega huevos y crema.\n4. Hornea a temperatura baja.\n5. Enfría y cubre con frutos rojos.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Reposar en refrigeración antes de cortar.'
)::text, updated_at = NOW() WHERE codigo = 'CHEESE-FRUT';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Galleta Decorada',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Mantequilla\n- Azúcar impalpable\n- Huevo\n- Vainilla\n- Glaseado real\n- Colorantes alimentarios',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa de mantequilla.\n2. Refrigera, estira y corta figuras.\n3. Hornea hasta bordes ligeramente dorados.\n4. Deja enfriar por completo.\n5. Decora con glaseado real.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Esperar secado completo antes de empacar.'
)::text, updated_at = NOW() WHERE codigo = 'GALLETA-DEC';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Café Americano',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Café molido\n- Agua filtrada\n- Azúcar opcional\n- Vaso o taza de servicio',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara espresso o café concentrado.\n2. Agrega agua caliente según intensidad deseada.\n3. Sirve en taza limpia.\n4. Ofrece azúcar aparte si aplica.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Servir inmediatamente.'
)::text, updated_at = NOW() WHERE codigo = 'CAFE-AMER';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake de Vainilla',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina preparada\n- Azúcar\n- Huevos\n- Mantequilla\n- Leche\n- Vainilla\n- Frosting de vainilla',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Bate mantequilla y azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche.\n4. Llena capacillos a dos tercios.\n5. Hornea, enfría y decora con frosting.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- No decorar mientras estén calientes.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-VAIN-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake de Chocolate',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Aceite o mantequilla\n- Ganache o buttercream',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos.\n2. Integra líquidos hasta obtener masa uniforme.\n3. Llena capacillos.\n4. Hornea y deja enfriar.\n5. Decora con crema o ganache.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener cubiertos para evitar resequedad.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-CHOC-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 25 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres\n- Cupcakes\n- Galletas decoradas\n- Alfajores\n- Base de mesa\n- Etiquetas\n- Bandejas',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Define cantidad por persona.\n2. Arma variedad según disponibilidad.\n3. Agrupa por tipo de postre.\n4. Monta en bandejas limpias.\n5. Verifica transporte y presentación final.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Confirmar colores y temática antes de producir.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-25';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 50 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres\n- Cupcakes\n- Cake pops\n- Macarons o galletas\n- Bases decorativas\n- Bandejas\n- Etiquetas',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Calcula volumen total.\n2. Produce piezas por tandas.\n3. Clasifica postres fríos y secos.\n4. Monta bandejas y soportes.\n5. Revisa cantidades antes de entrega.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Preparar checklist de montaje y transporte.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-50';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Caja Mini Postres',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini brownies\n- Mini cheesecakes\n- Vasitos dulces\n- Alfajores\n- Base de caja\n- Separadores',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Arma variedad según disponibilidad.\n2. Controla tamaño uniforme de cada pieza.\n3. Coloca separadores en caja.\n4. Ubica productos frágiles al final.\n5. Etiqueta y refrigera si corresponde.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Ideal para regalos o degustaciones.'
)::text, updated_at = NOW() WHERE codigo = 'CAJA-MINI';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta de Zanahoria y Nuez',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Zanahoria rallada\n- Harina\n- Azúcar morena\n- Huevos\n- Aceite vegetal\n- Canela\n- Nueces\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos con canela.\n2. Integra huevos, aceite y zanahoria.\n3. Agrega nueces al final.\n4. Hornea hasta que el centro esté firme.\n5. Enfría y cubre con frosting de queso crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener refrigerada por la cobertura.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-ZANA-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Naked Cake de Boda',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcocho de vainilla\n- Crema de relleno\n- Frutos rojos\n- Flores comestibles o decorativas\n- Base rígida\n- Soportes internos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea capas uniformes.\n2. Nivela cada capa antes de montar.\n3. Rellena con crema y fruta controlada.\n4. Aplica acabado naked cake.\n5. Refrigera y transporta con soporte.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Confirmar decoración y altura antes de producir.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-BODA-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Tiramisú Familiar',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcotelas\n- Café concentrado\n- Queso mascarpone o crema\n- Azúcar\n- Cacao en polvo\n- Huevos pasteurizados o crema batida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara café y deja enfriar.\n2. Bate crema con queso y azúcar.\n3. Remoja bizcotelas sin saturarlas.\n4. Arma capas alternando crema y bizcotela.\n5. Refrigera y espolvorea cacao antes de servir.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener siempre refrigerado.'
)::text, updated_at = NOW() WHERE codigo = 'TIRA-FAM-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Pie de Limón Artesanal',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Masa sablé\n- Jugo de limón\n- Leche condensada\n- Yemas\n- Azúcar\n- Claras\n- Ralladura de limón',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea la base.\n2. Prepara crema de limón.\n3. Rellena y lleva a frío.\n4. Bate merengue.\n5. Decora y dora suavemente.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Conservar refrigerado y evitar calor directo.'
)::text, updated_at = NOW() WHERE codigo = 'PIE-LIMON-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Galletas Mix de Mantequilla',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mantequilla\n- Harina\n- Azúcar impalpable\n- Huevo\n- Vainilla\n- Chocolate o mermelada opcional',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa base de mantequilla.\n2. Divide para sabores si aplica.\n3. Forma piezas uniformes.\n4. Hornea por tandas controlando color.\n5. Enfría antes de empacar.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Guardar en envase hermético.'
)::text, updated_at = NOW() WHERE codigo = 'GALL-MIX-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Galleta Corporativa con Logo',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Masa de galleta\n- Glaseado real\n- Colorantes\n- Plantilla o impresión comestible\n- Empaque individual',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara y corta galletas uniformes.\n2. Hornea y deja enfriar.\n3. Aplica base de glaseado.\n4. Coloca logo o decoración.\n5. Empaca individualmente cuando seque.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Validar logo y colores con el cliente.'
)::text, updated_at = NOW() WHERE codigo = 'GALL-CORP-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Frappé Mocha',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Café frío\n- Leche\n- Hielo\n- Chocolate\n- Azúcar o jarabe\n- Crema batida\n- Sirope de chocolate',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Licúa café, leche, hielo y chocolate.\n2. Ajusta dulzor.\n3. Sirve en vaso frío.\n4. Agrega crema batida.\n5. Decora con sirope.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Servir inmediatamente para conservar textura.'
)::text, updated_at = NOW() WHERE codigo = 'FRAP-MOCHA';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Chocolate Caliente de la Casa',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Leche\n- Chocolate semiamargo\n- Cacao\n- Azúcar\n- Canela opcional\n- Marshmallows opcionales',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Calienta la leche sin hervir fuerte.\n2. Integra chocolate y cacao.\n3. Endulza al gusto.\n4. Sirve caliente en taza.\n5. Decora con marshmallows si aplica.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener temperatura segura de servicio.'
)::text, updated_at = NOW() WHERE codigo = 'CHOC-CAL-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake Red Velvet',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Buttermilk\n- Colorante rojo\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos.\n2. Integra líquidos y colorante.\n3. Llena capacillos a dos tercios.\n4. Hornea y enfría.\n5. Decora con frosting y migas rojas.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Refrigerar si el frosting contiene queso crema.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-RED-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake Oreo',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Galletas trituradas\n- Crema cookies and cream',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa de chocolate.\n2. Agrega trozos de galleta.\n3. Hornea en capacillos.\n4. Enfría por completo.\n5. Decora con crema y galleta.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener en caja cerrada para conservar textura.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-OREO-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Caja Brigadeiro 12',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Leche condensada\n- Cacao\n- Mantequilla\n- Chocolate granulado\n- Capacillos\n- Caja rígida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Cocina leche condensada con cacao y mantequilla.\n2. Enfría la mezcla.\n3. Forma bolitas uniformes.\n4. Pasa por granulado.\n5. Coloca en capacillos y caja.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener en lugar fresco y evitar sol directo.'
)::text, updated_at = NOW() WHERE codigo = 'BRIGA-BOX-12';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 80 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres variados\n- Cupcakes\n- Cake pops\n- Galletas\n- Macarons o alfajores\n- Bases decorativas\n- Etiquetas\n- Bandejas grandes',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Define distribución por tipo de postre.\n2. Produce por tandas y controla inventario.\n3. Agrupa fríos y secos por separado.\n4. Prepara bandejas y soportes.\n5. Revisa montaje, transporte y reposición.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Requiere checklist de montaje y apoyo en entrega.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-80';
