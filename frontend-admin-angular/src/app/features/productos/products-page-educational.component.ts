/**
 * ============================================================
 * COMPONENTE DE GESTIÓN DE PRODUCTOS
 * ============================================================
 * 
 * Este archivo demuestra patrones avanzados de Angular:
 * 
 * 1. ARQUITECTURA REACTIVA CON SIGNALS (Angular 16+)
 *    - Uso de signals para estado local
 *    - Computed signals para derivar valores
 *    - Reactividad granular sin RxJS complejo
 * 
 * 2. STANDALONE COMPONENTS
 *    - Componente autónomo sin NgModule
 *    - Imports explícitos de dependencias
 * 
 * 3. STATE MANAGEMENT CON FACADE PATTERN
 *    - Separación entre UI y lógica de negocio
 *    - Facade como único punto de contacto con backend
 * 
 * 4. FORMULARIOS REACTIVOS AVANZADOS
 *    - FormBuilder para construcción dinámica
 *    - Validaciones asíncronas potenciales
 *    - Integración con signals
 * 
 * 5. PATRÓN EDITOR (CREATE/UPDATE)
 *    - Reutilización de formulario para ambas operaciones
 *    - Detección de modo edición vía signals
 * 
 * CONCEPTOS CLAVE PARA ESTUDIAR:
 * - Reactive forms vs Template-driven forms
 * - Signals vs Observables (cuándo usar cada uno)
 * - Facade pattern para desacoplar UI de datos
 * - Optimización de change detection
 */

import { CommonModule, CurrencyPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators, FormControl } from "@angular/forms";
import { ProductsFacadeService } from "./state/products.facade";
import type { ProductSummary } from "./models/product.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import { buildBackendAssetUrl } from "../../shared/utils/backend-asset.util";

