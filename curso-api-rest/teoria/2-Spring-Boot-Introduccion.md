# Clase 1 (Parte 2): Introducción a Spring Boot

## ¿Qué es Spring?

**Spring** es un framework de Java que facilita el desarrollo de aplicaciones empresariales.

**Spring Boot** es una extensión de Spring que simplifica la configuración y permite crear aplicaciones rápidamente.

### ¿Por qué Spring Boot?

| Sin Spring Boot | Con Spring Boot |
|-----------------|-----------------|
| Configurar servidor web manualmente | Servidor embebido (Tomcat) |
| Decenas de archivos XML | Configuración automática |
| Gestionar dependencias una a una | "Starters" con todo incluido |
| Horas de configuración | Aplicación funcionando en minutos |

---

## ¿Qué es Maven?

**Maven** es una herramienta de gestión de proyectos Java que:

1. **Gestiona dependencias**: Descarga automáticamente las librerías que necesitas
2. **Compila el código**: Convierte `.java` en `.class`
3. **Ejecuta tests**: Lanza las pruebas automáticas
4. **Empaqueta la aplicación**: Crea un `.jar` ejecutable

### El archivo `pom.xml`

Es el corazón de Maven. Define:
- Información del proyecto
- Dependencias (librerías)
- Plugins
- Configuración de compilación

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <groupId>com.simpsons</groupId>      <!-- Identificador de tu organización -->
    <artifactId>api-simpsons</artifactId> <!-- Nombre del proyecto -->
    <version>1.0.0</version>              <!-- Versión -->

    <dependencies>
        <!-- Aquí van las librerías que necesitas -->
    </dependencies>
</project>
```

### Dependencias

Una **dependencia** es una librería externa que tu proyecto necesita.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Maven descarga automáticamente esta librería (y todas las que ella necesite).

---

## Crear un proyecto Spring Boot

### Opción 1: Spring Initializr (recomendada)

1. Ve a [start.spring.io](https://start.spring.io/)
2. Configura:
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.2.x (la versión estable más reciente)
   - **Group**: `com.tuempresa`
   - **Artifact**: `nombre-proyecto`
   - **Packaging**: Jar
   - **Java**: 17
3. Añade dependencia: **Spring Web**
4. Click en "Generate"
5. Descomprime y abre en IntelliJ

### Opción 2: Desde IntelliJ

1. File → New → Project
2. Selecciona "Spring Initializr"
3. Configura igual que arriba

---

## Estructura de un proyecto Spring Boot

```
mi-proyecto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ejemplo/miproyecto/
│   │   │       └── MiProyectoApplication.java    ← Clase principal
│   │   └── resources/
│   │       ├── application.properties            ← Configuración
│   │       ├── static/                           ← Archivos estáticos (CSS, JS)
│   │       └── templates/                        ← Plantillas HTML
│   └── test/
│       └── java/                                 ← Tests
├── pom.xml                                       ← Configuración Maven
└── README.md
```

### Archivos importantes

| Archivo | Propósito |
|---------|-----------|
| `pom.xml` | Dependencias y configuración de Maven |
| `*Application.java` | Punto de entrada de la aplicación |
| `application.properties` | Configuración de la aplicación |

---

## La clase principal

```java
package com.simpsons.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiSimpsonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSimpsonsApplication.class, args);
    }
}
```

### ¿Qué hace `@SpringBootApplication`?

Es una anotación que combina tres cosas:

1. `@Configuration`: Esta clase puede definir beans (objetos gestionados por Spring)
2. `@EnableAutoConfiguration`: Configura automáticamente según las dependencias
3. `@ComponentScan`: Busca componentes en este paquete y subpaquetes

---

## Tu primer controlador REST

Un **controlador** es una clase que maneja las peticiones HTTP.

```java
package com.simpsons.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundoController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Hola Springfield!";
    }
}
```

### Anotaciones clave

| Anotación | Significado |
|-----------|-------------|
| `@RestController` | Esta clase es un controlador REST |
| `@GetMapping("/ruta")` | Este método responde a peticiones GET en /ruta |

### Ejecutar y probar

1. Ejecuta la clase `*Application.java` (click derecho → Run)
2. Abre el navegador: `http://localhost:8080/hola`
3. Verás: `¡Hola Springfield!`

---

## Devolviendo JSON

Spring convierte automáticamente objetos Java a JSON.

### Paso 1: Crear una clase modelo

