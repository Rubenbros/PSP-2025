# Ejercicio Clase 1: API de Ciudadanos de Springfield

**Puntuación:** 0.5 puntos
**Tiempo:** 20-25 minutos

---

## Objetivo

Crear una API REST básica que gestione ciudadanos de Springfield.

---

## Pasos

### 1. Crear el proyecto (5 min)

1. Ve a [start.spring.io](https://start.spring.io)
2. Configura: Maven, Java 17, Spring Boot 3.2.x
3. Group: `com.springfield`
4. Artifact: `ciudadanos-api`
5. Dependencia: **Spring Web**
6. Genera, descomprime y abre en IntelliJ

### 2. Crear el modelo (5 min)

Crea la clase `Ciudadano` en el paquete `model`:

```java
public class Ciudadano {
    private Long id;
    private String nombre;
    private String ocupacion;
    private int edad;

    // Constructor vacío
    // Constructor con todos los parámetros
    // Getters y Setters (usa Alt+Insert en IntelliJ)
}
```

### 3. Crear el controlador (10-15 min)

Crea `CiudadanoController` en el paquete `controller` con estos endpoints:

```java
@RestController
@RequestMapping("/api/ciudadanos")
public class CiudadanoController {

    // Lista de ciudadanos (simula base de datos)
    private List<Ciudadano> ciudadanos = new ArrayList<>(Arrays.asList(
        new Ciudadano(1L, "Homer Simpson", "Inspector de seguridad", 39),
        new Ciudadano(2L, "Marge Simpson", "Ama de casa", 36),
        new Ciudadano(3L, "Bart Simpson", "Estudiante", 10),
        new Ciudadano(4L, "Lisa Simpson", "Estudiante", 8),
        new Ciudadano(5L, "Moe Szyslak", "Barman", 45)
    ));

    // GET /api/ciudadanos
    // Devuelve todos los ciudadanos

    // GET /api/ciudadanos/{id}
    // Devuelve un ciudadano por ID
    // Si no existe, devuelve un ciudadano con nombre "No encontrado"

    // GET /api/ciudadanos/buscar?nombre=xxx
    // Busca ciudadanos cuyo nombre contenga el texto (ignorar mayúsculas)
    // Ejemplo: /api/ciudadanos/buscar?nombre=simpson → devuelve los 4 Simpson

    // GET /api/ciudadanos/mayores/{edad}
    // Devuelve ciudadanos con edad mayor a la indicada
    // Ejemplo: /api/ciudadanos/mayores/18 → Homer, Marge, Moe
}
```

---

## Entrega

Demuestra al profesor que funcionan estos endpoints:

- [ ] `http://localhost:8080/api/ciudadanos`
- [ ] `http://localhost:8080/api/ciudadanos/1`
- [ ] `http://localhost:8080/api/ciudadanos/99` (debe devolver "No encontrado")
- [ ] `http://localhost:8080/api/ciudadanos/buscar?nombre=simpson`
- [ ] `http://localhost:8080/api/ciudadanos/mayores/18`