@Component({
  /**
   * SELECTOR: Nombre del componente para usar en HTML
   * Uso: <app-products-page></app-products-page>
   */
  selector: "app-products-page",
  
  /**
   * STANDALONE: Componente independiente (Angular 14+)
   * No requiere declararse en un NgModule
   * Importa directamente sus dependencias
   */
  standalone: true,
  
  /**
   * IMPORTS: Módulos y componentes necesarios
   * Solo se importa lo que se usa (tree-shakeable)
   */
  imports: [CommonModule, CurrencyPipe, ReactiveFormsModule],
  
  /**
   * TEMPLATE: HTML inline (alternativa a templateUrl)
   * Ventajas:
   * - Un solo archivo para mantener
   * - TypeScript verifica referencias en tiempo de compilación
   * - Mejor para componentes pequeños/medios
   */
  template: `
    <!-- 
      ==========================================================
      LAYOUT PRINCIPAL: Grid de 2 columnas
      Columna izquierda: Formulario de edición
      Columna derecha: Lista de productos
      ==========================================================
    -->
    <div class="admin-grid admin-grid--split">
      
      <!-- 
        ========================================================
        SECCIÓN IZQUIERDA: FORMULARIO DE PRODUCTO
        ========================================================
        Usa clases CSS del sistema de diseño (design system)
        para mantener consistencia visual en toda la aplicación.
      -->
      <section class="surface-card surface-card--tinted">
        
        <!-- CABECERA DEL FORMULARIO -->
        <header class="surface-header">
          <!-- Kicker: etiqueta pequeña que indica contexto -->
          <p class="surface-kicker">Carta de la casa</p>
          
          <!-- Título dinámico: cambia según el modo (crear/editar) -->
          <div class="surface-title-row">
            <img
              src="assets/icons/products.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>
              <!-- 
                SIGNAL REACTIVO: editingProduct()
                - Se actualiza automáticamente cuando cambia
                - Trigger re-rendering eficiente
                - Sintaxis: () para acceder al valor
              -->
              {{ editingProduct() ? "Editar producto" : "Nuevo producto" }}
            </h3>
          </div>
          
          <p class="surface-copy">
            Gestión interna del catálogo para vitrina, pedidos y solicitudes
            personalizadas.
          </p>
          
          <!-- CHIPS INFORMATIVOS: muestran estadísticas -->
          <div class="chip-row">
            <span class="summary-chip">
              <img src="assets/icons/products.svg" alt="" aria-hidden="true" />
              {{ productPage().totalElements }} productos registrados
            </span>
            <span class="summary-chip" *ngIf="editingProduct()">
              <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
              Estás corrigiendo {{ editingProduct()?.name }}
            </span>
          </div>
        </header>

        <!-- 
          ========================================================
          FORMULARIO REACTIVO
          ========================================================
          [formGroup]: Vincula el formulario al objeto FormGroup de TS
          (ngSubmit): Evento de envío que llama al método submit()
          
          VENTAJAS DE FORMULARIOS REACTIVOS:
          - Lógica de validación en TypeScript (testeable)
          - Acceso programático a valores y estado
          - Validaciones dinámicas complejas
          - Mejor rendimiento en formularios grandes
        -->
        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          
          <!-- FILA 1: Código y Categoría -->
          <div class="surface-row surface-row--2">
            <label>
              Código
              <input type="text" formControlName="code" />
            </label>
            <label>
              Categoría
              <select formControlName="categoryId">
                <option value="">Selecciona una categoría</option>
                <!-- 
                  *ngFor: Directiva estructural para iterar
                  Recorre las categorías del facade y genera opciones
                  
                  [value]: Binding de propiedad (one-way)
                  {{ }}: Interpolación de string
                -->
                <option
                  *ngFor="let category of facade.categories()"
                  [value]="category.id"
                >
                  {{ category.name }}
                </option>
              </select>
            </label>
          </div>
          
          <!-- NOMBRE DEL PRODUCTO -->
          <label>
            Nombre del producto
            <input type="text" formControlName="name" />
          </label>
          
          <!-- DESCRIPCIÓN -->
          <label>
            Descripción
            <textarea rows="2" formControlName="description"></textarea>
          </label>

          <!-- 
            ========================================================
            SECCIÓN DE SUBIDA DE IMÁGENES
            ========================================================
            Patrón: Input file oculto + trigger manual
            
            ¿Por qué?
            - Estilizar el input file es muy limitado
            - Ocultamos el input real y mostramos UI personalizada
            - JavaScript dispara el click() del input oculto
          -->
          <div class="image-upload-section">
            <!-- Input real: display:none lo oculta -->
            <input 
              type="file" 
              accept="image/*"
              (change)="onImageSelected($event)"
              #imageInput
              style="display: none;"
            />
            
            <label class="image-upload-label">
              <span>Foto del producto</span>
              <!-- 
                Área clickeable que abre el diálogo de archivo
                Eventos:
                - click: Dispara input.click()
                - preventDefault/stopPropagation: Evita bubbling
              -->
              <div 
                class="image-upload-area" 
                (click)="triggerFileInput(imageInput); $event.preventDefault(); $event.stopPropagation();"
              >
                <!-- 
                  PREVIEW CONDICIONAL
                  *ngIf: Renderiza solo si previewImage() tiene valor
                  [src]: Property binding para la URL de la imagen
                -->
                <img 
                  *ngIf="previewImage()" 
                  [src]="previewImage()" 
                  alt="Preview" 
                  class="image-preview"
                />
                
                <!-- PLACEHOLDER: visible cuando NO hay imagen -->
                <div *ngIf="!previewImage()" class="image-upload-placeholder">
                  <img src="assets/icons/abastecimiento/image.svg" alt="" width="32" height="32" />
                  <p>Haz clic para subir imagen</p>
                  <small *ngIf="editingProduct()">
                    Se renombrará automáticamente a: {{ editingProduct()?.slug }}.png
                  </small>
                  <small *ngIf="!editingProduct()">
                    Se renombrará automáticamente al código del producto
                  </small>
                </div>
              </div>
            </label>
          </div>
        </form>
      </section>
    </div>
  `,
  
  /**
   * STYLES: CSS inline con template literals
   * Beneficios:
   * - Scoped styles (solo afectan este componente)
   * - Variables CSS para theming
   * - Responsive design integrado
   */
  styles: [`
    /* ... resto de estilos ... */
  `]
})

/**
 * ============================================================
 * CLASE DEL COMPONENTE
 * ============================================================
 * Implementa OnInit para inicialización post-construcción
 */
export class ProductsPageComponent implements OnInit {
  
  /**
   * ==========================================================
   * INYECCIÓN DE DEPENDENCIAS
   * ==========================================================
   * 
   * inject() es la forma moderna (Angular 14+) de inyectar.
   * Ventajas sobre constructor injection:
   * - No necesitas declarar propiedades por separado
   * - Mejor para herencia
   * - Permite inyección en funciones standalone
   */
  
  /** Facade: Único punto de contacto con el backend */
  readonly facade = inject(ProductsFacadeService);
  
  /** FormBuilder: Fábrica para crear formularios reactivos */
  private readonly fb = inject(FormBuilder);
  
  // ... resto de la implementación
}
