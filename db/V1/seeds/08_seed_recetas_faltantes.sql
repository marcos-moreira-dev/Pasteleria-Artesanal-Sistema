-- Agregar recetas a productos restantes
UPDATE producto SET 
receta_json = '{
  "titulo": "Café Americano Premium",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 20g de café molido 100% arábica\n• 200ml de agua filtrada\n• Hielo (opcional)\n• Azúcar al gusto",
  "tituloPasos": "Preparación",
  "pasos": "1. Calentar agua a 90-95°C.\n2. Pesar café recién molido.\n3. Colocar filtro en cafetera.\n4. Agregar café molido.\n5. Verter agua lentamente.\n6. Dejar filtrar 3-4 minutos.\n7. Servir inmediatamente.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Usar café molido recién molido\n• El agua no debe hervir\n• Consumir dentro de 30 minutos"
}'::jsonb
WHERE codigo = 'CAFE-AMER';

UPDATE producto SET 
receta_json = '{
  "titulo": "Caja Mini Postres Variados",
  "tituloIngredientes": "Contenido",
  "ingredientes": "• 2 mini brownies\n• 2 alfajores de maicena\n• 2 galletas decoradas\n• 2 trufas de chocolate\n• 1 macaron\n• Tarjeta de presentación\n• Caja decorativa",
  "tituloPasos": "Armado",
  "pasos": "1. Preparar todos los productos mini.\n2. Seleccionar caja apropiada.\n3. Colocar base de papel seda.\n4. Acomodar productos de mayor a menor.\n5. Agregar separadores si es necesario.\n6. Cerrar caja con cinta decorativa.\n7. Colocar tarjeta personalizada.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Ideal para regalo\n• Personalizable según ocasión\n• Duración: 5 días"
}'::jsonb
WHERE codigo = 'CAJA-MINI';

UPDATE producto SET 
receta_json = '{
  "titulo": "Cupcake de Chocolate Intenso",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 200g de harina\n• 180g de azúcar\n• 80g de cacao en polvo\n• 100g de mantequilla\n• 2 huevos\n• 120ml de leche\n• 100g de chocolate fundido\n• 1 cdta de polvo de hornear",
  "tituloPasos": "Preparación",
  "pasos": "1. Precalentar horno a 170°C.\n2. Batir mantequilla con azúcar.\n3. Agregar huevos y chocolate tibio.\n4. Tamizar harina, cacao y polvo.\n5. Alternar con leche.\n6. Llenar capacillos 2/3.\n7. Hornear 20 minutos.\n8. Decorar con ganache.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Usar cacao de buena calidad\n• Ganache debe estar tibia para decorar\n• Se pueden congelar"
}'::jsonb
WHERE codigo = 'CUPK-CHOC-01';

UPDATE producto SET 
receta_json = '{
  "titulo": "Torta Red Velvet Clásica",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 300g de harina\n• 250g de azúcar\n• 100g de mantequilla\n• 2 huevos\n• 240ml de suero de leche\n• 2 cdas de cacao\n• Colorante rojo\n• 1 cdta de vinagre",
  "tituloPasos": "Preparación",
  "pasos": "1. Precalentar horno a 175°C.\n2. Batir mantequilla y azúcar.\n3. Agregar huevos y colorante.\n4. Tamizar harina y cacao.\n5. Alternar con suero de leche.\n6. Agregar vinagre.\n7. Hornear 30 minutos.\n8. Cubrir con frosting de queso crema.",
  "tituloObservaciones": "Notas",
  "observaciones": "• El colorante debe ser en gel\n• No sustituir el vinagre\n• El frosting es esencial"
}'::jsonb
WHERE codigo = 'TORT-RED-VEL';

UPDATE producto SET 
receta_json = '{
  "titulo": "Torta Vainilla con Frutos",
  "tituloIngredientes": "Ingredientes",
  "ingredientes": "• 300g de harina\n• 250g de azúcar\n• 150g de mantequilla\n• 4 huevos\n• 200ml de leche\n• Esencia de vainilla\n• 200g de frutos rojos mixtos\n• Polvo de hornear",
  "tituloPasos": "Preparación",
  "pasos": "1. Precalentar horno a 180°C.\n2. Batir mantequilla y azúcar.\n3. Agregar huevos y vainilla.\n4. Incorporar harina y polvo.\n5. Agregar leche gradualmente.\n6. Envolver frutos en harina.\n7. Incorporar suavemente.\n8. Hornear 40 minutos.",
  "tituloObservaciones": "Notas",
  "observaciones": "• Los frutos deben estar secos\n• No sobrebatir la mezcla\n• Se puede usar fruta congelada"
}'::jsonb
WHERE codigo = 'TORT-VAIN-FRUT';

-- Verificar que todos tengan recetas
SELECT COUNT(*) as productos_con_receta 
FROM producto 
WHERE receta_json IS NOT NULL;
