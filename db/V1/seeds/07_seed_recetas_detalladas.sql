-- Actualizar productos con recetas detalladas
UPDATE producto SET 
receta_json = '{
  "titulo": "Torta de Chocolate Especial",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 500g de harina de trigo\n• 300g de azúcar\n• 200g de mantequilla\n• 4 huevos grandes\n• 200ml de leche\n• 100g de cacao en polvo\n• 1 cda de polvo de hornear\n• 1 cdta de esencia de vainilla\n• 100g de chocolate negro",
  "tituloPasos": "Preparación",
  "pasos": "1. Precalentar el horno a 180°C.\n2. Tamizar la harina con el cacao y polvo de hornear.\n3. Batir la mantequilla con el azúcar hasta obtener una crema.\n4. Agregar los huevos uno por uno, batiendo bien.\n5. Incorporar los secos alternando con la leche.\n6. Agregar la esencia de vainilla.\n7. Verter en molde engrasado y hornear por 45 minutos.\n8. Dejar enfriar antes de desmoldar.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Usar ingredientes a temperatura ambiente\n• No abrir el horno antes de 30 minutos\n• Se puede decorar con ganache de chocolate"
}'::jsonb
WHERE codigo = 'TORT-CHOC-01';

UPDATE producto SET 
receta_json = '{
  "titulo": "Torta Tres Leches Clásica",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 5 huevos separados\n• 150g de azúcar\n• 150g de harina de trigo\n• 1 cdta de polvo de hornear\n• 1 lata de leche condensada\n• 1 lata de leche evaporada\n• 500ml de crema de leche\n• 1 cdta de esencia de vainilla",
  "tituloPasos": "Preparación",
  "pasos": "1. Batir las claras a punto de nieve.\n2. Agregar las yemas una a una.\n3. Incorporar el azúcar gradualmente.\n4. Tamizar harina y polvo de hornear, agregar suavemente.\n5. Hornear a 180°C por 35 minutos.\n6. Mezclar las tres leches con vainilla.\n7. Perforar la torta caliente y verter la mezcla.\n8. Refrigerar mínimo 4 horas antes de servir.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Debe reposar toda la noche para mejor sabor\n• Se puede decorar con merengue o crema chantilly\n• Ideal para eventos especiales"
}'::jsonb
WHERE codigo = 'TORT-3-LECH';

UPDATE producto SET 
receta_json = '{
  "titulo": "Brownie Americano",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 200g de chocolate negro 70%\n• 150g de mantequilla\n• 200g de azúcar\n• 3 huevos\n• 100g de harina\n• 50g de nueces picadas\n• 1 cdta de esencia de vainilla\n• Pizca de sal",
  "tituloPasos": "Preparación",
  "pasos": "1. Derretir chocolate con mantequilla a baño maría.\n2. Batir huevos con azúcar hasta disolver.\n3. Agregar el chocolate derretido.\n4. Incorporar harina tamizada y sal.\n5. Agregar nueces y vainilla.\n6. Verter en molde cuadrado engrasado.\n7. Hornear a 170°C por 25 minutos.\n8. Dejar enfriar antes de cortar.",
  "tituloObservaciones": "Notas",
  "observaciones": "• No sobrehornear para mantenerlo húmedo\n• Se puede añadir chocolate chips\n• Se congela muy bien"
}'::jsonb
WHERE codigo = 'BROWNIE-IND';

UPDATE producto SET 
receta_json = '{
  "titulo": "Alfajor de Maicena",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 250g de maicena\n• 150g de harina\n• 200g de mantequilla\n• 150g de azúcar impalpable\n• 3 yemas\n• 1 cdta de polvo de hornear\n• Ralladura de 1 limón\n• 400g de dulce de leche repostero",
  "tituloPasos": "Preparación",
  "pasos": "1. Batir mantequilla pomada con azúcar.\n2. Agregar yemas y ralladura.\n3. Tamizar maicena, harina y polvo.\n4. Formar masa suave sin amasar.\n5. Estirar a 5mm y cortar círculos.\n6. Hornear a 160°C por 12 minutos.\n7. Unir con abundante dulce de leche.\n8. Pasar por coco rallado.",
  "tituloObservaciones": "Notas",
  "observaciones": "• La masa debe reposar 30 minutos\n• No dorar demasiado\n• Consumir dentro de 5 días"
}'::jsonb
WHERE codigo = 'ALFAJOR-PREM';

