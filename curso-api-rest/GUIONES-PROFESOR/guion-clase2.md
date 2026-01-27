# GUIÓN CLASE 2 - Para el profesor
## Arquitectura por Capas y CRUD completo

**Duración total:** 2 horas

---

## Preparación antes de clase

### Verificar
- [ ] Proyecto de la clase anterior funcionando
- [ ] Postman o cliente HTTP preparado
- [ ] Diagramas de arquitectura listos (puedes dibujarlos en pizarra)

---

## Estructura de la clase

| Tiempo | Bloque | Actividad |
|--------|--------|-----------|
| 0:00-0:20 | Teoría | Arquitectura de 3 capas |
| 0:20-0:50 | Código en vivo | Crear el Modelo y Repositorio |
| 0:50-1:10 | Código en vivo | Crear el Servicio |
| 1:10-1:35 | Código en vivo | Crear el Controlador CRUD |
| 1:35-1:55 | Práctica | Alumnos implementan ejercicios |
| 1:55-2:00 | Cierre | Entrega y resolución dudas |

---

## BLOQUE 1: Teoría de arquitectura (20 min)

### Dibuja en la pizarra

```
                    ┌──────────────┐
    Petición HTTP → │ CONTROLADOR  │ ← ResponseEntity, códigos HTTP
                    └──────┬───────┘
                           │ llama a
                    ┌──────▼───────┐
                    │   SERVICIO   │ ← Lógica de negocio
                    └──────┬───────┘
                           │ llama a
                    ┌──────▼───────┐
                    │ REPOSITORIO  │ ← Acceso a datos
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
                    │ BASE DATOS   │
                    └──────────────┘
```

### Pregunta al aire
"¿Por qué crees que separamos el código en capas?"

**Respuestas esperadas:**
- Organización
- Cada clase tiene una responsabilidad
- Más fácil de mantener
- Más fácil de testear

### Analogía del restaurante (conecta con clase anterior)
- Controlador = Camarero (recibe pedidos, entrega platos)
- Servicio = Cocinero (prepara la comida, conoce las recetas)
- Repositorio = Despensa (guarda y recupera ingredientes)

### Estructura de paquetes
Muestra cómo organizarán el código:
```
com.springfield.api/
├── controller/
├── service/
├── repository/
└── model/
```

---

## BLOQUE 2: Modelo y Repositorio (30 min)

### Crear el modelo Comercio

Escribe paso a paso, explicando cada campo:

```java
package com.springfield.api.model;

public class Comercio {
    private Long id;           // Identificador único
    private String nombre;     // Ej: "Kwik-E-Mart"
    private String propietario; // Ej: "Apu"
    private String tipo;       // supermercado, bar, restaurante
    private String direccion;
    private boolean abierto;   // ¿Está abierto ahora?

    // Constructor vacío (Spring lo necesita)
    public Comercio() {}

    // Constructor completo
    // Getters y Setters
}
```

**Truco:** En IntelliJ, escribe los campos y luego:
- Alt+Insert → Constructor
- Alt+Insert → Getter and Setter

### Crear el repositorio

```java
package com.springfield.api.repository;

@Repository  // "Le dice a Spring: gestiona esta clase"
public class ComercioRepository {

    // Lista que simula la base de datos
    private final List<Comercio> comercios = new ArrayList<>();

    // Generador de IDs (thread-safe, conecta con primer trimestre)
    private final AtomicLong contadorId = new AtomicLong(1);

    // Constructor con datos iniciales
    public ComercioRepository() {
        comercios.add(new Comercio(contadorId.getAndIncrement(),
            "Kwik-E-Mart", "Apu", "supermercado", "123 Main St", true));
        // ... más comercios
    }
}
```

**Punto importante:** Explica `AtomicLong`
- "¿Os acordáis del problema de concurrencia?"
- "Dos hilos incrementando un contador pueden dar problemas"
- "AtomicLong lo hace de forma segura"

### Implementar métodos del repositorio

```java
public List<Comercio> findAll() {
    return new ArrayList<>(comercios);  // Copia para evitar modificaciones
}

public Optional<Comercio> findById(Long id) {
    return comercios.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();
}
```

**Explica Optional:**
- "¿Qué pasa si buscamos un ID que no existe?"
- "Antes devolvíamos null y rezábamos"
- "Optional nos obliga a manejar el caso de 'no encontrado'"

---

## BLOQUE 3: El Servicio (20 min)

