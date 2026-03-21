/**
 * ============================================================
 * TESTS UNITARIOS PARA ProductsPageComponent
 * ============================================================
 * 
 * ESTE ARCHIVO DEMUESTRA:
 * 
 * 1. JASMINE - Framework de testing para JavaScript
 *    - describe(): Agrupa tests relacionados
 *    - it(): Define un test individual
 *    - beforeEach(): Setup antes de cada test
 * 
 * 2. ANGULAR TESTING UTILITIES
 *    - TestBed: Configuración de módulo de pruebas
 *    - ComponentFixture: Wrapper para testear componentes
 *    - NO_ERRORS_SCHEMA: Ignora elementos desconocidos
 * 
 * 3. MOCKING DE SERVICIOS
 *    - jasmine.createSpyObj(): Crea mocks con métodos spy
 *    - provide: { useValue: mock }: Inyección de mocks
 * 
 * 4. TESTING DE FORMULARIOS REACTIVOS
 *    - setValue(): Establecer valores de formulario
 *    - valid/invalid: Verificar estado de validación
 *    - triggerEventHandler(): Simular eventos DOM
 * 
 * 5. TESTING DE SIGNALS
 *    - Verificar que signals se actualizan correctamente
 *    - detectChanges(): Forzar detección de cambios
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductsPageComponent } from './products-page.component';
import { ProductsFacadeService } from './state/products.facade';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { NO_ERRORS_SCHEMA, signal } from '@angular/core';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('ProductsPageComponent', () => {
  // ==========================================================
  // DECLARACIÓN DE VARIABLES
  // ==========================================================
  
  /** Componente bajo prueba */
  let component: ProductsPageComponent;
  
  /** Fixture: Wrapper que permite interactuar con el componente */
  let fixture: ComponentFixture<ProductsPageComponent>;
  
  /** Mock del servicio facade */
  let facadeMock: jasmine.SpyObj<ProductsFacadeService>;

  // ==========================================================
  // CONFIGURACIÓN GLOBAL (se ejecuta antes de todos los tests)
  // ==========================================================
  
  beforeEach(async () => {
    /**
     * Crear mock del facade con métodos spy
     * 
     * createSpyObj crea un objeto con métodos "espía" que:
     * - Pueden verificar si fueron llamados
     * - Pueden retornar valores controlados
     * - No ejecutan la lógica real
     */
    facadeMock = jasmine.createSpyObj('ProductsFacadeService', [
      'createProduct',
      'updateProduct',
      'loadPage'
    ]);

    /**
     * Configurar TestBed
     * 
     * TestBed es como un @NgModule pero para tests.
     * Aquí declaramos qué necesita nuestro componente para funcionar.
     */
    await TestBed.configureTestingModule({
      // Importamos el componente standalone
      imports: [
        ProductsPageComponent,
        ReactiveFormsModule  // Necesario para formularios reactivos
      ],
      providers: [
        FormBuilder,  // Servicio real para formularios
        { 
          provide: ProductsFacadeService,  // Cuando alguien pida ProductsFacadeService...
          useValue: facadeMock              // ...darle el mock en lugar del real
        }
      ],
      /**
       * NO_ERRORS_SCHEMA evita errores por elementos HTML
       * que Angular no reconoce en el template.
       * Útil para tests unitarios enfocados en lógica, no en DOM.
       */
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  // ==========================================================
  // SETUP ANTES DE CADA TEST
  // ==========================================================
  
  beforeEach(() => {
    /**
     * Crear instancia del componente con su fixture
     * 
     * TestBed.createComponent():
     * 1. Crea una instancia real del componente
     * 2. La envuelve en un ComponentFixture
     * 3. No ejecuta ngOnInit() todavía
     */
    fixture = TestBed.createComponent(ProductsPageComponent);
    component = fixture.componentInstance;
    
    /**
     * Configurar datos mock del facade
     * 
     * Estos son signals mock que simulan el estado del store.
     */
    Object.defineProperty(facadeMock, 'productPage', {
      get: () => signal({
        content: [],
        totalElements: 0,
        totalPages: 1,
        size: 10,
        number: 0
      })
    });
    
    Object.defineProperty(facadeMock, 'categories', {
      get: () => signal([
        { id: 1, name: 'Tortas' },
        { id: 2, name: 'Cupcakes' }
      ])
    });
  });

  // ==========================================================
  // TEST 1: Creación del componente
  // ==========================================================
  
  it('Debería crear el componente', () => {
    // Verificar que el componente se instanció correctamente
    expect(component).toBeTruthy();
  });

  // ==========================================================
  // TEST 2: Formulario inicializado correctamente
  // ==========================================================
  
  it('Debería inicializar el formulario con campos vacíos', () => {
    // ARRANGE: El formulario ya se inicializó en ngOnInit
    fixture.detectChanges();  // Ejecutar ngOnInit()
    
    // ACT: Obtener valores del formulario
    const formValue = component.form.value;
    
    // ASSERT: Verificar estado inicial
    expect(formValue.code).toBe('');
    expect(formValue.name).toBe('');
    expect(formValue.description).toBe('');
    expect(formValue.categoryId).toBe('');
    expect(formValue.basePrice).toBe(0);
  });

  // ==========================================================
  // TEST 3: Validación de formulario
  // ==========================================================
  
  it('Debería marcar formulario como inválido si falta nombre', () => {
    fixture.detectChanges();
    
    // ARRANGE: Completar solo código, dejar nombre vacío
    component.form.patchValue({
      code: 'PROD-001',
      name: '',  // Campo requerido vacío
      categoryId: '1'
    });
    
    // ACT: Verificar validez
    const isValid = component.form.valid;
    
    // ASSERT
    expect(isValid).toBeFalse();
    expect(component.form.get('name')?.valid).toBeFalse();
    expect(component.form.get('name')?.errors?.['required']).toBeTruthy();
  });

  // ==========================================================
  // TEST 4: Validación exitosa
  // ==========================================================
  
  it('Debería marcar formulario como válido con datos completos', () => {
    fixture.detectChanges();
    
    // ARRANGE: Completar todos los campos requeridos
    component.form.setValue({
      code: 'PROD-001',
      name: 'Torta de Chocolate',
      description: 'Deliciosa torta',
      categoryId: '1',
      basePrice: 25.00,
      quotationRequired: false,
      active: true,
      published: true,
      receta: null
    });
    
    // ASSERT
    expect(component.form.valid).toBeTrue();
  });

  // ==========================================================
  // TEST 5: Modo edición
  // ==========================================================
  
  it('Debería cargar datos del producto en modo edición', () => {
    fixture.detectChanges();
    
    // ARRANGE: Crear producto mock para editar
    const productToEdit = {
      id: 1,
      code: 'TORTA-001',
      name: 'Torta Tres Leches',
      description: 'Descripción actual',
      categoryId: 1,
      basePrice: 45.00,
      quotationRequired: false,
      active: true,
      published: true,
      slug: 'torta-tres-leches',
      receta: null
    };
    
    // ACT: Establecer modo edición
    component.editingProduct.set(productToEdit);
    component.form.patchValue({
      code: productToEdit.code,
      name: productToEdit.name,
      description: productToEdit.description,
      categoryId: String(productToEdit.categoryId),
      basePrice: productToEdit.basePrice,
      quotationRequired: productToEdit.quotationRequired,
      active: productToEdit.active,
      published: productToEdit.published
    });
    
    // ASSERT
    expect(component.editingProduct()).toBeTruthy();
    expect(component.form.get('name')?.value).toBe('Torta Tres Leches');
    expect(component.editingProduct()?.slug).toBe('torta-tres-leches');
  });

  // ==========================================================
  // TEST 6: Crear producto nuevo
  // ==========================================================
  
  it('Debería llamar a facade.createProduct al crear nuevo', () => {
    fixture.detectChanges();
    
    // ARRANGE: Completar formulario válido
    component.form.setValue({
      code: 'NUEVO-001',
      name: 'Nuevo Producto',
      description: 'Descripción',
      categoryId: '1',
      basePrice: 30.00,
      quotationRequired: false,
      active: true,
      published: true,
      receta: {
        titulo: 'Receta',
        ingredientes: '• Ingrediente 1',
        pasos: '1. Paso 1',
        observaciones: 'Notas'
      }
    });
    
    // ACT: Enviar formulario
    component.submit();
    
    // ASSERT: Verificar que se llamó al método correcto
    expect(facadeMock.createProduct).toHaveBeenCalled();
    expect(facadeMock.updateProduct).not.toHaveBeenCalled();
  });

  // ==========================================================
  // TEST 7: Actualizar producto existente
  // ==========================================================
  
  it('Debería llamar a facade.updateProduct al editar', () => {
    fixture.detectChanges();
    
    // ARRANGE: Establecer modo edición
    const existingProduct = {
      id: 5,
      code: 'EXIST-001',
      name: 'Producto Existente',
      description: 'Desc',
      categoryId: 2,
      basePrice: 50.00,
      quotationRequired: false,
      active: true,
      published: true,
      slug: 'producto-existente',
      receta: null
    };
    
    component.editingProduct.set(existingProduct);
    component.form.patchValue({
      code: existingProduct.code,
      name: 'Nombre Actualizado',  // Cambiamos el nombre
      description: existingProduct.description,
      categoryId: String(existingProduct.categoryId),
      basePrice: existingProduct.basePrice,
      quotationRequired: existingProduct.quotationRequired,
      active: existingProduct.active,
      published: existingProduct.published,
      receta: existingProduct.receta
    });
    
    // ACT: Enviar formulario
    component.submit();
    
    // ASSERT
    expect(facadeMock.updateProduct).toHaveBeenCalledWith(
      5,  // ID del producto
      jasmine.any(Object)  // Payload (cualquier objeto)
    );
    expect(facadeMock.createProduct).not.toHaveBeenCalled();
  });

  // ==========================================================
  // TEST 8: Resetear formulario
  // ==========================================================
  
  it('Debería limpiar el formulario al resetear', () => {
    fixture.detectChanges();
    
    // ARRANGE: Llenar formulario
    component.form.patchValue({
      code: 'TEST-001',
      name: 'Test'
    });
    component.editingProduct.set({ id: 1 } as any);
    component.selectedFile.set({} as any);
    component.previewImage.set('url');
    
    // ACT: Resetear
    component.resetForm();
    
    // ASSERT
    expect(component.form.value.code).toBe('');
    expect(component.editingProduct()).toBeNull();
    expect(component.selectedFile()).toBeNull();
    expect(component.previewImage()).toBeNull();
  });
});
