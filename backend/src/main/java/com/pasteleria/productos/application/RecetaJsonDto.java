package com.pasteleria.productos.application;

/**
 * DTO que representa la estructura JSON de una receta.
 * 
 * Almacena los campos estructurados de una receta:
 * - titulo: Título principal de la receta
 * - tituloIngredientes: Título de la sección de ingredientes (ej: "Ingredientes", "Para la masa")
 * - ingredientes: Lista de ingredientes con cantidades
 * - tituloPasos: Título de la sección de preparación (ej: "Preparación", "Pasos")
 * - pasos: Instrucciones paso a paso
 * - tituloObservaciones: Título de notas (ej: "Notas", "Tips", "Observaciones")
 * - observaciones: Notas adicionales, tips, variaciones
 */
public record RecetaJsonDto(
    String titulo,
    String tituloIngredientes,
    String ingredientes,
    String tituloPasos,
    String pasos,
    String tituloObservaciones,
    String observaciones
) {
  
  /**
   * Constructor con valores por defecto
   */
  public RecetaJsonDto() {
    this("", "Ingredientes", "", "Preparación", "", "Notas", "");
  }
  
  /**
   * Crea una instancia con título y valores por defecto para las secciones
   */
  public static RecetaJsonDto withDefaults(String titulo) {
    return new RecetaJsonDto(
        titulo,
        "Ingredientes",
        "",
        "Preparación", 
        "",
        "Notas",
        ""
    );
  }
}
