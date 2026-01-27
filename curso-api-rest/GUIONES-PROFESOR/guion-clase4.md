# GUIÓN CLASE 4 - Para el profesor
## Consumo de APIs externas y Concurrencia

**Duración total:** 2 horas

---

## Preparación antes de clase

### Verificar
- [ ] The Simpsons Quote API funcionando: `https://thesimpsonsquoteapi.glitch.me/quotes`
- [ ] Proyecto de la clase anterior funcionando
- [ ] Tener ejemplos del primer trimestre sobre concurrencia a mano

---

## Estructura de la clase

| Tiempo | Bloque | Actividad |
|--------|--------|-----------|
| 0:00-0:10 | Repaso | Conectar con primer trimestre |
| 0:10-0:35 | Código en vivo | Consumir API externa con RestTemplate |
| 0:35-0:55 | Código en vivo | Combinar API externa con BD local |
| 0:55-1:20 | Código en vivo | Concurrencia en APIs |
| 1:20-1:40 | Práctica | Ejercicios |
| 1:40-2:00 | Cierre | Presentar trabajo final |

---

## BLOQUE 1: Conexión con primer trimestre (10 min)

### Repaso rápido

**Pregunta:** "¿Qué problemas teníamos con los hilos en el primer trimestre?"

Respuestas esperadas:
- Race conditions
- Dos hilos modificando lo mismo
- Datos inconsistentes

**Conexión:** "En una API REST, cada petición es un hilo diferente. ¿Qué pasa si dos usuarios compran el último producto a la vez?"

```
Usuario A ─────► [Hilo A] ──┐
                            ├──► stock = 1 → ¿Quién lo consigue?
Usuario B ─────► [Hilo B] ──┘
```

**Hoy veremos:**
1. Cómo consumir otras APIs
2. Cómo proteger operaciones críticas

---

## BLOQUE 2: Consumir API externa (25 min)

### Configurar RestTemplate

```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**Explica @Bean:**
- "Es como decirle a Spring: crea este objeto y tenlo disponible"
- "Cuando alguien pida un RestTemplate, usa este"

### Crear modelo para la respuesta

**Primero, explora la API en el navegador:**
```
https://thesimpsonsquoteapi.glitch.me/quotes
```

**Muestra la respuesta:**
```json
[
    {
        "quote": "D'oh!",
        "character": "Homer Simpson",
        "image": "https://...",
        "characterDirection": "Right"
    }
]
```

**Crea la clase:**
```java
public class SimpsonsQuote {
    private String quote;
    private String character;
    private String image;
    private String characterDirection;

    // Getters, setters
}
```

### Crear el servicio

```java
@Service
public class QuoteService {

    private final RestTemplate restTemplate;
    private static final String API_URL =
        "https://thesimpsonsquoteapi.glitch.me/quotes";

    public QuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SimpsonsQuote obtenerFraseAleatoria() {
        SimpsonsQuote[] response = restTemplate.getForObject(
            API_URL,
            SimpsonsQuote[].class  // La API devuelve un array
        );
        return response[0];  // Cogemos el primero
    }
}
```

**Demuestra en vivo:** Llama al endpoint y muestra la frase.

### Manejo de errores

**Pregunta:** "¿Qué pasa si la API de Simpsons está caída?"

```java
public SimpsonsQuote obtenerFraseAleatoria() {
    try {
        SimpsonsQuote[] response = restTemplate.getForObject(
            API_URL, SimpsonsQuote[].class);
        if (response != null && response.length > 0) {
            return response[0];
        }
    } catch (Exception e) {
        System.err.println("Error llamando a API: " + e.getMessage());
    }

    // Valor por defecto si falla
    SimpsonsQuote fallback = new SimpsonsQuote();
    fallback.setQuote("¡D'oh! La API no responde");
    fallback.setCharacter("Homer Simpson");
    return fallback;
}
```

**Mensaje clave:** "Nunca confíes en servicios externos. Siempre ten un plan B."

---

## BLOQUE 3: Combinar API externa con BD (20 min)

### Caso de uso: Guardar frases favoritas

"Queremos guardar las frases que nos gustan en nuestra base de datos."

### Entidad

```java
@Entity
public class FraseFavorita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frase;
    private String personaje;
    private LocalDateTime guardadoEn;
}
```

### Servicio combinado

```java
@Service
public class FavoritasService {

    private final FraseFavoritaRepository repository;
    private final QuoteService quoteService;  // Servicio de API externa

    public FavoritasService(FraseFavoritaRepository repository,
                           QuoteService quoteService) {
        this.repository = repository;
        this.quoteService = quoteService;
    }

    public FraseFavorita guardarAleatoria() {
        // 1. Llamar a API externa
        SimpsonsQuote quote = quoteService.obtenerFraseAleatoria();

        // 2. Convertir a nuestra entidad
        FraseFavorita favorita = new FraseFavorita();
        favorita.setFrase(quote.getQuote());
        favorita.setPersonaje(quote.getCharacter());
        favorita.setGuardadoEn(LocalDateTime.now());

        // 3. Guardar en BD
        return repository.save(favorita);
    }
}
```

**Demuestra:**
1. POST `/api/favoritas/guardar-aleatoria`
2. Ver la frase guardada en la BD
3. GET `/api/favoritas` → lista de guardadas

---

## BLOQUE 4: Concurrencia en APIs (25 min)

### El problema

**Escenario:** Sistema de votación para el personaje favorito.

```java
// ❌ CÓDIGO PELIGROSO
public void votar(String personaje) {
    Votacion v = repository.findByPersonaje(personaje);
    v.setVotos(v.getVotos() + 1);  // ← PELIGRO
    repository.save(v);
}
```

**Dibuja la race condition:**
```
Tiempo    Hilo A                  Hilo B                  BD
──────────────────────────────────────────────────────────────
t1        lee votos = 10
t2                                lee votos = 10
t3        votos = 10 + 1 = 11
t4                                votos = 10 + 1 = 11
t5        guarda 11
t6                                guarda 11               votos = 11
                                                          (debería ser 12!)
