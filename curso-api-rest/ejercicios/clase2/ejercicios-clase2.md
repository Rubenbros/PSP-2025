# Ejercicio Clase 2: CRUD de Comercios de Springfield

**Puntuación:** 0.5 puntos
**Tiempo:** 20-25 minutos

---

## Objetivo

Crear una API CRUD completa con arquitectura de 3 capas (Controller → Service → Repository).

---

## Estructura de paquetes

```
com.springfield.comercios/
├── controller/
│   └── ComercioController.java
├── service/
│   └── ComercioService.java
├── repository/
│   └── ComercioRepository.java
├── model/
│   └── Comercio.java
└── ComerciosApplication.java
```

---

## Pasos

### 1. Modelo (3 min)

```java
public class Comercio {
    private Long id;
    private String nombre;
    private String propietario;
    private String tipo;  // bar, supermercado, restaurante
    private boolean abierto;

    // Constructores, getters, setters
}
```

### 2. Repositorio (5 min)

```java
@Repository
public class ComercioRepository {

    private List<Comercio> comercios = new ArrayList<>();
    private AtomicLong contador = new AtomicLong(1);

    // Constructor: inicializa con 3-4 comercios de Springfield
    // Kwik-E-Mart (Apu), Taberna de Moe, Krusty Burger...

    public List<Comercio> findAll() { }
    public Optional<Comercio> findById(Long id) { }
    public Comercio save(Comercio comercio) { }
    public boolean deleteById(Long id) { }
}
```

### 3. Servicio (5 min)

```java
@Service
public class ComercioService {

    private final ComercioRepository repository;

    // Constructor con inyección de dependencias

    // Métodos que llaman al repositorio
}
```

### 4. Controlador (10 min)

```java
@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private final ComercioService service;

    // Constructor con inyección de dependencias

    // GET /api/comercios → 200 OK + lista

    // GET /api/comercios/{id} → 200 OK o 404 Not Found

    // POST /api/comercios → 201 Created + comercio creado
    // (usar @RequestBody para recibir JSON)

    // PUT /api/comercios/{id} → 200 OK o 404 Not Found

    // DELETE /api/comercios/{id} → 204 No Content o 404 Not Found
}
```

---

## Entrega

Demuestra al profesor usando Postman o el cliente HTTP de IntelliJ:

- [ ] `GET /api/comercios` → Lista los comercios
- [ ] `GET /api/comercios/1` → Devuelve un comercio
- [ ] `GET /api/comercios/99` → Devuelve 404
- [ ] `POST /api/comercios` con JSON → Devuelve 201 + comercio creado
- [ ] `DELETE /api/comercios/1` → Devuelve 204

**JSON de ejemplo para POST:**
```json
{
    "nombre": "Leftorium",
    "propietario": "Ned Flanders",
    "tipo": "tienda",
    "abierto": true
}
```
