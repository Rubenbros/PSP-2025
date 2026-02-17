# Instrucciones del Examen Practico - Springfield Evaluator

## Introduccion

Bienvenido al examen practico de **Procesos y Servicios de Programacion (PSP)**. Este examen consiste en **10 tareas** que deberas completar de forma secuencial. Cada tarea vale **1 punto**, sumando un total de **10 puntos**.

El examen evalua tu capacidad para:
- Consumir APIs REST desde tu aplicacion Spring Boot
- Implementar tu propia API REST con operaciones CRUD
- Manejar codigos HTTP correctamente
- Procesar datos JSON
- Exponer servicios que seran evaluados automaticamente

---

## Requisitos Previos

Antes de comenzar, asegurate de tener:

1. **Java 17** o superior instalado
2. Un proyecto **Spring Boot** configurado (se recomienda Spring Boot 3.x)
3. Dependencias necesarias en tu `pom.xml`:
   - `spring-boot-starter-web`
   - `spring-boot-starter-webflux` (opcional, si usas WebClient)
4. Una cuenta en **ngrok** (https://ngrok.com) - necesaria a partir de la Tarea 4
5. **ngrok** instalado y autenticado en tu equipo

### Dependencias Maven recomendadas

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
</dependencies>
```

---

## URL Base

A lo largo de estas instrucciones, se usa `{BASE_URL}` para referirse a la URL del servidor del profesor. Sustituye `{BASE_URL}` por la URL real que te proporcionara el profesor (por ejemplo: `https://springfield-evaluator.vercel.app`).

---

## Tareas

### Tarea 1 - Registro

**Objetivo:** Registrarte en el sistema del evaluador.

**Que debes hacer:**
Realiza una peticion GET al servidor del profesor con tu nombre y apellido (sin espacios, sin tildes).

**Endpoint:**
```
GET {BASE_URL}/api/tareas/1?nombre=NombreApellido
```

**Ejemplo con curl:**
```bash
curl "{BASE_URL}/api/tareas/1?nombre=HomerSimpson"
```

**Respuesta esperada (ejemplo):**
```json
{
  "alumno_id": 1,
  "mensaje": "Registro completado correctamente"
}
```

**Codigo Java con RestTemplate:**
```java
@Service
public class TareaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "{BASE_URL}";

    public void tarea1() {
        String url = BASE_URL + "/api/tareas/1?nombre=NombreApellido";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Respuesta: " + response.getBody());
    }
}
```

> **IMPORTANTE:** Guarda el `alumno_id` que recibas. Lo necesitaras para todas las tareas siguientes.

---

### Tarea 2 - Enviar datos personales

**Objetivo:** Enviar tus datos personales al servidor del profesor.

**Que debes hacer:**
Realiza una peticion POST enviando un JSON con tus datos.

**Endpoint:**
```
POST {BASE_URL}/api/tareas/2
```

**Body JSON:**
```json
{
  "alumno_id": 1,
  "nombre": "Homer",
  "apellidos": "Simpson",
  "email": "homer@springfield.com"
}
```

**Ejemplo con curl:**
```bash
curl -X POST "{BASE_URL}/api/tareas/2" \
  -H "Content-Type: application/json" \
  -d '{"alumno_id": 1, "nombre": "Homer", "apellidos": "Simpson", "email": "homer@springfield.com"}'
```

**Codigo Java con RestTemplate:**
```java
public void tarea2(int alumnoId) {
    String url = BASE_URL + "/api/tareas/2";

    Map<String, Object> body = new HashMap<>();
    body.put("alumno_id", alumnoId);
    body.put("nombre", "Homer");
    body.put("apellidos", "Simpson");
    body.put("email", "homer@springfield.com");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    System.out.println("Respuesta: " + response.getBody());
}
```

---

### Tarea 3 - Filtrar y calcular

**Objetivo:** Obtener una lista de personajes de Springfield, filtrarlos y calcular la media de edad.

**Paso 1 - Obtener datos:**
```
GET {BASE_URL}/api/tareas/3?alumno_id=1
```

Recibiras una lista de personajes con nombre y edad:
```json
[
  {"nombre": "Homer Simpson", "edad": 39},
  {"nombre": "Bart Simpson", "edad": 10},
  {"nombre": "Marge Simpson", "edad": 36},
  ...
]
```

**Paso 2 - Procesar datos:**
1. Filtra los personajes **mayores de 18 anios**
2. Ordena los personajes filtrados **alfabeticamente por nombre**
3. Calcula la **media de edad** de los personajes filtrados

**Paso 3 - Enviar resultado:**
```
POST {BASE_URL}/api/tareas/3
```

**Body JSON:**
```json
{
  "alumno_id": 1,
  "resultado": {
    "personajes_filtrados": [
      {"nombre": "Apu Nahasapeemapetilon", "edad": 45},
      {"nombre": "Homer Simpson", "edad": 39},
      {"nombre": "Marge Simpson", "edad": 36},
      {"nombre": "Montgomery Burns", "edad": 104},
      {"nombre": "Ned Flanders", "edad": 60}
    ],
    "media_edad": 56.8
  }
}
```

**Ejemplo con curl:**
```bash
# Paso 1: Obtener personajes
curl "{BASE_URL}/api/tareas/3?alumno_id=1"

# Paso 2: Enviar resultado procesado
curl -X POST "{BASE_URL}/api/tareas/3" \
  -H "Content-Type: application/json" \
  -d '{"alumno_id": 1, "resultado": {"personajes_filtrados": [...], "media_edad": 56.8}}'
```

**Codigo Java con RestTemplate:**
```java
public void tarea3(int alumnoId) {
    // Paso 1: Obtener personajes
    String getUrl = BASE_URL + "/api/tareas/3?alumno_id=" + alumnoId;
    ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
        getUrl, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
    );
    List<Map<String, Object>> personajes = response.getBody();

    // Paso 2: Filtrar mayores de 18 y ordenar alfabeticamente
    List<Map<String, Object>> filtrados = personajes.stream()
        .filter(p -> ((Number) p.get("edad")).intValue() > 18)
        .sorted(Comparator.comparing(p -> (String) p.get("nombre")))
        .collect(Collectors.toList());

    // Paso 3: Calcular media de edad
    double media = filtrados.stream()
        .mapToInt(p -> ((Number) p.get("edad")).intValue())
        .average()
        .orElse(0.0);

    // Paso 4: Enviar resultado
    String postUrl = BASE_URL + "/api/tareas/3";
    Map<String, Object> resultado = new HashMap<>();
    resultado.put("personajes_filtrados", filtrados);
    resultado.put("media_edad", media);

    Map<String, Object> body = new HashMap<>();
    body.put("alumno_id", alumnoId);
    body.put("resultado", resultado);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, request, String.class);
    System.out.println("Respuesta: " + postResponse.getBody());
}
```

---

### Tarea 4 - Configurar ngrok y exponer tu servidor

**Objetivo:** Hacer que tu servidor Spring Boot sea accesible desde Internet usando ngrok.

**Que debes hacer:**

1. Asegurate de que tu proyecto Spring Boot esta corriendo (por defecto en el puerto 8080).
2. En tu servidor, implementa un endpoint `/ping` que devuelva:
   ```json
   {"status": "ok"}
   ```

   ```java
   @RestController
   public class PingController {
       @GetMapping("/ping")
       public Map<String, String> ping() {
           return Map.of("status", "ok");
       }
   }
   ```

3. Abre ngrok y exponlo:
   ```bash
   ngrok http 8080
   ```

4. Copia la URL que te da ngrok (por ejemplo: `https://abc123.ngrok-free.app`)

5. Envia tu URL al servidor del profesor:
   ```
   POST {BASE_URL}/api/tareas/4
   ```

   ```json
   {
     "alumno_id": 1,
     "url": "https://abc123.ngrok-free.app"
   }
   ```

**Ejemplo con curl:**
```bash
curl -X POST "{BASE_URL}/api/tareas/4" \
  -H "Content-Type: application/json" \
  -d '{"alumno_id": 1, "url": "https://abc123.ngrok-free.app"}'
```

**Codigo Java:**
```java
public void tarea4(int alumnoId, String ngrokUrl) {
    String url = BASE_URL + "/api/tareas/4";

    Map<String, Object> body = new HashMap<>();
    body.put("alumno_id", alumnoId);
    body.put("url", ngrokUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    System.out.println("Respuesta: " + response.getBody());
}
```

> **IMPORTANTE:** A partir de aqui, el servidor del profesor hara peticiones a TU servidor. Asegurate de que ngrok sigue activo durante todo el examen.

---

### Tarea 5 - API CRUD: Crear y leer personajes

**Objetivo:** Implementar en tu servidor Spring Boot los endpoints para crear y leer personajes.

**Que debes implementar en tu servidor:**

1. `POST /api/personajes` - Crear un personaje
   - Recibe: `{"nombre": "...", "edad": ..., "profesion": "..."}`
   - Devuelve: El personaje creado con su `id` y codigo **201 Created**

2. `GET /api/personajes` - Listar todos los personajes
   - Devuelve: Array de personajes

3. `GET /api/personajes/{id}` - Obtener un personaje por ID
   - Devuelve: El personaje correspondiente

**Ejemplo de modelo:**
```java
@Entity
public class Personaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int edad;
    private String profesion;

    // Getters y Setters
}
```

**Ejemplo de controlador:**
```java
@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    private final List<Personaje> personajes = new ArrayList<>();
    private long nextId = 1;

    @PostMapping
    public ResponseEntity<Personaje> crear(@RequestBody Personaje personaje) {
        personaje.setId(nextId++);
        personajes.add(personaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaje);
    }

    @GetMapping
    public List<Personaje> listar() {
        return personajes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable Long id) {
        return personajes.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

**Como se evalua:**
El servidor del profesor hara peticiones a tu servidor para verificar que los endpoints funcionan correctamente. Asegurate de que ngrok sigue activo.

Para lanzar la evaluacion:
```bash
# Desde el dashboard del profesor, o manualmente:
curl "{BASE_URL}/api/evaluar/{alumno_id}/5"
```

---

### Tarea 6 - CRUD completo

**Objetivo:** Completar tu API anadiendo operaciones de actualizar y eliminar.

**Que debes implementar:**

1. `PUT /api/personajes/{id}` - Actualizar un personaje
   - Recibe: `{"nombre": "...", "edad": ..., "profesion": "..."}`
   - Devuelve: El personaje actualizado

2. `DELETE /api/personajes/{id}` - Eliminar un personaje
   - Devuelve: Codigo **204 No Content**

**Ejemplo de controlador (anadido a la Tarea 5):**
```java
@PutMapping("/{id}")
public ResponseEntity<Personaje> actualizar(@PathVariable Long id, @RequestBody Personaje datos) {
    for (int i = 0; i < personajes.size(); i++) {
        if (personajes.get(i).getId().equals(id)) {
            datos.setId(id);
            personajes.set(i, datos);
            return ResponseEntity.ok(datos);
        }
    }
    return ResponseEntity.notFound().build();
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    boolean eliminado = personajes.removeIf(p -> p.getId().equals(id));
    if (eliminado) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}
```

**Como se evalua:**
El profesor creara un personaje, verificara que existe, lo actualizara, verificara el cambio, lo eliminara y comprobara que ya no existe (404).

---

### Tarea 7 - Codigos HTTP correctos

**Objetivo:** Asegurar que tu API devuelve los codigos HTTP adecuados en cada situacion.

**Codigos esperados:**

| Situacion | Codigo esperado |
|---|---|
| `GET /api/personajes/99999` (no existe) | **404 Not Found** |
| `POST /api/personajes` con body vacio | **400 Bad Request** |
| `POST /api/personajes` con datos validos | **201 Created** |
| `DELETE /api/personajes/{id}` existente | **204 No Content** |
| `DELETE /api/personajes/99999` (no existe) | **404 Not Found** |

**Ejemplo de validacion en el POST:**
```java
@PostMapping
public ResponseEntity<?> crear(@RequestBody(required = false) Personaje personaje) {
    if (personaje == null || personaje.getNombre() == null || personaje.getNombre().isBlank()) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "El nombre es obligatorio"));
    }
    personaje.setId(nextId++);
    personajes.add(personaje);
    return ResponseEntity.status(HttpStatus.CREATED).body(personaje);
}
```

---

### Tarea 8 - Validaciones y busqueda

**Objetivo:** Implementar validaciones robustas y un endpoint de busqueda.

**Que debes implementar:**

1. Validar que el campo `nombre` no este vacio al crear un personaje
2. Validar que el body no este vacio al crear un personaje
3. Implementar un endpoint de busqueda:

```
GET /api/personajes/buscar?nombre=Krusty
```

Debe devolver un array con los personajes cuyo nombre contenga el texto buscado.

**Ejemplo de endpoint de busqueda:**
```java
@GetMapping("/buscar")
public List<Personaje> buscar(@RequestParam String nombre) {
    return personajes.stream()
        .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
        .collect(Collectors.toList());
}
```

---

### Tarea 9 - Consumir API externa y exponer estadisticas

**Objetivo:** Tu servidor debe consumir datos del servidor del profesor y exponer un endpoint con estadisticas calculadas.

**Paso 1:** Tu servidor debe hacer una peticion GET al endpoint del profesor para obtener los datos de ciudadanos:
```
GET {BASE_URL}/api/tareas/9/ciudadanos?alumno_id={tu_alumno_id}
```

Recibiras una lista como:
```json
[
  {"nombre": "Homer Simpson", "edad": 39, "salario": 25000},
  {"nombre": "Ned Flanders", "edad": 60, "salario": 45000},
  ...
]
```

**Paso 2:** Implementa un endpoint en tu servidor:
```
GET /api/estadisticas
```

Que devuelva:
```json
{
  "salario_medio": 35000.0,
  "persona_mas_joven": "Bart Simpson",
  "mayor_salario": "Montgomery Burns"
}
```

Donde:
- `salario_medio`: la media aritmetica de todos los salarios
- `persona_mas_joven`: el nombre de la persona con menor edad
- `mayor_salario`: el nombre de la persona con el salario mas alto

**Ejemplo de implementacion con RestTemplate:**
```java
@RestController
@RequestMapping("/api")
public class EstadisticasController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "{BASE_URL}";
    private static final int ALUMNO_ID = 1; // Tu alumno_id

    @GetMapping("/estadisticas")
    public Map<String, Object> estadisticas() {
        String url = BASE_URL + "/api/tareas/9/ciudadanos?alumno_id=" + ALUMNO_ID;

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        List<Map<String, Object>> ciudadanos = response.getBody();

        double salarioMedio = ciudadanos.stream()
            .mapToDouble(c -> ((Number) c.get("salario")).doubleValue())
            .average()
            .orElse(0.0);

        String masJoven = ciudadanos.stream()
            .min(Comparator.comparingInt(c -> ((Number) c.get("edad")).intValue()))
            .map(c -> (String) c.get("nombre"))
            .orElse("");

        String mayorSalario = ciudadanos.stream()
            .max(Comparator.comparingDouble(c -> ((Number) c.get("salario")).doubleValue()))
            .map(c -> (String) c.get("nombre"))
            .orElse("");

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("salario_medio", salarioMedio);
        resultado.put("persona_mas_joven", masJoven);
        resultado.put("mayor_salario", mayorSalario);

        return resultado;
    }
}
```

---

### Tarea 10 - Flujo multi-paso con autenticacion

**Objetivo:** Implementar un flujo completo que involucra autenticacion con token, procesamiento de datos y verificacion.

**Paso 1:** Tu servidor inicia el flujo haciendo:
```
POST {BASE_URL}/api/tareas/10/iniciar
Body: {"alumno_id": 1}
```

Recibiras un token:
```json
{"token": "abc123-uuid-aqui"}
```

**Paso 2:** Usa el token para obtener la lista de productos:
```
GET {BASE_URL}/api/tareas/10/productos
Header: Authorization: Bearer abc123-uuid-aqui
```

Recibiras:
```json
[
  {"nombre": "Dona de Homer", "precio": 75.0},
  {"nombre": "Cerveza Duff", "precio": 30.0},
  {"nombre": "Saxofon de Lisa", "precio": 120.0},
  ...
]
```

**Paso 3:** Procesa los productos aplicando un **10% de descuento** a los que cuesten **mas de 50 euros**. Calcula el total.

Ejemplo:
- "Dona de Homer" (75.0) -> 75.0 * 0.9 = 67.5
- "Cerveza Duff" (30.0) -> 30.0 (sin descuento, <= 50)
- "Saxofon de Lisa" (120.0) -> 120.0 * 0.9 = 108.0

**Paso 4:** Envia el resultado al servidor del profesor:
```
POST {BASE_URL}/api/tareas/10/resultado
```
```json
{
  "alumno_id": 1,
  "token": "abc123-uuid-aqui",
  "productos_procesados": [
    {"nombre": "Dona de Homer", "precio_original": 75.0, "precio_final": 67.5},
    {"nombre": "Cerveza Duff", "precio_original": 30.0, "precio_final": 30.0},
    {"nombre": "Saxofon de Lisa", "precio_original": 120.0, "precio_final": 108.0}
  ],
  "total": 205.5
}
```

Recibiras un codigo de verificacion:
```json
{"codigo_verificacion": "SPRING-XXXX"}
```

**Paso 5:** Implementa en tu servidor un endpoint:
```
GET /api/verificacion
```

Que devuelva:
```json
{"codigo_verificacion": "SPRING-XXXX"}
```

**Ejemplo de implementacion completa:**
```java
@RestController
@RequestMapping("/api")
public class VerificacionController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "{BASE_URL}";
    private static final int ALUMNO_ID = 1;
    private String codigoVerificacion;

    @PostMapping("/realizar-tarea10")
    public Map<String, Object> realizarTarea10() {
        // Paso 1: Iniciar flujo
        Map<String, Object> iniciarBody = Map.of("alumno_id", ALUMNO_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> iniciarResp = restTemplate.postForEntity(
            BASE_URL + "/api/tareas/10/iniciar",
            new HttpEntity<>(iniciarBody, headers), Map.class
        );
        String token = (String) iniciarResp.getBody().get("token");

        // Paso 2: Obtener productos con token
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.set("Authorization", "Bearer " + token);
        ResponseEntity<List<Map<String, Object>>> productosResp = restTemplate.exchange(
            BASE_URL + "/api/tareas/10/productos",
            HttpMethod.GET,
            new HttpEntity<>(authHeaders),
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        List<Map<String, Object>> productos = productosResp.getBody();

        // Paso 3: Procesar - 10% descuento a los que cuestan > 50
        List<Map<String, Object>> procesados = new ArrayList<>();
        double total = 0;
        for (Map<String, Object> prod : productos) {
            double precioOriginal = ((Number) prod.get("precio")).doubleValue();
            double precioFinal = precioOriginal > 50 ? precioOriginal * 0.9 : precioOriginal;
            total += precioFinal;

            Map<String, Object> procesado = new HashMap<>();
            procesado.put("nombre", prod.get("nombre"));
            procesado.put("precio_original", precioOriginal);
            procesado.put("precio_final", precioFinal);
            procesados.add(procesado);
        }

        // Paso 4: Enviar resultado
        Map<String, Object> resultadoBody = new HashMap<>();
        resultadoBody.put("alumno_id", ALUMNO_ID);
        resultadoBody.put("token", token);
        resultadoBody.put("productos_procesados", procesados);
        resultadoBody.put("total", total);

        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> resultadoResp = restTemplate.postForEntity(
            BASE_URL + "/api/tareas/10/resultado",
            new HttpEntity<>(resultadoBody, headers), Map.class
        );

        codigoVerificacion = (String) resultadoResp.getBody().get("codigo_verificacion");
        return Map.of("codigo_verificacion", codigoVerificacion);
    }

    @GetMapping("/verificacion")
    public Map<String, String> verificacion() {
        return Map.of("codigo_verificacion", codigoVerificacion != null ? codigoVerificacion : "");
    }
}
```

---

## Guia de Configuracion de ngrok

### 1. Crear cuenta
Ve a https://ngrok.com y crea una cuenta gratuita.

### 2. Instalar ngrok
Descarga ngrok desde https://ngrok.com/download e instalalo.

### 3. Autenticarte
```bash
ngrok config add-authtoken TU_TOKEN_AQUI
```
(Encuentra tu token en https://dashboard.ngrok.com/get-started/your-authtoken)

### 4. Exponer tu servidor
```bash
ngrok http 8080
```

### 5. Copiar la URL
Veras algo como:
```
Forwarding  https://abc123.ngrok-free.app -> http://localhost:8080
```
Copia la URL HTTPS. Esta es la URL que enviaras en la Tarea 4.

> **Nota:** La URL de ngrok cambia cada vez que lo reinicias (en la version gratuita). Si reinicias ngrok, deberas enviar la nueva URL en la Tarea 4 otra vez.

---

## Resolucion de Problemas

### Error "alumno_id no encontrado"
- Asegurate de haberte registrado primero con la Tarea 1
- Verifica que estas usando el `alumno_id` correcto

### Error "Campos obligatorios faltantes"
- Revisa que tu JSON tenga todos los campos requeridos
- Verifica que el `Content-Type` sea `application/json`

### Error al conectar con ngrok
- Verifica que tu servidor Spring Boot esta corriendo
- Comprueba que ngrok esta activo y apuntando al puerto correcto
- Prueba acceder a tu URL de ngrok desde el navegador

### Error 404 en tus endpoints
- Asegurate de que las rutas coinciden exactamente con las especificadas
- Revisa que tu aplicacion Spring Boot no tiene un `context-path` configurado
- Verifica que el controlador tiene las anotaciones correctas (`@RestController`, `@RequestMapping`)

### Error de CORS
Si recibes errores de CORS, anade esta configuracion:
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*");
    }
}
```

### Error "Connection refused" desde el servidor del profesor
- ngrok debe estar corriendo
- Tu servidor Spring Boot debe estar corriendo
- La URL enviada en la Tarea 4 debe ser la actual de ngrok

### Los datos se pierden al reiniciar
- Si usas almacenamiento en memoria (ArrayList), los datos se pierden al reiniciar
- Para el examen esto es aceptable, simplemente re-ejecuta las tareas que creen datos

---

## Flujo Recomendado

1. **Tareas 1-3:** Haz peticiones HTTP al servidor del profesor desde tu codigo Java
2. **Tarea 4:** Configura ngrok y registra tu URL
3. **Tareas 5-8:** Implementa la API CRUD en tu servidor - el profesor la evaluara remotamente
4. **Tarea 9:** Consume la API del profesor y expone estadisticas
5. **Tarea 10:** Completa el flujo multi-paso con autenticacion

Buena suerte!
