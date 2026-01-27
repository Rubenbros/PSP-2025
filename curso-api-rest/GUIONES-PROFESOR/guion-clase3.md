# GUIÓN CLASE 3 - Para el profesor
## Spring Data JPA y Persistencia

**Duración total:** 2 horas

---

## Preparación antes de clase

### Verificar
- [ ] Proyecto de la clase anterior funcionando
- [ ] Conexión a internet (para descargar dependencias)
- [ ] Tener preparada la documentación de H2 por si hay problemas

---

## Estructura de la clase

| Tiempo | Bloque | Actividad |
|--------|--------|-----------|
| 0:00-0:15 | Teoría | ¿Qué es JPA? ORM explicado |
| 0:15-0:35 | Código en vivo | Configurar JPA y H2 |
| 0:35-0:55 | Código en vivo | Convertir modelo a @Entity |
| 0:55-1:15 | Código en vivo | JpaRepository y Query Methods |
| 1:15-1:35 | Código en vivo | Validaciones y excepciones |
| 1:35-1:55 | Práctica | Ejercicios |
| 1:55-2:00 | Cierre | Entrega y dudas |

---

## BLOQUE 1: Teoría JPA (15 min)

### El problema que resuelve JPA

**Pregunta:** "¿Qué tenemos que hacer ahora para guardar un Comercio en la base de datos?"

Respuesta actual:
```java
// Sin JPA - mucho código manual
String sql = "INSERT INTO comercios (nombre, propietario) VALUES (?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, comercio.getNombre());
ps.setString(2, comercio.getPropietario());
ps.executeUpdate();
```

**Con JPA:**
```java
// Con JPA - una línea
repository.save(comercio);
```

### ORM = Object-Relational Mapping

Dibuja en la pizarra:

```
   JAVA                          BASE DE DATOS
┌─────────────┐               ┌─────────────────┐
│   Clase     │  ←── JPA ───► │     Tabla       │
│  Comercio   │               │    comercios    │
├─────────────┤               ├─────────────────┤
│ Long id     │ ◄────────────►│ id BIGINT PK    │
│ String name │ ◄────────────►│ nombre VARCHAR  │
│ int edad    │ ◄────────────►│ edad INT        │
└─────────────┘               └─────────────────┘
```

### ¿Qué es H2?

- Base de datos escrita en Java
- Funciona en memoria (RAM)
- No necesita instalación
- Perfecta para desarrollo y testing
- **Advertencia:** Los datos se pierden al reiniciar

---

## BLOQUE 2: Configurar JPA y H2 (20 min)

### Paso 1: Añadir dependencias

Abre `pom.xml` y añade:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Espera a que Maven descargue** (puede tardar).

### Paso 2: Configurar application.properties

```properties
# Base de datos H2 en memoria
spring.datasource.url=jdbc:h2:mem:springfielddb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Consola web de H2
spring.h2.console.enabled=true
```

**Explica `ddl-auto=create-drop`:**
- `create-drop`: Crea tablas al arrancar, las borra al parar
- `update`: Actualiza sin borrar datos
- `none`: No toca la BD (producción)

### Paso 3: Probar la consola H2

1. Arranca la aplicación
2. Ve a `http://localhost:8080/h2-console`
3. JDBC URL: `jdbc:h2:mem:springfielddb`
4. User: `sa`, Password: vacío
5. Conecta y muestra que no hay tablas todavía

---

## BLOQUE 3: Convertir a @Entity (20 min)

### Transformar la clase Comercio

```java
package com.springfield.api.model;

import jakarta.persistence.*;

@Entity                          // "Esta clase es una tabla"
@Table(name = "comercios")       // Nombre de la tabla (opcional)
public class Comercio {

    @Id                          // "Este campo es la clave primaria"
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremento
    private Long id;

    @Column(nullable = false)    // No puede ser null
    private String nombre;

    private String propietario;  // Sin anotación = configuración por defecto

    private String tipo;
    private String direccion;
    private boolean abierto;

    // El constructor vacío es OBLIGATORIO
    public Comercio() {}

    // Resto igual...
}
```

### Demostración

1. Arranca la aplicación
2. Ve a la consola H2
3. Ejecuta: `SELECT * FROM COMERCIOS`
4. **¡La tabla se creó sola!**

