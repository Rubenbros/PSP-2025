# Clase 2: Arquitectura por Capas y CRUD completo

## Arquitectura de tres capas

En aplicaciones profesionales, el código se organiza en **capas** con responsabilidades específicas:

```
┌─────────────────────────────────────────────────────────┐
│                      CLIENTE                            │
│               (Navegador, App móvil, Postman)           │
└─────────────────────────┬───────────────────────────────┘
                          │ HTTP
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    CONTROLADOR                          │
│   (@RestController)                                     │
│   - Recibe peticiones HTTP                              │
│   - Valida entrada                                      │
│   - Llama al servicio                                   │
│   - Devuelve respuesta HTTP                             │
└─────────────────────────┬───────────────────────────────┘
                          │ Llamadas a métodos
                          ▼
┌─────────────────────────────────────────────────────────┐
│                      SERVICIO                           │
│   (@Service)                                            │
│   - Contiene la lógica de negocio                       │
│   - Coordina operaciones                                │
│   - No sabe nada de HTTP                                │
└─────────────────────────┬───────────────────────────────┘
                          │ Llamadas a métodos
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    REPOSITORIO                          │
│   (@Repository)                                         │
│   - Accede a la base de datos                           │
│   - CRUD de entidades                                   │
│   - No sabe nada de lógica de negocio                   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
                    [BASE DE DATOS]
```

### ¿Por qué separar en capas?

| Beneficio | Explicación |
|-----------|-------------|
| **Mantenibilidad** | Cambios en una capa no afectan a las demás |
| **Testabilidad** | Puedes probar cada capa por separado |
| **Reutilización** | El servicio puede usarse desde varios controladores |
| **Organización** | Cada clase tiene una única responsabilidad |

---

## Estructura de paquetes

```
com.simpsons.api/
├── controller/          ← Controladores REST
│   └── PersonajeController.java
├── service/             ← Lógica de negocio
│   └── PersonajeService.java
├── repository/          ← Acceso a datos
│   └── PersonajeRepository.java
├── model/               ← Clases de datos (entidades)
│   └── Personaje.java
└── ApiSimpsonsApplication.java
```

---

## El Modelo (Entidad)

La clase que representa los datos:

```java
package com.simpsons.api.model;

public class Personaje {
    private Long id;
    private String nombre;
    private String ocupacion;
    private String fraseCelebre;
    private int edad;

    // Constructor vacío
    public Personaje() {}

    // Constructor completo
    public Personaje(Long id, String nombre, String ocupacion,
                     String fraseCelebre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.ocupacion = ocupacion;
        this.fraseCelebre = fraseCelebre;
        this.edad = edad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getFraseCelebre() { return fraseCelebre; }
    public void setFraseCelebre(String fraseCelebre) { this.fraseCelebre = fraseCelebre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}
```

---

## El Repositorio

Por ahora, usaremos una lista en memoria (en la siguiente clase conectaremos a base de datos real):

```java
package com.simpsons.api.repository;

import com.simpsons.api.model.Personaje;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PersonajeRepository {

    // Lista que simula una base de datos
    private final List<Personaje> personajes = new ArrayList<>();

    // Generador de IDs (thread-safe)
    private final AtomicLong contadorId = new AtomicLong(1);

    // Inicializamos con algunos datos
    public PersonajeRepository() {
        personajes.add(new Personaje(contadorId.getAndIncrement(),
            "Homer Simpson", "Inspector de seguridad", "¡D'oh!", 39));
        personajes.add(new Personaje(contadorId.getAndIncrement(),
            "Marge Simpson", "Ama de casa", "Mmmmm...", 36));
        personajes.add(new Personaje(contadorId.getAndIncrement(),
            "Bart Simpson", "Estudiante", "¡Ay, caramba!", 10));
        personajes.add(new Personaje(contadorId.getAndIncrement(),
            "Lisa Simpson", "Estudiante", "Si alguien quiere encontrarme estaré en mi habitación", 8));
        personajes.add(new Personaje(contadorId.getAndIncrement(),
            "Maggie Simpson", "Bebé", "*chupa chupete*", 1));
    }

    // Obtener todos
    public List<Personaje> findAll() {
        return new ArrayList<>(personajes);
    }

    // Buscar por ID
    public Optional<Personaje> findById(Long id) {
        return personajes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // Guardar (crear o actualizar)
    public Personaje save(Personaje personaje) {
        if (personaje.getId() == null) {
            // Crear nuevo
            personaje.setId(contadorId.getAndIncrement());
            personajes.add(personaje);
        } else {
            // Actualizar existente
            deleteById(personaje.getId());
            personajes.add(personaje);
        }
        return personaje;
    }

    // Eliminar por ID
    public boolean deleteById(Long id) {
        return personajes.removeIf(p -> p.getId().equals(id));
    }

    // Buscar por nombre (contiene)
    public List<Personaje> findByNombreContaining(String nombre) {
        return personajes.stream()
                .filter(p -> p.getNombre().toLowerCase()
                        .contains(nombre.toLowerCase()))
                .toList();
    }
}
```

