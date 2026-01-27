# Clase 3: Persistencia con Spring Data JPA

## ¿Qué es JPA?

**JPA** (Java Persistence API) es una especificación de Java para mapear objetos a bases de datos relacionales.

**Hibernate** es la implementación más popular de JPA.

**Spring Data JPA** es una capa sobre JPA que simplifica aún más el acceso a datos.

```
Tu código
    ↓
Spring Data JPA (simplifica)
    ↓
JPA/Hibernate (mapea objetos ↔ tablas)
    ↓
JDBC (conexión a BD)
    ↓
Base de datos
```

---

## ORM: Object-Relational Mapping

JPA es un **ORM**: mapea clases Java a tablas de base de datos.

| Java | Base de datos |
|------|---------------|
| Clase | Tabla |
| Atributo | Columna |
| Objeto | Fila |
| Tipo de dato | Tipo SQL |

```java
// Clase Java
public class Personaje {
    private Long id;
    private String nombre;
    private int edad;
}
```

```sql
-- Tabla SQL equivalente
CREATE TABLE personaje (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(255),
    edad INT
);
```

---

## Configurar Spring Data JPA

### 1. Añadir dependencias en `pom.xml`

```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Base de datos H2 (en memoria, ideal para desarrollo) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 2. Configurar `application.properties`

```properties
# Configuración de H2 (base de datos en memoria)
spring.datasource.url=jdbc:h2:mem:simpsonsdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Consola web de H2 (para ver los datos)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### ¿Qué es H2?

H2 es una base de datos escrita en Java que:
- Funciona en memoria (no necesita instalación)
- Se reinicia cada vez que arrancas la aplicación
- Tiene una consola web para ver los datos
- Perfecta para desarrollo y testing

Para acceder a la consola: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:simpsonsdb`
- User: `sa`
- Password: (vacío)

---

## Convertir el modelo en Entidad

```java
package com.simpsons.api.model;

import jakarta.persistence.*;

@Entity                              // Esta clase es una entidad JPA
@Table(name = "personajes")          // Nombre de la tabla (opcional)
public class Personaje {

    @Id                              // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremento
    private Long id;

    @Column(nullable = false)        // No puede ser null
    private String nombre;

    @Column(length = 100)            // Longitud máxima
    private String ocupacion;

    @Column(name = "frase_celebre")  // Nombre de columna personalizado
    private String fraseCelebre;

    private int edad;                // Sin anotación = configuración por defecto

    // Constructor vacío (OBLIGATORIO para JPA)
    public Personaje() {}

    // Constructor con parámetros
    public Personaje(String nombre, String ocupacion, String fraseCelebre, int edad) {
        this.nombre = nombre;
        this.ocupacion = ocupacion;
        this.fraseCelebre = fraseCelebre;
        this.edad = edad;
    }

    // Getters y Setters (todos necesarios)
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

### Anotaciones JPA importantes

| Anotación | Propósito |
|-----------|-----------|
| `@Entity` | Marca la clase como entidad JPA |
| `@Table` | Configura la tabla (nombre, índices...) |
| `@Id` | Marca el campo como clave primaria |
| `@GeneratedValue` | El valor se genera automáticamente |
| `@Column` | Configura la columna (nombre, nullable, length...) |

### Estrategias de generación de ID

| Estrategia | Descripción |
|------------|-------------|
| `IDENTITY` | Auto-incremento de la BD (recomendado para H2, MySQL) |
| `SEQUENCE` | Usa secuencias (recomendado para PostgreSQL, Oracle) |
| `AUTO` | JPA elige automáticamente |

---

## Crear el Repositorio con Spring Data JPA

Aquí viene la magia. El repositorio ahora es una **interfaz**:

```java
package com.simpsons.api.repository;

import com.simpsons.api.model.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Long> {

    // ¡Spring implementa automáticamente los métodos CRUD básicos!

    // Métodos personalizados (Spring los implementa según el nombre)
    List<Personaje> findByNombre(String nombre);
    List<Personaje> findByNombreContainingIgnoreCase(String nombre);
    List<Personaje> findByEdadGreaterThan(int edad);
    List<Personaje> findByOcupacion(String ocupacion);
}
```

### Métodos que vienen GRATIS

Al extender `JpaRepository<Personaje, Long>`:

| Método | Descripción |
|--------|-------------|
| `findAll()` | Obtener todos |
| `findById(Long id)` | Buscar por ID (devuelve Optional) |
| `save(Personaje p)` | Guardar (crear o actualizar) |
| `deleteById(Long id)` | Eliminar por ID |
| `count()` | Contar registros |
| `existsById(Long id)` | Verificar si existe |

### Query Methods: Magia de nombres

Spring genera consultas SQL automáticamente según el nombre del método:

| Nombre del método | SQL generado |
|-------------------|--------------|
| `findByNombre(String nombre)` | `WHERE nombre = ?` |
| `findByNombreContaining(String s)` | `WHERE nombre LIKE %?%` |
| `findByNombreContainingIgnoreCase(String s)` | `WHERE LOWER(nombre) LIKE LOWER(%?%)` |
| `findByEdadGreaterThan(int edad)` | `WHERE edad > ?` |
| `findByEdadBetween(int min, int max)` | `WHERE edad BETWEEN ? AND ?` |
| `findByNombreAndOcupacion(...)` | `WHERE nombre = ? AND ocupacion = ?` |
| `findByNombreOrOcupacion(...)` | `WHERE nombre = ? OR ocupacion = ?` |
| `findByNombreOrderByEdadDesc(...)` | `ORDER BY edad DESC` |

### Palabras clave para Query Methods

| Palabra | Significado |
|---------|-------------|
| `And` | Y lógico |
| `Or` | O lógico |
| `Is`, `Equals` | Igualdad |
| `Between` | Entre dos valores |
| `LessThan`, `GreaterThan` | Comparaciones |
| `Like` | Patrón SQL |
| `Containing` | Contiene (LIKE %valor%) |
| `StartingWith`, `EndingWith` | Empieza/termina con |
| `IgnoreCase` | Sin distinguir mayúsculas |
| `OrderBy` | Ordenar resultados |

---

## Actualizar el Servicio

El servicio cambia ligeramente para usar el nuevo repositorio:

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
        return repository.save(personaje);
    }

    public Optional<Personaje> actualizar(Long id, Personaje datosNuevos) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNombre(datosNuevos.getNombre());
                    existente.setOcupacion(datosNuevos.getOcupacion());
                    existente.setFraseCelebre(datosNuevos.getFraseCelebre());
                    existente.setEdad(datosNuevos.getEdad());
                    return repository.save(existente);
                });
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Personaje> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Personaje> buscarMayoresDe(int edad) {
        return repository.findByEdadGreaterThan(edad);
    }
}
```

---

## Cargar datos iniciales

Para tener datos al arrancar, crea un archivo `data.sql` en `src/main/resources/`:

```sql
INSERT INTO personajes (nombre, ocupacion, frase_celebre, edad)
VALUES ('Homer Simpson', 'Inspector de seguridad', '¡D''oh!', 39);