```java
package com.springfield.api.service;

@Service
public class ComercioService {

    private final ComercioRepository repository;

    // Inyección de dependencias por constructor
    public ComercioService(ComercioRepository repository) {
        this.repository = repository;
    }

    public List<Comercio> obtenerTodos() {
        return repository.findAll();
    }

    // ... resto de métodos
}
```

### Explica la inyección de dependencias

**Pregunta:** "¿Por qué no hacemos `new ComercioRepository()`?"

Dibuja:
```
SIN inyección:                    CON inyección:
┌─────────────────┐               ┌─────────────────┐
│    Servicio     │               │    Servicio     │
│ ┌─────────────┐ │               │                 │
│ │ Repository  │ │               │   repository ───┼──┐
│ │   (new)     │ │               │                 │  │
│ └─────────────┘ │               └─────────────────┘  │
└─────────────────┘                                    │
   Difícil de testear              ┌─────────────────┐ │
   Acoplado                        │   Repository    │◄┘
                                   │ (Spring crea)   │
                                   └─────────────────┘
                                      Fácil de testear
                                      Desacoplado
```

---

## BLOQUE 4: El Controlador CRUD (25 min)

```java
package com.springfield.api.controller;

@RestController
@RequestMapping("/api/comercios")  // Prefijo para todos los endpoints
public class ComercioController {

    private final ComercioService service;

    public ComercioController(ComercioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comercio> listarTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comercio> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
```

### Explica ResponseEntity

"Hasta ahora devolvíamos directamente el objeto. Pero..."
- ¿Qué código HTTP devuelve si todo va bien? (200 OK)
- ¿Y si el recurso no existe? (404 Not Found)
- ¿Y si creamos algo nuevo? (201 Created)

```java
// Formas de usar ResponseEntity
ResponseEntity.ok(comercio);                    // 200 + cuerpo
ResponseEntity.notFound().build();              // 404 sin cuerpo
ResponseEntity.status(HttpStatus.CREATED).body(nuevo);  // 201 + cuerpo
ResponseEntity.noContent().build();             // 204 sin cuerpo
```

### POST - Crear

```java
@PostMapping
public ResponseEntity<Comercio> crear(@RequestBody Comercio comercio) {
    Comercio nuevo = service.crear(comercio);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
}
```

**Explica @RequestBody:**
- "El cliente envía JSON en el cuerpo de la petición"
- "Spring lo convierte automáticamente a un objeto Java"

### PUT - Actualizar

```java
@PutMapping("/{id}")
public ResponseEntity<Comercio> actualizar(
        @PathVariable Long id,
        @RequestBody Comercio comercio) {
    return service.actualizar(id, comercio)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

### DELETE - Eliminar

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    if (service.eliminar(id)) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}
```

### Probar con Postman/HTTP Client

Demuestra cada operación:
1. GET /api/comercios → Lista
2. GET /api/comercios/1 → Un comercio
3. GET /api/comercios/999 → 404
4. POST con JSON → 201
5. DELETE /api/comercios/1 → 204

---

## BLOQUE 5: Práctica guiada (20 min)

### Que implementen los ejercicios 2.1, 2.2, 2.3

**Pasa por las mesas** mientras trabajan.

### Errores típicos a vigilar

| Error | Causa | Solución |
|-------|-------|----------|
| "No such bean" | Falta @Service o @Repository | Añadir anotación |
| Lista vacía | No inicializaron datos | Añadir en constructor |
| 404 en todo | URL incorrecta o falta @RequestMapping | Verificar rutas |
| JSON vacío | Faltan getters | Alt+Insert → Getters |
| Error de compilación | Imports faltantes | Alt+Enter en cada error |

---

## BLOQUE 6: Cierre (5 min)

### Recogida de ejercicios
- Que te muestren los endpoints funcionando
- Verificar que tienen el CRUD completo

### Avance de la próxima clase
"La próxima clase conectaremos esto a una base de datos real con JPA. Ya no perderemos los datos al reiniciar."

---

## Notas para el profesor

### Si van muy rápido
- Que implementen el ejercicio de estadísticas (2.3)
- Que añadan más endpoints de filtrado

### Si van muy lentos
- Prioriza: GET, POST, y uno de los dos (PUT o DELETE)
- Los filtros pueden quedar para casa

### Conceptos que deben quedar claros
1. Separación en capas (cada una su responsabilidad)
2. @RestController, @Service, @Repository
3. Inyección de dependencias
4. ResponseEntity y códigos HTTP
5. @RequestBody para recibir JSON
