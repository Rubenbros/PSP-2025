# Clase 4: Consumo de APIs externas y Concurrencia

## Consumir APIs externas

Hasta ahora hemos creado APIs. Ahora aprenderemos a **consumir** APIs de terceros.

### RestTemplate vs WebClient

| RestTemplate | WebClient |
|--------------|-----------|
| Síncrono (bloquea el hilo) | Asíncrono (no bloquea) |
| Más sencillo | Más potente |
| Será deprecado | Es el futuro |
| Ideal para aprender | Ideal para producción |

Usaremos **RestTemplate** por simplicidad, pero mencionaremos WebClient.

---

## Configurar RestTemplate

### 1. Crear un Bean de configuración

```java
package com.simpsons.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### ¿Qué es un Bean?

Un **Bean** es un objeto gestionado por Spring. Al anotarlo con `@Bean`, Spring:
1. Crea una única instancia
2. La hace disponible para inyección en cualquier parte

---

## Consumir The Simpsons Quote API

URL: `https://thesimpsonsquoteapi.glitch.me/quotes`

### 1. Crear modelo para la respuesta

```java
package com.simpsons.api.model;

public class SimpsonsQuote {
    private String quote;
    private String character;
    private String image;
    private String characterDirection;

    // Constructor vacío
    public SimpsonsQuote() {}

    // Getters y Setters
    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCharacterDirection() { return characterDirection; }
    public void setCharacterDirection(String characterDirection) {
        this.characterDirection = characterDirection;
    }
}
```

### 2. Crear servicio para consumir la API

```java
package com.simpsons.api.service;

import com.simpsons.api.model.SimpsonsQuote;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SimpsonsQuoteService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://thesimpsonsquoteapi.glitch.me/quotes";

    public SimpsonsQuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Obtener una frase aleatoria
    public SimpsonsQuote obtenerFraseAleatoria() {
        SimpsonsQuote[] response = restTemplate.getForObject(API_URL, SimpsonsQuote[].class);
        return response != null && response.length > 0 ? response[0] : null;
    }

    // Obtener varias frases
    public List<SimpsonsQuote> obtenerFrases(int cantidad) {
        String url = API_URL + "?count=" + cantidad;
        SimpsonsQuote[] response = restTemplate.getForObject(url, SimpsonsQuote[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }

    // Obtener frase de un personaje específico
    public SimpsonsQuote obtenerFraseDe(String personaje) {
        String url = API_URL + "?character=" + personaje.toLowerCase();
        SimpsonsQuote[] response = restTemplate.getForObject(url, SimpsonsQuote[].class);
        return response != null && response.length > 0 ? response[0] : null;
    }
}
```

### 3. Crear controlador

```java
package com.simpsons.api.controller;

import com.simpsons.api.model.SimpsonsQuote;
import com.simpsons.api.service.SimpsonsQuoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class SimpsonsQuoteController {

    private final SimpsonsQuoteService quoteService;

    public SimpsonsQuoteController(SimpsonsQuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public SimpsonsQuote obtenerFraseAleatoria() {
        return quoteService.obtenerFraseAleatoria();
    }

    @GetMapping("/multiple")
    public List<SimpsonsQuote> obtenerVarias(
            @RequestParam(defaultValue = "5") int count) {
        return quoteService.obtenerFrases(count);
    }

    @GetMapping("/character/{nombre}")
    public SimpsonsQuote obtenerDe(@PathVariable String nombre) {
        return quoteService.obtenerFraseDe(nombre);
    }
}
```

---

## Manejo de errores en llamadas externas

Las APIs externas pueden fallar. Debemos manejarlo:

```java
@Service
public class SimpsonsQuoteService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://thesimpsonsquoteapi.glitch.me/quotes";

    public SimpsonsQuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SimpsonsQuote obtenerFraseAleatoria() {
        try {
            SimpsonsQuote[] response = restTemplate.getForObject(API_URL, SimpsonsQuote[].class);
            return response != null && response.length > 0 ? response[0] : null;
        } catch (Exception e) {
            // Log del error
            System.err.println("Error al llamar a la API de Simpsons: " + e.getMessage());

            // Devolver un valor por defecto o lanzar excepción personalizada
            SimpsonsQuote fallback = new SimpsonsQuote();
            fallback.setQuote("¡D'oh! La API no responde");
            fallback.setCharacter("Homer Simpson");
            return fallback;
        }
    }
}
```

---

## Concurrencia en APIs REST

### ¿Por qué importa la concurrencia?

Un servidor web recibe **múltiples peticiones simultáneas**. Cada petición se ejecuta en un **hilo diferente**.

```
Cliente 1 ─────► [Hilo 1] ──┐
                            │
Cliente 2 ─────► [Hilo 2] ──┼──► Recurso compartido (BD, Lista, etc.)
                            │
Cliente 3 ─────► [Hilo 3] ──┘
```

### Problema: Condiciones de carrera

Si dos hilos modifican el mismo dato a la vez, pueden ocurrir errores.

**Ejemplo**: Sistema de reservas con plazas limitadas

```java
// ❌ PROBLEMA: Race condition
@Service
public class ReservaService {
    private int plazasDisponibles = 10;

    public boolean reservar() {
        if (plazasDisponibles > 0) {
            // Entre esta comprobación y la resta, otro hilo podría entrar
            plazasDisponibles--;  // ← Aquí puede haber problemas
            return true;
        }
        return false;
    }
}
```

**Escenario problemático:**
1. Hilo A lee `plazasDisponibles = 1`
2. Hilo B lee `plazasDisponibles = 1`
3. Hilo A ve que hay plaza, resta: `plazasDisponibles = 0`
4. Hilo B ve que hay plaza (leyó antes), resta: `plazasDisponibles = -1` ← ¡ERROR!

---

## Soluciones a la concurrencia

### 1. Synchronized (lo que ya conocéis)

```java
@Service
public class ReservaService {
    private int plazasDisponibles = 10;

    public synchronized boolean reservar() {
        if (plazasDisponibles > 0) {
            plazasDisponibles--;
            return true;
        }
        return false;
    }

    public synchronized int getPlazasDisponibles() {
        return plazasDisponibles;
    }
}
```

### 2. AtomicInteger (para contadores)

```java
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReservaService {
    private final AtomicInteger plazasDisponibles = new AtomicInteger(10);

    public boolean reservar() {
        // decrementAndGet es atómico (thread-safe)
        int restantes = plazasDisponibles.decrementAndGet();
        if (restantes >= 0) {
            return true;
        } else {
            // Revertir si no había plaza
            plazasDisponibles.incrementAndGet();
            return false;
        }
    }

    // Versión más elegante
    public boolean reservarV2() {
        return plazasDisponibles.updateAndGet(current ->
            current > 0 ? current - 1 : current
        ) >= 0;
    }
}
```

### 3. Locks de la base de datos

Con JPA, la propia base de datos gestiona la concurrencia:

```java
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int plazasDisponibles;

    @Version  // Optimistic locking
    private Long version;
}
```

`@Version` implementa **bloqueo optimista**:
- Cada actualización incrementa la versión
- Si dos hilos intentan actualizar la misma versión, uno falla
- Spring lanza `OptimisticLockingFailureException`

---

## Ejemplo práctico: Sistema de votaciones

Vamos a crear un sistema donde los usuarios votan por su personaje favorito de Los Simpson:

### Modelo

```java
@Entity
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personaje;
    private int cantidad;

    @Version
    private Long version;

    // Constructor, getters, setters...
}
```

### Repositorio

```java
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByPersonaje(String personaje);
}
```

### Servicio con manejo de concurrencia