INSERT INTO personajes (nombre, ocupacion, frase_celebre, edad)
VALUES ('Marge Simpson', 'Ama de casa', 'Mmmmm...', 36);

INSERT INTO personajes (nombre, ocupacion, frase_celebre, edad)
VALUES ('Bart Simpson', 'Estudiante', '¡Ay, caramba!', 10);

INSERT INTO personajes (nombre, ocupacion, frase_celebre, edad)
VALUES ('Lisa Simpson', 'Estudiante', 'Si alguien quiere encontrarme estaré en mi habitación', 8);

INSERT INTO personajes (nombre, ocupacion, frase_celebre, edad)
VALUES ('Maggie Simpson', 'Bebé', '*chupa chupete*', 1);
```

O usar Java con `CommandLineRunner`:

```java
package com.simpsons.api;

import com.simpsons.api.model.Personaje;
import com.simpsons.api.repository.PersonajeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(PersonajeRepository repository) {
        return args -> {
            repository.save(new Personaje("Homer Simpson",
                "Inspector de seguridad", "¡D'oh!", 39));
            repository.save(new Personaje("Marge Simpson",
                "Ama de casa", "Mmmmm...", 36));
            repository.save(new Personaje("Bart Simpson",
                "Estudiante", "¡Ay, caramba!", 10));
            repository.save(new Personaje("Lisa Simpson",
                "Estudiante", "Si alguien quiere encontrarme...", 8));
            repository.save(new Personaje("Maggie Simpson",
                "Bebé", "*chupa chupete*", 1));

            System.out.println("Datos cargados: " + repository.count() + " personajes");
        };
    }
}
```

---

## Validaciones

Spring Boot incluye validación con anotaciones:

### 1. Añadir dependencia

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. Añadir anotaciones al modelo

```java
import jakarta.validation.constraints.*;

@Entity
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "La ocupación no puede superar 100 caracteres")
    private String ocupacion;

    private String fraseCelebre;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 150, message = "La edad no puede superar 150")
    private int edad;

    // ...
}
```

### 3. Activar validación en el controlador

```java
@PostMapping
public ResponseEntity<Personaje> crear(@Valid @RequestBody Personaje personaje) {
    Personaje nuevo = service.crear(personaje);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
}
```

### Anotaciones de validación comunes

| Anotación | Uso |
|-----------|-----|
| `@NotNull` | No puede ser null |
| `@NotBlank` | No puede ser null, vacío, ni solo espacios |
| `@NotEmpty` | No puede ser null ni vacío |
| `@Size(min, max)` | Longitud entre min y max |
| `@Min(value)` | Valor mínimo numérico |
| `@Max(value)` | Valor máximo numérico |
| `@Email` | Debe ser email válido |
| `@Pattern(regexp)` | Debe coincidir con expresión regular |

---

## Manejo de errores

### Crear excepciones personalizadas

```java
package com.simpsons.api.exception;

public class PersonajeNotFoundException extends RuntimeException {
    public PersonajeNotFoundException(Long id) {
        super("No se encontró personaje con id: " + id);
    }
}
```

### Controlador de excepciones global

```java
package com.simpsons.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonajeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(PersonajeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

Ahora si envías datos inválidos:

```json
POST /api/personajes
{
    "nombre": "",
    "edad": -5
}
```

Recibes:
```json
{
    "nombre": "El nombre es obligatorio",
    "edad": "La edad no puede ser negativa"
}
```

---

## Resumen

| Concepto | Descripción |
|----------|-------------|
| **JPA** | Especificación para mapear objetos a BD |
| **Hibernate** | Implementación de JPA |
| **Spring Data JPA** | Simplifica JPA con repositorios automáticos |
| **@Entity** | Marca clase como entidad de BD |
| **@Id** | Marca la clave primaria |
| **JpaRepository** | Interfaz con CRUD automático |
| **Query Methods** | Métodos que generan SQL por su nombre |
| **@Valid** | Activa validación de datos |
| **@RestControllerAdvice** | Manejo global de excepciones |

---

## Siguiente clase

Aprenderemos a consumir APIs externas y veremos conceptos de concurrencia en APIs REST.