```

### Solución 1: @Transactional

```java
@Service
public class VotacionService {

    @Transactional  // "Todo esto es una operación atómica"
    public Votacion votar(String personaje) {
        Votacion votacion = repository.findByPersonaje(personaje)
            .orElseGet(() -> new Votacion(personaje));

        votacion.setVotos(votacion.getVotos() + 1);
        return repository.save(votacion);
    }
}
```

**Explica @Transactional:**
- "Asegura que todo se ejecute junto"
- "Si algo falla, se deshacen todos los cambios"
- "Pero NO evita completamente la race condition..."

### Solución 2: @Version (Optimistic Locking)

```java
@Entity
public class Votacion {
    @Id
    @GeneratedValue
    private Long id;

    private String personaje;
    private int votos;

    @Version  // ← LA CLAVE
    private Long version;
}
```

**Explica cómo funciona:**
```
Tiempo    Hilo A                  Hilo B                  BD
──────────────────────────────────────────────────────────────
t1        lee votos=10, ver=1
t2                                lee votos=10, ver=1
t3        votos=11, ver=2
t4        guarda (ver=1→2) ✅
t5                                votos=11, ver=2
t6                                guarda (ver=1→?) ❌ FALLA
                                  (la BD tiene ver=2)
```

**El segundo hilo reintenta:** "Si falla, vuelve a leer y sumar"

### Demostración práctica

Si da tiempo, muestra cómo probar concurrencia:

```java
// Test simple (no hace falta que lo implementen)
ExecutorService executor = Executors.newFixedThreadPool(10);
for (int i = 0; i < 100; i++) {
    executor.submit(() -> votacionService.votar("Homer"));
}
// Al final, Homer debería tener exactamente 100 votos
```

---

## BLOQUE 5: Práctica (20 min)

### Ejercicios

1. **Consumir API** (Ejercicio 4.1) - Básico
2. **Guardar favoritas** (Ejercicio 4.2) - Intermedio
3. **Votaciones** (Ejercicio 4.3) - Avanzado

**Consejo:** Que empiecen por el 4.1 y avancen según tiempo.

### Errores típicos

| Error | Causa | Solución |
|-------|-------|----------|
| No se conecta a la API | Sin internet o URL mal | Verificar URL en navegador |
| JSON mal parseado | Campos no coinciden | Nombres exactos igual que JSON |
| @Transactional no funciona | Llamada desde mismo servicio | Mover a otro servicio |
| NullPointerException | La API devuelve null | Siempre verificar antes |

---

## BLOQUE 6: Presentación del trabajo final (20 min)

### Mostrar el enunciado

Proyecta `trabajo-final/enunciado.md` y explica:

1. **Las 3 opciones:** Cinema, Festival, Quiz
2. **Requisitos técnicos obligatorios**
3. **Rúbrica de evaluación**
4. **Fecha de entrega:** 28 de febrero

### Puntos importantes a recalcar

1. **Individual** - Nada de copiar
2. **Concurrencia obligatoria** - Tiene que haber al menos una operación que la maneje
3. **API externa obligatoria** - Consumir Simpsons API
4. **Documentación** - README y archivo de requests

### Sistema de puntuación completo

```
Ejercicios Clase 1:  0.5 puntos
Ejercicios Clase 2:  0.5 puntos
Ejercicios Clase 3:  0.5 puntos
Ejercicios Clase 4:  0.5 puntos
Trabajo Final:       8.0 puntos
────────────────────────────────
TOTAL:              10.0 puntos
```

### Tiempo para preguntas

Reserva los últimos 5-10 minutos para dudas sobre el trabajo.

**Preguntas frecuentes:**
- "¿Puedo hacer otra temática?" → Sí, pero tiene que tener la misma complejidad
- "¿Puedo usar MySQL?" → Sí, pero H2 es más fácil para entregar
- "¿Cuántos endpoints mínimo?" → Los que pide el enunciado, todos
- "¿Y si no me da tiempo a todo?" → Prioriza funcionalidad básica

---

## Cierre del curso

### Resumen de las 4 clases

| Clase | Aprendido |
|-------|-----------|
| 1 | HTTP, REST, JSON, Spring Boot básico |
| 2 | Arquitectura capas, CRUD, ResponseEntity |
| 3 | JPA, H2, Repositorios, Validaciones |
| 4 | APIs externas, Concurrencia, Transacciones |

### Despedida

"Ya tenéis todo lo necesario para el trabajo. Empezad pronto, preguntad dudas, y suerte."

---

## Notas para el profesor

### Puntos críticos de esta clase

1. **RestTemplate puede fallar** - Siempre tener plan B
2. **La concurrencia es difícil** - No esperes que todos lo entiendan a la primera
3. **@Version es nuevo** - Puede que necesiten más explicación

### Si la API de Simpsons está caída

Usa una alternativa o prepara respuestas mockeadas:
```java
// Backup: devolver siempre lo mismo
return new SimpsonsQuote("D'oh!", "Homer Simpson", null, "Right");
```

### Seguimiento post-curso

- Establece horario de tutorías para dudas del trabajo
- Recuerda la fecha de entrega periódicamente
- Ofrece revisiones intermedias si lo solicitan