Muestra también el SQL en la consola de IntelliJ (gracias a `show-sql=true`).

---

## BLOQUE 4: JpaRepository (20 min)

### Transformar el repositorio

**ANTES (nuestra implementación manual):**
```java
@Repository
public class ComercioRepository {
    private final List<Comercio> comercios = new ArrayList<>();
    // ... 50 líneas de código
}
```

**AHORA (con JPA):**
```java
@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    // ¡Vacío! Todo viene gratis
}
```

**Reacción esperada:** "¿Ya está?" → Sí.

### Lo que viene gratis

Escribe en la pizarra:
- `findAll()` → SELECT * FROM comercios
- `findById(id)` → SELECT * WHERE id = ?
- `save(comercio)` → INSERT o UPDATE
- `deleteById(id)` → DELETE WHERE id = ?
- `count()` → SELECT COUNT(*)
- `existsById(id)` → SELECT COUNT(*) > 0

### Query Methods (la magia)

```java
public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    // Spring genera esto automáticamente según el nombre
    List<Comercio> findByTipo(String tipo);
    // → SELECT * FROM comercios WHERE tipo = ?

    List<Comercio> findByAbiertoTrue();
    // → SELECT * FROM comercios WHERE abierto = true

    List<Comercio> findByNombreContainingIgnoreCase(String nombre);
    // → SELECT * WHERE LOWER(nombre) LIKE LOWER('%?%')
}
```

**Demuestra en vivo:** Escribe un Query Method, ejecuta, y verifica el SQL en consola.

### Actualizar el Servicio

El servicio casi no cambia:

```java
public boolean eliminar(Long id) {
    if (repository.existsById(id)) {  // Nuevo método
        repository.deleteById(id);
        return true;
    }
    return false;
}
```

### Cargar datos iniciales

```java
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ComercioRepository repository) {
        return args -> {
            repository.save(new Comercio(null, "Kwik-E-Mart", ...));
            repository.save(new Comercio(null, "Taberna de Moe", ...));
            System.out.println("Datos cargados: " + repository.count());
        };
    }
}
```

**Nota:** El ID es null porque la BD lo genera.

---

## BLOQUE 5: Validaciones (20 min)

### Añadir dependencia

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### Anotaciones en el modelo

```java
@Entity
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "Entre 2 y 100 caracteres")
    private String nombre;

    // ...
}
```

### Activar en el controlador

```java
@PostMapping
public ResponseEntity<Comercio> crear(@Valid @RequestBody Comercio comercio) {
    //                                 ^^^^^^ Esto activa la validación
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.crear(comercio));
}
```

### Manejador de errores

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

### Demostración

Envía un POST con datos inválidos y muestra la respuesta:

```json
{
    "nombre": "El nombre es obligatorio",
    "tipo": "El tipo no puede estar vacío"
}
```

---

## BLOQUE 6: Práctica (20 min)

### Ejercicios a completar

1. Migrar API de comercios a JPA (Ejercicio 3.1)
2. Crear API de empleados (Ejercicio 3.2)
3. Añadir validaciones (Ejercicio 3.3)

### Errores típicos

| Error | Causa | Solución |
|-------|-------|----------|
| "No identifier specified" | Falta @Id | Añadir @Id al campo id |
| "No default constructor" | Falta constructor vacío | Añadir public Comercio() {} |
| Datos no se guardan | Falta @Transactional o error en save | Verificar servicio |
| Query Method no funciona | Nombre mal escrito | Revisar sintaxis exacta |
| Validación no funciona | Falta @Valid | Añadirlo en el controlador |

---

## Cierre (5 min)

### Verificar ejercicios
- Que muestren los endpoints funcionando
- Verificar que los datos persisten (en la misma ejecución)

### Avance de la próxima clase
"Última clase: consumiremos APIs externas como The Simpsons Quote API y veremos cómo manejar la concurrencia en APIs REST."

---

## Notas para el profesor

### Puntos clave
1. JPA elimina código repetitivo
2. Query Methods son magia pero tienen reglas
3. Validaciones son imprescindibles
4. H2 es temporal, en producción usarían MySQL/PostgreSQL

### Si hay problemas de conexión
- H2 a veces falla si la URL está mal
- Verificar exactamente: `jdbc:h2:mem:springfielddb`
- El nombre de la BD debe coincidir en properties y consola
