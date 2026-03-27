import { CommonModule, CurrencyPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators, FormControl } from "@angular/forms";
import { ProductsFacadeService } from "./state/products.facade";
import type { ProductSummary } from "./models/product.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import { buildBackendAssetUrl } from "../../shared/utils/backend-asset.util";

@Component({
  selector: "app-products-page",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Carta de la casa</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/products.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>
              {{ editingProduct() ? "Editar producto" : "Nuevo producto" }}
            </h3>
          </div>
          <p class="surface-copy">
            Gestión interna del catálogo para vitrina, pedidos y solicitudes
            personalizadas.
          </p>
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

        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          <div class="surface-row surface-row--2">
            <label>Código<input type="text" formControlName="code" /></label>
            <label
              >Categoría
              <select formControlName="categoryId">
                <option value="">Selecciona una categoría</option>
                <option
                  *ngFor="let category of facade.categories()"
                  [value]="category.id"
                >
                  {{ category.name }}
                </option>
              </select>
            </label>
          </div>
          <label
            >Nombre del producto<input type="text" formControlName="name"
          /></label>
          <label
            >Descripción<textarea
              rows="2"
              formControlName="description"
            ></textarea>
          </label>

          <!-- SUBIR IMAGEN DEL PRODUCTO -->
          <div class="image-upload-section">
            <input 
              type="file" 
              accept="image/*"
              (change)="onImageSelected($event)"
              #imageInput
              class="visually-hidden-file-input"
            />
            <label class="image-upload-label">
              <span>Foto del producto</span>
              <div class="image-upload-area" (click)="triggerFileInput(imageInput); $event.preventDefault(); $event.stopPropagation();">
                <img 
                  *ngIf="previewImage()" 
                  [src]="previewImage()" 
                  alt="Preview" 
                  class="image-preview"
                />
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
            <p class="image-help" *ngIf="selectedFile()">
              <span>📷 {{ selectedFile()?.name }}</span>
              <button *ngIf="editingProduct()" type="button" class="mini-button" (click)="uploadImage()">
                Subir imagen
              </button>
              <button *ngIf="!editingProduct()" type="button" class="mini-button" (click)="clearImageSelection()">
                Quitar
              </button>
            </p>
          </div>

          <!-- SECCIÓN RECETA ESTRUCTURADA -->
          <div class="receta-section" formGroupName="receta">
            <div class="section-header">
              <img
                src="assets/icons/abastecimiento/book-open.svg"
                alt=""
                width="20"
                height="20"
              />
              <h4>Receta del Producto</h4>

            </div>

            <label class="receta-titulo-label"
              >Título de la receta<input
                type="text"
                formControlName="titulo"
                placeholder="Ej: Torta de Chocolate Especial"
            /></label>

            <div class="receta-grid">
              <div class="receta-column">
                <label class="receta-subtitulo"
                  >Título sección<input
                    type="text"
                    formControlName="tituloIngredientes"
                    placeholder="Ingredientes"
                /></label>
                <label
                  >Lista de ingredientes<textarea
                    rows="8"
                    formControlName="ingredientes"
                    placeholder="• 500g harina&#10;• 300g azúcar&#10;• 200g mantequilla..."
                    (keydown)="onIngredientesKeydown($event)"
                    (focus)="onTextareaFocus('ingredientes', '• ')"
                    (paste)="onIngredientesPaste($event)"
                  ></textarea>
                </label>
              </div>

              <div class="receta-column">
                <label class="receta-subtitulo"
                  >Título sección<input
                    type="text"
                    formControlName="tituloPasos"
                    placeholder="Preparación"
                /></label>
                <label
                  >Instrucciones paso a paso<textarea
                    rows="8"
                    formControlName="pasos"
                    placeholder="1. Precalentar el horno...&#10;2. Mezclar los ingredientes secos...&#10;3. Agregar los líquidos..."
                    (keydown)="onPasosKeydown($event)"
                    (focus)="onTextareaFocus('pasos', '1. ')"
                    (paste)="onPasosPaste($event)"
                  ></textarea>
                </label>
              </div>
            </div>

            <label class="receta-subtitulo"
              >Título sección<input
                type="text"
                formControlName="tituloObservaciones"
                placeholder="Notas / Tips"
            /></label>
            <label
              >Observaciones y tips<textarea
                rows="3"
                formControlName="observaciones"
                placeholder="• Tiempo de horneado: 45 minutos&#10;• Temperatura: 180°C&#10;• Conservación: 3 días en refrigeración"
                (keydown)="onObservacionesKeydown($event)"
                (focus)="onTextareaFocus('observaciones', '• ')"
                (paste)="onObservacionesPaste($event)"
              ></textarea>
            </label>
          </div>
          <div class="surface-row surface-row--2">
            <label
              >Precio base<input
                type="number"
                min="0"
                step="0.01"
                formControlName="basePrice"
            /></label>
            <label class="toggle-row">
              <input type="checkbox" formControlName="quotationRequired" />
              Requiere cotización previa
            </label>
          </div>
          <div class="surface-row surface-row--2">
            <label class="toggle-row">
              <input type="checkbox" formControlName="active" />
              Producto activo
            </label>
            <label class="toggle-row">
              <input type="checkbox" formControlName="published" />
              Visible en catálogo público
            </label>
          </div>

          <div class="action-row">
            <button
              type="submit"
              class="surface-button"
              [disabled]="form.invalid"
            >
              {{ editingProduct() ? "Guardar cambios" : "Registrar producto" }}
            </button>
            <button
              type="button"
              class="mini-button"
              *ngIf="editingProduct()"
              (click)="resetForm()"
            >
              Cancelar edición
            </button>
            <button
              type="button"
              class="mini-button mini-button--secondary"
              *ngIf="editingProduct()?.receta?.titulo"
              (click)="downloadRecetaPDF()"
            >
              <img
                src="assets/icons/reports.svg"
                alt=""
                width="16"
                height="16"
              />
              Descargar receta PDF
            </button>
          </div>
        </form>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Catálogo administrativo</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/dashboard.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Productos del sistema</h3>
          </div>
          <p class="surface-copy">
            {{ activePublishedCount() }} publicados y
            {{ inactiveCount() }} fuera de vitrina.
          </p>
        </header>

        <div class="page-toolbar" *ngIf="productPage().totalElements">
          <p class="pager__meta">{{ pageSummary() }}</p>
          <label class="pager__size">
            Tarjetas por página
            <select
              [value]="productPage().size"
              (change)="changePageSize($any($event.target).value)"
            >
              <option value="6">6</option>
              <option value="8">8</option>
              <option value="12">12</option>
            </select>
          </label>
        </div>

        <div
          class="cards-grid"
          *ngIf="productPage().content.length; else empty"
        >
          <article *ngFor="let product of productPage().content">
            <div class="product-visual">
              <img
                class="product-visual__logo"
                [src]="resolveProductImage(product)"
                [alt]="product.imageAlt"
              />
              <div class="product-visual__copy">
                <strong>{{ product.slug }}</strong>
              </div>
            </div>
            <div class="chip-row">
              <span class="status-pill">{{ product.categoryName }}</span>
              <span class="summary-chip">{{
                product.active ? "Activo" : "Inactivo"
              }}</span>
              <span class="summary-chip">{{
                product.published ? "Publicado" : "Interno"
              }}</span>
            </div>
            <h4>{{ product.name }}</h4>
            <p class="card-copy">
              {{ product.description || "Producto sin descripción pública." }}
            </p>
            <strong class="product-price">{{
              product.basePrice | currency: "USD" : "symbol" : "1.2-2"
            }}</strong>
            <p class="surface-meta">
              {{ product.code }} ·
              {{
                product.quotationRequired ? "Con cotización" : "Venta directa"
              }}
            </p>
            <div class="action-row">
              <button
                type="button"
                class="mini-button mini-button--icon"
                (click)="startEdit(product)"
              >
                <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
                Editar
              </button>
              <button
                type="button"
                class="mini-button mini-button--icon"
                *ngIf="product.receta?.titulo || product.receta?.ingredientes"
                (click)="downloadRecetaPDFInline(product)"
                title="Descargar receta"
              >
                <img src="assets/icons/abastecimiento/download.svg" alt="" aria-hidden="true" />
                Receta
              </button>
              <button
                type="button"
                class="mini-button mini-button--danger"
                (click)="deleteProduct(product)"
              >
                Eliminar
              </button>
            </div>
          </article>
        </div>

        <ng-template #empty>
          <div class="empty-state">No hay productos cargados todavía.</div>
        </ng-template>

        <div class="pager" *ngIf="productPage().totalElements">
          <p class="pager__meta">
            Página {{ productPage().page + 1 }} de
            {{ productPage().totalPages || 1 }}
          </p>
          <div class="pager__controls">
            <button
              type="button"
              class="mini-button"
              [disabled]="productPage().first"
              (click)="goToPage(productPage().page - 1)"
            >
              Anterior
            </button>
            <button
              type="button"
              class="mini-button"
              [disabled]="productPage().last"
              (click)="goToPage(productPage().page + 1)"
            >
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .toggle-row {
        display: flex !important;
        align-items: center;
        gap: 0.65rem;
        min-height: 100%;
      }

      .toggle-row input {
        width: auto;
        margin: 0;
      }

      .mini-button--icon {
        display: inline-flex;
        align-items: center;
        gap: 0.45rem;
      }

      .mini-button--icon img {
        width: 16px;
        height: 16px;
      }

      .product-visual {
        display: grid;
        grid-template-columns: 96px 1fr;
        gap: 0.8rem;
        align-items: center;
        padding: 0.8rem;
        margin-bottom: 0.9rem;
        border: 1px dashed #e7d2c4;
        background: linear-gradient(
          135deg,
          rgba(255, 244, 237, 0.98),
          rgba(252, 235, 225, 0.85)
        );
      }

      .product-visual__logo {
        width: 96px !important;
        height: 72px !important;
        padding: 0 !important;
        border-radius: 4px !important;
        background: transparent !important;
        object-fit: cover;
        border: 1px solid #ead6ca;
      }

      .product-visual__copy {
        display: grid;
        gap: 0.25rem;
        color: #6c564c;
        line-height: 1.45;
      }

      .product-visual__copy strong {
        color: #4d3023;
        font-size: 0.92rem;
      }

      .product-visual__copy span {
        font-size: 0.83rem;
      }

      .product-price {
        font-size: 1.15rem;
        color: #4f2519;
      }

      .mini-button--danger {
        background: #f7ddd8;
        color: #8a2f2c;
      }

      .mini-button--secondary {
        background: #f0e8e2;
        color: #5a3424;
        border: 1px solid #c4a77d;
      }

      .mini-button--secondary:hover {
        background: #e8ded4;
      }

      /* ESTILOS SECCIÓN RECETA */
      .receta-section {
        background: linear-gradient(135deg, #faf7f4 0%, #f5ebe3 100%);
        border: 1px solid #e7d2c4;
        border-radius: 8px;
        padding: 1.5rem;
        margin: 1rem 0;
      }

      .section-header {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        margin-bottom: 1.25rem;
        padding-bottom: 0.75rem;
        border-bottom: 2px solid #c4a77d;
      }

      .section-header h4 {
        margin: 0;
        color: #5a3424;
        font-size: 1.1rem;
        font-weight: 600;
      }

      .section-header img {
        opacity: 0.8;
      }

      .receta-titulo-label input {
        font-weight: 600;
        font-size: 1.05rem;
        border: 2px solid #c4a77d;
      }

      .receta-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1.5rem;
        margin: 1rem 0;
      }

      @media (max-width: 768px) {
        .receta-grid {
          grid-template-columns: 1fr;
        }
      }

      .receta-column {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
      }

      .receta-subtitulo input {
        font-size: 0.9rem;
        font-weight: 600;
        background: #f0e8e2;
        border: 1px solid #c4a77d;
        padding: 0.5rem 0.75rem;
      }

      .receta-column textarea {
        min-height: 200px;
        font-family: "Courier New", monospace;
        font-size: 0.9rem;
        line-height: 1.6;
      }

      .image-upload-section {
        margin: 1.5rem 0;
        padding: 1rem;
        background: #faf7f4;
        border: 1px dashed #c4a77d;
        border-radius: 6px;
      }

      .visually-hidden-file-input {
        display: none;
      }

      .image-upload-label span {
        display: block;
        font-weight: 600;
        color: #5a3424;
        margin-bottom: 0.5rem;
      }

      .image-upload-area {
        cursor: pointer;
        text-align: center;
        padding: 2rem;
        background: white;
        border: 2px dashed #d4c4b8;
        border-radius: 4px;
        transition: all 0.2s;
      }

      .image-upload-area:hover {
        border-color: #8a5c46;
        background: #fff;
      }

      .image-preview {
        max-width: 100%;
        max-height: 200px;
        border-radius: 4px;
      }

      .image-upload-placeholder {
        color: #8a5c46;
      }

      .image-upload-placeholder p {
        margin: 0.5rem 0 0.25rem;
        font-weight: 500;
      }

      .image-upload-placeholder small {
        color: #a08070;
        font-size: 0.8rem;
      }

      .image-help {
        margin-top: 0.75rem;
        display: flex;
        align-items: center;
        gap: 0.75rem;
        font-size: 0.9rem;
        color: #5a3424;
      }
    `,
  ],
})
export class ProductsPageComponent implements OnInit {
  readonly facade = inject(ProductsFacadeService);
  private readonly fb = inject(FormBuilder);
  readonly editingProduct = signal<ProductSummary | null>(null);
  readonly productPage = computed(() => this.facade.productsPage());
  readonly activePublishedCount = computed(
    () =>
      this.facade.products().filter((item) => item.active && item.published)
        .length,
  );
  readonly inactiveCount = computed(
    () =>
      this.facade.products().filter((item) => !item.active || !item.published)
        .length,
  );

  // Manejo de imágenes
  readonly selectedFile = signal<File | null>(null);
  readonly previewImage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    categoryId: ["", Validators.required],
    code: ["", Validators.required],
    name: ["", Validators.required],
    description: [""],
    receta: this.fb.nonNullable.group({
      titulo: [""],
      tituloIngredientes: ["Ingredientes"],
      ingredientes: [""],
      tituloPasos: ["Preparación"],
      pasos: [""],
      tituloObservaciones: ["Notas"],
      observaciones: [""],
    }),
    basePrice: [0, Validators.required],
    quotationRequired: [false],
    active: [true],
    published: [true],
  });

  ngOnInit() {
    this.facade.loadPage();
  }

  submit() {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    const recetaValue = value.receta;

    // Verificar si la receta tiene contenido
    const hasRecetaContent =
      recetaValue.titulo?.trim() ||
      recetaValue.ingredientes?.trim() ||
      recetaValue.pasos?.trim() ||
      recetaValue.observaciones?.trim();

    const payload = {
      categoryId: Number(value.categoryId),
      code: value.code.trim(),
      name: value.name.trim(),
      description: value.description.trim() || null,
      receta: hasRecetaContent
        ? {
            titulo: recetaValue.titulo?.trim() || "",
            tituloIngredientes:
              recetaValue.tituloIngredientes?.trim() || "Ingredientes",
            ingredientes: recetaValue.ingredientes?.trim() || "",
            tituloPasos: recetaValue.tituloPasos?.trim() || "Preparación",
            pasos: recetaValue.pasos?.trim() || "",
            tituloObservaciones:
              recetaValue.tituloObservaciones?.trim() || "Notas",
            observaciones: recetaValue.observaciones?.trim() || "",
          }
        : null,
      basePrice: Number(value.basePrice),
      quotationRequired: value.quotationRequired,
      active: value.active,
      published: value.published,
    };

    const editing = this.editingProduct();
    const selectedImage = this.selectedFile();
    
    if (editing) {
      this.facade.updateProduct(editing.id, payload, () => {
        // Si hay imagen seleccionada y estamos editando, subirla
        if (selectedImage) {
          setTimeout(() => {
            this.uploadImage();
          }, 500);
        }
        this.resetForm();
      });
    } else {
      this.facade.createProduct(payload, () => {
        this.resetForm();
      });
    }
  }

  startEdit(product: ProductSummary) {
    this.editingProduct.set(product);
    const category = this.facade
      .categories()
      .find((item) => item.code === product.categoryCode);

    const receta = product.receta || {
      titulo: "",
      tituloIngredientes: "Ingredientes",
      ingredientes: "",
      tituloPasos: "Preparación",
      pasos: "",
      tituloObservaciones: "Notas",
      observaciones: "",
    };

    this.form.reset({
      categoryId: category ? String(category.id) : "",
      code: product.code,
      name: product.name,
      description: product.description ?? "",
      receta: {
        titulo: receta.titulo || "",
        tituloIngredientes: receta.tituloIngredientes || "Ingredientes",
        ingredientes: receta.ingredientes || "",
        tituloPasos: receta.tituloPasos || "Preparación",
        pasos: receta.pasos || "",
        tituloObservaciones: receta.tituloObservaciones || "Notas",
        observaciones: receta.observaciones || "",
      },
      basePrice: product.basePrice,
      quotationRequired: product.quotationRequired,
      active: product.active,
      published: product.published,
    });
  }

  resetForm() {
    this.editingProduct.set(null);
    this.selectedFile.set(null);
    this.previewImage.set(null);
    this.form.reset({
      categoryId: "",
      code: "",
      name: "",
      description: "",
      receta: {
        titulo: "",
        tituloIngredientes: "Ingredientes",
        ingredientes: "",
        tituloPasos: "Preparación",
        pasos: "",
        tituloObservaciones: "Notas",
        observaciones: "",
      },
      basePrice: 0,
      quotationRequired: false,
      active: true,
      published: true,
    });
  }

  generateSlug(code: string): string {
    return code.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
  }

  deleteProduct(product: ProductSummary) {
    if (
      !window.confirm(
        `Se eliminará ${product.name} si no tiene pedidos o cotizaciones asociados. ¿Deseas continuar?`,
      )
    ) {
      return;
    }
    this.facade.deleteProduct(product.id);
    if (this.editingProduct()?.id === product.id) {
      this.resetForm();
    }
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.productPage().size);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
  }

  /**
   * Descarga la receta como PDF con formato bonito
   */
  downloadRecetaPDF() {
    const product = this.editingProduct();
    if (!product || !product.receta) {
      alert("No hay receta disponible para descargar");
      return;
    }

    // Usar el método auxiliar
    this.generateAndOpenRecipePDF(product);
  }

  // Método para descargar receta desde la tarjeta
  downloadRecetaPDFInline(product: ProductSummary) {
    if (!product.receta) {
      alert("Este producto no tiene receta");
      return;
    }

    // Llamar directamente al método de descarga sin modificar el estado
    this.generateAndOpenRecipePDF(product);
  }

  // Método auxiliar para generar y abrir el PDF
  private generateAndOpenRecipePDF(product: ProductSummary) {
    const receta = product.receta;
    if (!receta) return;

    const imageUrl = product.imagePath ? `http://localhost:8081${product.imagePath}` : null;
    const ventana = window.open("", "_blank");
    if (!ventana) {
      alert("Por favor permite ventanas emergentes para descargar el PDF");
      return;
    }

    const htmlContent = `
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${receta.titulo || product.name}</title>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=Lato:wght@400;700&display=swap');

    * { margin: 0; padding: 0; box-sizing: border-box; }

    body {
      font-family: 'Lato', sans-serif;
      background: linear-gradient(135deg, #faf7f4 0%, #f5ebe3 100%);
      padding: 40px;
      color: #2d201a;
      line-height: 1.6;
    }

    .container {
      max-width: 800px;
      margin: 0 auto;
      background: white;
      padding: 50px;
      border-radius: 8px;
      box-shadow: 0 10px 40px rgba(45, 32, 26, 0.1);
    }

    .header {
      text-align: center;
      border-bottom: 3px solid #8a5c46;
      padding-bottom: 30px;
      margin-bottom: 40px;
    }

    .product-image {
      width: 200px;
      height: 200px;
      object-fit: cover;
      border-radius: 8px;
      margin-bottom: 20px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }

    .logo {
      font-size: 14px;
      color: #8a5c46;
      text-transform: uppercase;
      letter-spacing: 3px;
      margin-bottom: 15px;
    }

    h1 {
      font-family: 'Playfair Display', serif;
      font-size: 36px;
      color: #2d201a;
      margin-bottom: 10px;
    }

    .product-code {
      font-size: 12px;
      color: #8a5c46;
      letter-spacing: 2px;
    }

    .section {
      margin-bottom: 35px;
    }

    .section-title {
      font-family: 'Playfair Display', serif;
      font-size: 22px;
      color: #5a3424;
      border-left: 4px solid #8a5c46;
      padding-left: 15px;
      margin-bottom: 15px;
    }

    .content {
      font-size: 15px;
      line-height: 1.8;
      color: #4a3f35;
      white-space: pre-line;
    }

    .content ul {
      list-style: none;
      padding-left: 0;
    }

    .content li {
      padding: 8px 0;
      padding-left: 25px;
      position: relative;
    }

    .content li:before {
      content: "•";
      color: #8a5c46;
      font-weight: bold;
      position: absolute;
      left: 0;
    }

    .observaciones {
      background: #faf7f4;
      padding: 20px;
      border-radius: 6px;
      border-left: 4px solid #c4a77d;
    }

    .footer {
      margin-top: 50px;
      padding-top: 20px;
      border-top: 1px solid #eaded4;
      text-align: center;
      font-size: 12px;
      color: #8a5c46;
    }

    @media print {
      body { background: white; padding: 20px; }
      .container { box-shadow: none; padding: 30px; }
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <div class="logo">Pastelería Artesanal</div>
      ${imageUrl ? `<img src="${imageUrl}" alt="${product.name}" class="product-image" onerror="this.style.display='none'">` : ''}
      <h1>${receta.titulo || product.name}</h1>
      <div class="product-code">${product.code}</div>
    </div>

    <div class="section">
      <h2 class="section-title">${receta.tituloIngredientes || "Ingredientes"}</h2>
      <div class="content">${receta.ingredientes || "No especificados"}</div>
    </div>

    <div class="section">
      <h2 class="section-title">${receta.tituloPasos || "Preparación"}</h2>
      <div class="content">${receta.pasos || "No especificados"}</div>
    </div>

    ${receta.observaciones ? `
    <div class="section observaciones">
      <h2 class="section-title">${receta.tituloObservaciones || "Notas"}</h2>
      <div class="content">${receta.observaciones}</div>
    </div>
    ` : ''}

    <div class="footer">
      Receta interna - Uso exclusivo de la pastelería
    </div>
  </div>

  <script>
    window.onload = function() {
      document.title = "${receta.titulo || product.name}";
    };
  </script>
</body>
</html>
    `;

    ventana.document.write(htmlContent);
    ventana.document.close();
  }

  // Métodos para manejar subida de imágenes
  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      this.selectedFile.set(file);
      
      // Crear preview
      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewImage.set(e.target?.result as string);
      };
      reader.readAsDataURL(file);
      
      // Limpiar el input para permitir seleccionar el mismo archivo nuevamente si es necesario
      input.value = '';
    }
  }

  // Métodos para viñetas y numeración automática en recetas
  onIngredientesKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      event.preventDefault();
      const textarea = event.target as HTMLTextAreaElement;
      const cursorPosition = textarea.selectionStart;
      const value = textarea.value;
      
      // Obtener el texto antes y después del cursor
      const beforeCursor = value.substring(0, cursorPosition);
      const afterCursor = value.substring(cursorPosition);
      
      // Agregar viñeta en la nueva línea
      const newValue = beforeCursor + '\n• ' + afterCursor;
      
      // Actualizar el formulario
      this.form.get('receta.ingredientes')?.setValue(newValue);
      
      // Reposicionar cursor después de la viñeta
      setTimeout(() => {
        textarea.selectionStart = textarea.selectionEnd = cursorPosition + 3;
      });
    }
  }

  onPasosKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      event.preventDefault();
      const textarea = event.target as HTMLTextAreaElement;
      const cursorPosition = textarea.selectionStart;
      const value = textarea.value;
      
      // Obtener el texto antes del cursor
      const beforeCursor = value.substring(0, cursorPosition);
      const afterCursor = value.substring(cursorPosition);
      
      // Contar cuántas líneas hay antes del cursor para saber el número
      const lines = beforeCursor.split('\n');
      const nextNumber = lines.length + 1;
      
      // Agregar numeración en la nueva línea
      const newValue = beforeCursor + '\n' + nextNumber + '. ' + afterCursor;
      
      // Actualizar el formulario
      this.form.get('receta.pasos')?.setValue(newValue);
      
      // Reposicionar cursor después del número
      setTimeout(() => {
        const numberLength = String(nextNumber).length + 2; // número + ". "
        textarea.selectionStart = textarea.selectionEnd = cursorPosition + numberLength;
      });
    }
  }

  onObservacionesKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      event.preventDefault();
      const textarea = event.target as HTMLTextAreaElement;
      const cursorPosition = textarea.selectionStart;
      const value = textarea.value;
      
      // Obtener el texto antes y después del cursor
      const beforeCursor = value.substring(0, cursorPosition);
      const afterCursor = value.substring(cursorPosition);
      
      // Agregar viñeta en la nueva línea
      const newValue = beforeCursor + '\n• ' + afterCursor;
      
      // Actualizar el formulario
      this.form.get('receta.observaciones')?.setValue(newValue);
      
      // Reposicionar cursor después de la viñeta
      setTimeout(() => {
        textarea.selectionStart = textarea.selectionEnd = cursorPosition + 3;
      });
    }
  }

  onTextareaFocus(fieldName: 'ingredientes' | 'pasos' | 'observaciones', prefix: string) {
    const control = this.form.get(`receta.${fieldName}`) as FormControl<string | null>;
    const currentValue = control?.value;

    if (control && !currentValue) {
      // Si está vacío, agregar el prefijo automáticamente
      control.setValue(prefix);

      // Posicionar el cursor después del prefijo en el siguiente ciclo de detección
      // sin hacer focus manual (que interfieren con la interacción del usuario)
      setTimeout(() => {
        const textarea = document.querySelector(`textarea[formControlName="${fieldName}"]`) as HTMLTextAreaElement;
        if (textarea && document.activeElement === textarea) {
          textarea.selectionStart = textarea.selectionEnd = prefix.length;
        }
      }, 0);
    }
  }

  onIngredientesPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedText = event.clipboardData?.getData('text') || '';
    const currentValue = this.form.get('receta.ingredientes')?.value || '';
    
    // Si está vacío, formatear todo con viñetas
    if (!currentValue.trim()) {
      const lines = pastedText.split('\n').filter(line => line.trim());
      const formattedLines = lines.map(line => {
        // Si ya tiene viñeta, no agregar otra
        if (line.trim().startsWith('•')) return line;
        return '• ' + line.trim();
      });
      
      this.form.get('receta.ingredientes')?.setValue(formattedLines.join('\n'));
    } else {
      // Si no está vacío, pegar normal al final
      this.form.get('receta.ingredientes')?.setValue(currentValue + '\n' + pastedText);
    }
  }

  onPasosPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedText = event.clipboardData?.getData('text') || '';
    const currentValue = this.form.get('receta.pasos')?.value || '';
    
    // Si está vacío, formatear todo con numeración
    if (!currentValue.trim()) {
      const lines = pastedText.split('\n').filter(line => line.trim());
      const formattedLines = lines.map((line, index) => {
        const lineNum = index + 1;
        // Si ya tiene numeración, no agregar otra
        if (/^\d+\.\s/.test(line.trim())) return line;
        return `${lineNum}. ` + line.trim();
      });
      
      this.form.get('receta.pasos')?.setValue(formattedLines.join('\n'));
    } else {
      // Si no está vacío, calcular el número siguiente y continuar
      const lines = currentValue.split('\n');
      const lastNumber = lines.length;
      const pastedLines = pastedText.split('\n').filter(line => line.trim());
      const formattedLines = pastedLines.map((line, index) => {
        const lineNum = lastNumber + index + 1;
        if (/^\d+\.\s/.test(line.trim())) return line;
        return `${lineNum}. ` + line.trim();
      });
      
      this.form.get('receta.pasos')?.setValue(currentValue + '\n' + formattedLines.join('\n'));
    }
  }

  onObservacionesPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedText = event.clipboardData?.getData('text') || '';
    const currentValue = this.form.get('receta.observaciones')?.value || '';
    
    // Si está vacío, formatear todo con viñetas
    if (!currentValue.trim()) {
      const lines = pastedText.split('\n').filter(line => line.trim());
      const formattedLines = lines.map(line => {
        // Si ya tiene viñeta, no agregar otra
        if (line.trim().startsWith('•')) return line;
        return '• ' + line.trim();
      });
      
      this.form.get('receta.observaciones')?.setValue(formattedLines.join('\n'));
    } else {
      // Si no está vacío, pegar normal al final
      this.form.get('receta.observaciones')?.setValue(currentValue + '\n' + pastedText);
    }
  }

  triggerFileInput(input: HTMLInputElement) {
    // Prevenir propagación del evento
    input.click();
  }

  clearImageSelection() {
    this.selectedFile.set(null);
    this.previewImage.set(null);
  }

  uploadImage() {
    const file = this.selectedFile();
    const product = this.editingProduct();
    
    if (!file) {
      alert("Selecciona una imagen primero");
      return;
    }

    if (!product) {
      alert("💡 Tip: Primero guarda el producto con 'Registrar producto', luego edítalo para subir la imagen.");
      return;
    }

    // Crear FormData para subir
    const formData = new FormData();
    
    // El backend renombrará al slug del producto
    formData.append('imagen', file);

    // Llamar al backend
    this.facade.uploadProductImage(product.id, formData).subscribe({
      next: (imagePath) => {
        alert(`✅ Imagen subida correctamente.`);
        // Limpiar selección
        this.selectedFile.set(null);
        this.previewImage.set(null);
        // Recargar la lista de productos para mostrar la nueva imagen
        this.facade.loadPage();
      },
      error: (err) => {
        console.error('Error al subir imagen:', err);
        alert("❌ Error al subir la imagen. Inténtalo de nuevo.");
      }
    });
  }

  resolveProductImage(product: ProductSummary): string {
    return buildBackendAssetUrl(product.imagePath);
  }

  pageSummary(): string {
    const page = this.productPage();
    if (!page.totalElements) {
      return "Sin productos para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} productos.`;
  }
}