UPDATE producto SET 
receta_json = '{
  "titulo": "Cupcake de Vainilla",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 200g de harina\n• 150g de azúcar\n• 100g de mantequilla\n• 2 huevos\n• 120ml de leche\n• 1 cdta de esencia de vainilla\n• 1 cdta de polvo de hornear\n• Pizca de sal",
  "tituloPasos": "Preparación",
  "pasos": "1. Precalentar horno a 170°C.\n2. Batir mantequilla con azúcar.\n3. Agregar huevos uno por uno.\n4. Agregar vainilla.\n5. Incorporar harina alternando con leche.\n6. Llenar capacillos 2/3 partes.\n7. Hornear 18-20 minutos.\n8. Dejar enfriar antes de decorar.",
  "tituloObservaciones": "Notas",
  "observaciones": "• No llenar hasta arriba o se derrama\n• Se pueden congelar sin frosting\n• Decorar con buttercream o ganache"
}'::jsonb
WHERE codigo = 'CUPK-VAIN-01';

UPDATE producto SET 
receta_json = '{
  "titulo": "Cheesecake de Frutos Rojos",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 200g de galletas trituradas\n• 100g de mantequilla derretida\n• 600g de queso crema\n• 200g de azúcar\n• 200ml de crema de leche\n• 3 huevos\n• Ralladura de limón\n• 200g de frutos rojos mixtos",
  "tituloPasos": "Preparación",
  "pasos": "1. Mezclar galletas con mantequilla.\n2. Forrar base de molde desmontable.\n3. Batir queso crema con azúcar.\n4. Agregar huevos uno a uno.\n5. Incorporar crema y ralladura.\n6. Verter sobre la base.\n7. Hornear a 160°C por 50 minutos.\n8. Enfriar y refrigerar 6 horas.\n9. Decorar con frutos rojos.",
  "tituloObservaciones": "Notas",
  "observaciones": "• El centro debe quedar levemente tembloroso\n• Se puede hacer sin horno con gelatina\n• Ideal con salsa de frutos rojos"
}'::jsonb
WHERE codigo = 'CHEESE-FRUT';

UPDATE producto SET 
receta_json = '{
  "titulo": "Galleta Decorada",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 300g de harina\n• 150g de azúcar glass\n• 150g de mantequilla\n• 1 huevo\n• 1 cdta de esencia de vainilla\n• 1 pizca de sal\n• Colorantes vegetales\n• Royal icing para decorar",
  "tituloPasos": "Preparación",
  "pasos": "1. Batir mantequilla con azúcar.\n2. Agregar huevo y vainilla.\n3. Incorporar harina y sal.\n4. Formar disco y refrigerar 1 hora.\n5. Estirar y cortar formas.\n6. Hornear a 160°C por 12 min.\n7. Dejar enfriar completamente.\n8. Decorar con royal icing.",
  "tituloObservaciones": "Notas",
  "observaciones": "• La masa no debe ser manejada mucho\n• Refrigerar antes de hornear evita deformación\n• Guardar en lugar fresco y seco"
}'::jsonb
WHERE codigo = 'GALLETA-DEC';

UPDATE producto SET 
receta_json = '{
  "titulo": "Mesa Dulce Completa",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• Variedad de mini tortas\n• Mini cupcakes (2 por persona)\n• Galletas decoradas\n• Macarons surtidos\n• Cake pops\n• Alfajores\n• Brownies\n• Mesa decorada\n• Base de presentación",
  "tituloPasos": "Preparación",
  "pasos": "1. Planificar cantidades según invitados.\n2. Preparar productos con 2 días de anticipación.\n3. Congelar lo que sea posible.\n4. Decorar 1 día antes.\n5. Montar mesa 4 horas antes del evento.\n6. Mantener refrigerado hasta el momento.\n7. Decorar con elementos temáticos.\n8. Incluir etiquetas de productos.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Requiere cotización previa\n• Se personaliza según temática\n• Incluye servicio de montaje"
}'::jsonb
WHERE codigo LIKE 'MESA-DULCE-%';

-- Verificar recetas creadas
SELECT codigo, nombre, 
CASE WHEN receta_json IS NOT NULL THEN 'TIENE RECETA' ELSE 'SIN RECETA' END as estado
FROM producto ORDER BY codigo;