### ¿Qué es `@Repository`?

Es una anotación que:
1. Marca la clase como un componente de acceso a datos
2. Permite que Spring la detecte y gestione automáticamente
3. Traduce excepciones de base de datos a excepciones de Spring

### ¿Qué es `Optional<T>`?

Es una clase de Java que representa un valor que puede o no existir:

```java
Optional<Personaje> resultado = repository.findById(1L);

if (resultado.isPresent()) {
    Personaje personaje = resultado.get();
    // Hacer algo con el personaje
} else {
    // No se encontró
}

// O más elegante:
resultado.ifPresent(personaje -> System.out.println(personaje.getNombre()));

// O con valor por defecto:
Personaje personaje = resultado.orElse(new Personaje());

// O lanzar excepción si no existe:
Personaje personaje = resultado.orElseThrow(() ->
    new RuntimeException("Personaje no encontrado"));
```

---

## El Servicio

Contiene la lógica de negocio:

```java
package com.simpsons.api.service;

import com.simpsons.api.model.Personaje;
import com.simpsons.api.repository.PersonajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonajeService {

    private final PersonajeRepository repository;

    // Inyección de dependencias por constructor
    public PersonajeService(PersonajeRepository repository) {
        this.repository = repository;
    }

    public List<Personaje> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Personaje> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Personaje crear(Personaje personaje) {
        // Aquí podríamos añadir validaciones de negocio
        // Por ejemplo: verificar que no exista otro con el mismo nombre
        return repository.save(personaje);
    }

    public Optional<Personaje> actualizar(Long id, Personaje datosNuevos) {
        return repository.findById(id)
                .map(personajeExistente -> {
                    personajeExistente.setNombre(datosNuevos.getNombre());
                    personajeExistente.setOcupacion(datosNuevos.getOcupacion());
                    personajeExistente.setFraseCelebre(datosNuevos.getFraseCelebre());
                    personajeExistente.setEdad(datosNuevos.getEdad());
                    return repository.save(personajeExistente);
                });
    }

    public boolean eliminar(Long id) {
        return repository.deleteById(id);
    }

    public List<Personaje> buscarPorNombre(String nombre) {
        return repository.findByNombreContaining(nombre);
    }
}
```

### ¿Qué es `@Service`?

Similar a `@Repository`, marca la clase como un servicio de negocio que Spring gestiona automáticamente.

### Inyección de Dependencias

En lugar de crear objetos con `new`, Spring los "inyecta" automáticamente:

```java
// ❌ MAL - Creamos la dependencia nosotros
public class PersonajeService {
    private PersonajeRepository repository = new PersonajeRepository();
}

// ✅ BIEN - Spring inyecta la dependencia
public class PersonajeService {
    private final PersonajeRepository repository;

    public PersonajeService(PersonajeRepository repository) {
        this.repository = repository;
    }
}
```

**Ventajas:**
- Facilita el testing (puedes inyectar mocks)
- Desacopla las clases
- Spring gestiona el ciclo de vida de los objetos

---

## El Controlador (CRUD completo)

```java
package com.simpsons.api.controller;

import com.simpsons.api.model.Personaje;
import com.simpsons.api.service.PersonajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personajes")  // Prefijo para todos los endpoints
public class PersonajeController {

    private final PersonajeService service;

    public PersonajeController(PersonajeService service) {
        this.service = service;
    }

    // GET /api/personajes
    @GetMapping
    public List<Personaje> obtenerTodos() {
        return service.obtenerTodos();
    }

    // GET /api/personajes/1
    @GetMapping("/{id}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)                    // 200 OK si existe
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/personajes
    @PostMapping
    public ResponseEntity<Personaje> crear(@RequestBody Personaje personaje) {
        Personaje nuevo = service.crear(personaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201 Created
    }

    // PUT /api/personajes/1
    @PutMapping("/{id}")
    public ResponseEntity<Personaje> actualizar(
            @PathVariable Long id,
            @RequestBody Personaje personaje) {
        return service.actualizar(id, personaje)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/personajes/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();      // 404 Not Found
    }

    // GET /api/personajes/buscar?nombre=Homer
    @GetMapping("/buscar")
    public List<Personaje> buscar(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }
}
```