```java
@Service
public class VotacionService {

    private final VotoRepository repository;

    public VotacionService(VotoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Voto votar(String personaje) {
        Voto voto = repository.findByPersonaje(personaje)
                .orElseGet(() -> {
                    Voto nuevo = new Voto();
                    nuevo.setPersonaje(personaje);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        voto.setCantidad(voto.getCantidad() + 1);
        return repository.save(voto);
    }

    public List<Voto> obtenerResultados() {
        return repository.findAll();
    }
}
```

### Controlador

```java
@RestController
@RequestMapping("/api/votacion")
public class VotacionController {

    private final VotacionService votacionService;

    public VotacionController(VotacionService votacionService) {
        this.votacionService = votacionService;
    }

    @PostMapping("/votar/{personaje}")
    public Voto votar(@PathVariable String personaje) {
        return votacionService.votar(personaje);
    }

    @GetMapping("/resultados")
    public List<Voto> resultados() {
        return votacionService.obtenerResultados();
    }
}
```

---

## @Transactional

`@Transactional` asegura que:
1. Todas las operaciones de BD se ejecutan juntas
2. Si algo falla, se hace **rollback** (se deshacen los cambios)
3. La BD mantiene la consistencia

```java
@Transactional
public void transferir(Long cuentaOrigen, Long cuentaDestino, double cantidad) {
    Cuenta origen = cuentaRepository.findById(cuentaOrigen).orElseThrow();
    Cuenta destino = cuentaRepository.findById(cuentaDestino).orElseThrow();

    origen.setSaldo(origen.getSaldo() - cantidad);
    destino.setSaldo(destino.getSaldo() + cantidad);

    cuentaRepository.save(origen);
    cuentaRepository.save(destino);
    // Si algo falla aquí, AMBOS saves se deshacen
}
```

---

## Llamadas asíncronas con @Async

Para operaciones que tardan mucho (como llamar a APIs externas), podemos ejecutarlas en segundo plano:

### 1. Habilitar async

```java
@SpringBootApplication
@EnableAsync
public class ApiSimpsonsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiSimpsonsApplication.class, args);
    }
}
```

### 2. Marcar método como async

```java
@Service
public class SimpsonsQuoteService {

    @Async
    public CompletableFuture<SimpsonsQuote> obtenerFraseAsync() {
        // Esta operación se ejecuta en otro hilo
        SimpsonsQuote quote = restTemplate.getForObject(API_URL, SimpsonsQuote[].class)[0];
        return CompletableFuture.completedFuture(quote);
    }
}
```

### 3. Usar en el controlador

```java
@GetMapping("/async")
public CompletableFuture<SimpsonsQuote> obtenerAsync() {
    return quoteService.obtenerFraseAsync();
}
```

---

## Resumen de concurrencia

| Problema | Solución | Cuándo usar |
|----------|----------|-------------|
| Condición de carrera | `synchronized` | Recursos en memoria |
| Contadores concurrentes | `AtomicInteger` | Contadores simples |
| Datos en BD | `@Version` | Entidades JPA |
| Consistencia de operaciones | `@Transactional` | Múltiples operaciones BD |
| Operaciones lentas | `@Async` | Llamadas a APIs externas |

---

## Testing básico

Para verificar que la API funciona correctamente:

### 1. Añadir dependencia

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 2. Test del controlador

```java
package com.simpsons.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodos_devuelveListaPersonajes() throws Exception {
        mockMvc.perform(get("/api/personajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelvePersonaje() throws Exception {
        mockMvc.perform(get("/api/personajes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_devuelve404() throws Exception {
        mockMvc.perform(get("/api/personajes/999"))
                .andExpect(status().isNotFound());
    }
}
```

---

## Resumen del curso

| Clase | Temas |
|-------|-------|
| 1 | HTTP, REST, JSON, Primer proyecto Spring Boot |
| 2 | Arquitectura capas, CRUD completo, ResponseEntity |
| 3 | JPA, H2, Repositorios, Validación, Excepciones |
| 4 | Consumo APIs, Concurrencia, Transacciones, Testing |

---

## Siguiente paso

¡El trabajo final! Pon en práctica todo lo aprendido.
