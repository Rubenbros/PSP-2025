# Ejercicio Clase 4: Frases de Los Simpson

**Puntuación:** 0.5 puntos
**Tiempo:** 20-25 minutos

---

## Objetivo

Consumir The Simpsons Quote API y guardar frases favoritas en base de datos.

---

## Pasos

### 1. Configurar RestTemplate (2 min)

```java
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 2. Crear modelo para la API externa (3 min)

```java
public class SimpsonsQuote {
    private String quote;
    private String character;
    private String image;

    // Constructor vacío, getters, setters
}
```

### 3. Crear entidad para guardar favoritas (3 min)

```java
@Entity
public class FraseFavorita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frase;
    private String personaje;
    private LocalDateTime guardadoEn;

    // Constructores, getters, setters
}
```

### 4. Crear QuoteService (7 min)

```java
@Service
public class QuoteService {

    private final RestTemplate restTemplate;
    private final FraseFavoritaRepository repository;
    private static final String API_URL = "https://thesimpsonsquoteapi.glitch.me/quotes";

    // Constructor

    public SimpsonsQuote obtenerFraseAleatoria() {
        try {
            SimpsonsQuote[] response = restTemplate.getForObject(API_URL, SimpsonsQuote[].class);
            return (response != null && response.length > 0) ? response[0] : frasePorDefecto();
        } catch (Exception e) {
            return frasePorDefecto();
        }
    }

    public FraseFavorita guardarFraseAleatoria() {
        SimpsonsQuote quote = obtenerFraseAleatoria();
        FraseFavorita favorita = new FraseFavorita();
        favorita.setFrase(quote.getQuote());
        favorita.setPersonaje(quote.getCharacter());
        favorita.setGuardadoEn(LocalDateTime.now());
        return repository.save(favorita);
    }

    public List<FraseFavorita> obtenerFavoritas() {
        return repository.findAll();
    }

    private SimpsonsQuote frasePorDefecto() {
        SimpsonsQuote fallback = new SimpsonsQuote();
        fallback.setQuote("¡D'oh! La API no responde");
        fallback.setCharacter("Homer Simpson");
        return fallback;
    }
}
```

### 5. Crear QuoteController (5 min)

```java
@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService service;

    // Constructor

    // GET /api/quotes
    // Devuelve una frase aleatoria de la API externa

    // GET /api/quotes/favoritas
    // Devuelve las frases guardadas en BD

    // POST /api/quotes/favoritas
    // Obtiene frase de la API y la guarda en BD
    // Devuelve 201 + la frase guardada
}
```

---

## Entrega

Demuestra al profesor:

- [ ] `GET /api/quotes` → Devuelve frase aleatoria (recarga varias veces)
- [ ] `POST /api/quotes/favoritas` → Guarda una frase (201 Created)
- [ ] `POST /api/quotes/favoritas` → Guarda otra frase
- [ ] `GET /api/quotes/favoritas` → Lista las frases guardadas