### Nuevas anotaciones

| Anotación | Uso |
|-----------|-----|
| `@RequestMapping("/api/personajes")` | Prefijo de URL para toda la clase |
| `@PostMapping` | Maneja peticiones POST |
| `@PutMapping` | Maneja peticiones PUT |
| `@DeleteMapping` | Maneja peticiones DELETE |
| `@RequestBody` | El parámetro viene en el cuerpo de la petición (JSON) |

### ResponseEntity

`ResponseEntity<T>` permite controlar:
- El código de estado HTTP
- Las cabeceras de respuesta
- El cuerpo de respuesta

```java
// Devolver 200 OK con datos
ResponseEntity.ok(personaje);

// Devolver 201 Created con datos
ResponseEntity.status(HttpStatus.CREATED).body(personaje);

// Devolver 404 Not Found sin cuerpo
ResponseEntity.notFound().build();

// Devolver 204 No Content (éxito sin datos)
ResponseEntity.noContent().build();

// Devolver 400 Bad Request
ResponseEntity.badRequest().body("Error: nombre requerido");
```

---

## Probando la API con Postman

### 1. GET todos los personajes
- **Método**: GET
- **URL**: `http://localhost:8080/api/personajes`

### 2. GET un personaje
- **Método**: GET
- **URL**: `http://localhost:8080/api/personajes/1`

### 3. POST crear personaje
- **Método**: POST
- **URL**: `http://localhost:8080/api/personajes`
- **Body** (raw JSON):
```json
{
    "nombre": "Ned Flanders",
    "ocupacion": "Propietario del Leftorium",
    "fraseCelebre": "¡Hola holita,

!",
    "edad": 60
}
```

### 4. PUT actualizar personaje
- **Método**: PUT
- **URL**: `http://localhost:8080/api/personajes/1`
- **Body** (raw JSON):
```json
{
    "nombre": "Homer J. Simpson",
    "ocupacion": "Inspector de seguridad senior",
    "fraseCelebre": "¡D'oh! ¡Mmm, rosquillas!",
    "edad": 40
}
```

### 5. DELETE eliminar personaje
- **Método**: DELETE
- **URL**: `http://localhost:8080/api/personajes/1`

---

## Probando con el HTTP Client de IntelliJ

Crea un archivo `requests.http` en tu proyecto:

```http
### Obtener todos los personajes
GET http://localhost:8080/api/personajes

### Obtener personaje por ID
GET http://localhost:8080/api/personajes/1

### Crear nuevo personaje
POST http://localhost:8080/api/personajes
Content-Type: application/json

{
    "nombre": "Ned Flanders",
    "ocupacion": "Propietario del Leftorium",
    "fraseCelebre": "¡Hola holita, vecinito!",
    "edad": 60
}

### Actualizar personaje
PUT http://localhost:8080/api/personajes/1
Content-Type: application/json

{
    "nombre": "Homer J. Simpson",
    "ocupacion": "Inspector de seguridad senior",
    "fraseCelebre": "¡D'oh!",
    "edad": 40
}

### Eliminar personaje
DELETE http://localhost:8080/api/personajes/1

### Buscar por nombre
GET http://localhost:8080/api/personajes/buscar?nombre=Simpson
```

---

## Resumen CRUD

| Operación | Método HTTP | URL | Código éxito |
|-----------|-------------|-----|--------------|
| Listar todos | GET | /api/personajes | 200 OK |
| Obtener uno | GET | /api/personajes/{id} | 200 OK |
| Crear | POST | /api/personajes | 201 Created |
| Actualizar | PUT | /api/personajes/{id} | 200 OK |
| Eliminar | DELETE | /api/personajes/{id} | 204 No Content |

---

## Siguiente clase

Conectaremos la API a una base de datos real usando Spring Data JPA.