```java
package com.simpsons.api.model;

public class Personaje {
    private Long id;
    private String nombre;
    private String ocupacion;

    // Constructor vacío (necesario para Spring)
    public Personaje() {}

    // Constructor con parámetros
    public Personaje(Long id, String nombre, String ocupacion) {
        this.id = id;
        this.nombre = nombre;
        this.ocupacion = ocupacion;
    }

    // Getters y Setters (necesarios para la conversión a JSON)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
}
```

### Paso 2: Devolver el objeto desde el controlador

```java
@RestController
public class PersonajeController {

    @GetMapping("/personaje")
    public Personaje obtenerPersonaje() {
        return new Personaje(1L, "Homer Simpson", "Inspector de seguridad");
    }
}
```

### Resultado

Al acceder a `http://localhost:8080/personaje`:

```json
{
    "id": 1,
    "nombre": "Homer Simpson",
    "ocupacion": "Inspector de seguridad"
}
```

Spring convierte automáticamente el objeto `Personaje` a JSON gracias a la librería **Jackson** (incluida en `spring-boot-starter-web`).

---

## Devolviendo listas

```java
@GetMapping("/personajes")
public List<Personaje> obtenerPersonajes() {
    List<Personaje> personajes = new ArrayList<>();
    personajes.add(new Personaje(1L, "Homer Simpson", "Inspector de seguridad"));
    personajes.add(new Personaje(2L, "Marge Simpson", "Ama de casa"));
    personajes.add(new Personaje(3L, "Bart Simpson", "Estudiante"));
    return personajes;
}
```

Resultado:
```json
[
    {"id": 1, "nombre": "Homer Simpson", "ocupacion": "Inspector de seguridad"},
    {"id": 2, "nombre": "Marge Simpson", "ocupacion": "Ama de casa"},
    {"id": 3, "nombre": "Bart Simpson", "ocupacion": "Estudiante"}
]
```

---

## Parámetros en la URL

### Path Variables (parte de la ruta)

```java
@GetMapping("/personajes/{id}")
public Personaje obtenerPorId(@PathVariable Long id) {
    // Buscar personaje por id...
    return new Personaje(id, "Homer Simpson", "Inspector de seguridad");
}
```

URL: `http://localhost:8080/personajes/1`

### Query Parameters (después del ?)

```java
@GetMapping("/personajes/buscar")
public List<Personaje> buscar(@RequestParam String nombre) {
    // Buscar personajes que contengan ese nombre...
    return new ArrayList<>();
}
```

URL: `http://localhost:8080/personajes/buscar?nombre=Homer`

### Parámetro opcional con valor por defecto

```java
@GetMapping("/personajes")
public List<Personaje> listar(
        @RequestParam(defaultValue = "10") int limite) {
    // Devolver hasta 'limite' personajes
    return new ArrayList<>();
}
```

---

## Archivo application.properties

Configuración básica de la aplicación:

```properties
# Puerto del servidor (por defecto 8080)
server.port=8080

# Nombre de la aplicación
spring.application.name=api-simpsons

# Mostrar SQL en consola (útil para debug)
# spring.jpa.show-sql=true
```

---

## Ejecutar la aplicación

### Desde IntelliJ
1. Abre la clase `*Application.java`
2. Click en el botón verde ▶️ o click derecho → Run

### Desde terminal
```bash
./mvnw spring-boot:run
```

### Verificar que funciona

Busca en la consola:
```
Tomcat started on port(s): 8080 (http)
Started ApiSimpsonsApplication in 2.5 seconds
```

---

## Errores comunes

### "Port 8080 already in use"
Otra aplicación usa el puerto 8080.
- Solución 1: Cierra la otra aplicación
- Solución 2: Cambia el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

### "Cannot resolve symbol..."
Falta importar la clase.
- Solución: Alt+Enter sobre el error → Import class

### El endpoint devuelve 404
- Verifica que la clase tiene `@RestController`
- Verifica que el método tiene `@GetMapping`
- Verifica la URL (mayúsculas/minúsculas importan)

### Devuelve un objeto vacío `{}`
- Verifica que la clase modelo tiene getters

---

## Resumen

| Concepto | Descripción |
|----------|-------------|
| **Spring Boot** | Framework para crear aplicaciones Java rápidamente |
| **Maven** | Gestor de dependencias y builds |
| **pom.xml** | Archivo de configuración de Maven |
| **@SpringBootApplication** | Marca la clase principal |
| **@RestController** | Marca una clase como controlador REST |
| **@GetMapping** | Marca un método que responde a GET |
| **@PathVariable** | Captura valores de la URL |
| **@RequestParam** | Captura parámetros de query |

---

## Siguiente clase

Crearemos una API completa con operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
