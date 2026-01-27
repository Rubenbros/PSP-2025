# GUIÓN CLASE 1 - Para el profesor
## Fundamentos HTTP + Primer proyecto Spring Boot

**Duración total:** 2 horas

---

## Preparación antes de clase

### Material necesario
- [ ] Proyector funcionando
- [ ] Spring Initializr abierto en el navegador (start.spring.io)
- [ ] Postman instalado (o IntelliJ HTTP Client)
- [ ] Navegador con The Simpsons Quote API abierta
- [ ] Este guión impreso o en segunda pantalla

### Verificar con alumnos
- [ ] Tienen Java 17+ instalado
- [ ] Tienen IntelliJ instalado
- [ ] Tienen conexión a internet

---

## Estructura de la clase

| Tiempo | Bloque | Actividad |
|--------|--------|-----------|
| 0:00-0:20 | Teoría HTTP | Explicación con ejemplos visuales |
| 0:20-0:35 | Demo APIs | Explorar Simpsons API en navegador |
| 0:35-0:55 | Crear proyecto | Spring Initializr paso a paso |
| 0:55-1:20 | Código en vivo | Primer controlador |
| 1:20-1:45 | Práctica guiada | Alumnos replican + ejercicios |
| 1:45-2:00 | Cierre | Resolución dudas, entrega ejercicios |

---

## BLOQUE 1: Teoría HTTP (20 min)

### Puntos clave a explicar

**1. Analogía del restaurante (5 min)**
- Cliente = tu app/navegador
- Camarero = API
- Cocina = servidor/base de datos
- Menú = documentación de la API

**Pregunta al aire:** "¿Qué pasa si pides algo que no está en el menú?"
→ Error 404, el recurso no existe

**2. Métodos HTTP (5 min)**
- Dibuja en la pizarra:
```
GET    → "Dame información"      → Leer
POST   → "Crea esto nuevo"       → Crear
PUT    → "Actualiza todo esto"   → Actualizar
DELETE → "Borra esto"            → Eliminar
```

**Truco mnemotécnico:** CRUD = Create, Read, Update, Delete → POST, GET, PUT, DELETE

**3. Códigos de estado (5 min)**
- 2XX = Todo bien ✅
- 4XX = Tú la has cagado ❌ (cliente)
- 5XX = Yo la he cagado ❌ (servidor)

**Ejemplos que recuerdan:**
- 404 = "Página no encontrada" (todos lo han visto)
- 500 = "Error interno" (cuando una web se cae)

**4. JSON rápido (5 min)**
- Es como un diccionario de Python o un objeto de JavaScript
- Claves entre comillas, valores según tipo
- Mostrar ejemplo en la pizarra

---

## BLOQUE 2: Demo APIs reales (15 min)

### Explorar The Simpsons Quote API

Abre en el navegador: `https://thesimpsonsquoteapi.glitch.me/quotes`

**Haz esto delante de ellos:**
1. Recarga varias veces → "Veis que cambia? Es aleatoria"
2. Añade `?count=3` → "Parámetros de query"
3. Añade `?character=homer` → "Filtrando por personaje"
4. Prueba `?character=asdfasdf` → "¿Qué pasa si no existe?"

**Pregúntales:**
- "¿Qué estructura veis?" (array de objetos)
- "¿Qué campos tiene cada frase?"

### Si tienes tiempo, muestra otra API
- `https://pokeapi.co/api/v2/pokemon/pikachu`
- Les suele gustar porque conocen Pokémon

---

## BLOQUE 3: Crear proyecto Spring Boot (20 min)

### Paso a paso en Spring Initializr

**IMPORTANTE:** Hazlo TÚ primero en tu pantalla, luego ellos replican.

1. Ve a start.spring.io
2. Configura:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.2.x (la última estable)
   - Group: `com.springfield`
   - Artifact: `hola-springfield`
   - Packaging: Jar
   - Java: 17

3. Añade dependencia: **Spring Web**
   - "¿Por qué solo esta? Porque es lo mínimo para hacer una API"

4. Click en Generate
5. Descomprime
6. **En IntelliJ:** File → Open → selecciona la carpeta

### Mientras Maven descarga dependencias (puede tardar)

Explica la estructura:
```
src/main/java/      → Tu código
src/main/resources/ → Configuración
pom.xml             → Dependencias (como package.json en JS)
```

**Errores comunes aquí:**
- "No me descarga las dependencias" → Verificar conexión a internet
- "Sale en rojo" → Maven → Reload Project (icono de refresh)
- "No encuentra Java" → Verificar JDK configurado en IntelliJ

---

## BLOQUE 4: Código en vivo (25 min)

### Primer controlador

**Escribe esto paso a paso, explicando cada línea:**

```java
package com.springfield.holaspringfield.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // "Esto le dice a Spring: soy un controlador REST"
public class HolaController {

    @GetMapping("/hola")  // "Cuando alguien haga GET a /hola..."
    public String saludar() {
        return "¡Hola Springfield!";  // "...devuelve este texto"
    }
}
```

### Ejecutar y probar

1. Click derecho en `*Application.java` → Run
2. Espera a ver `Tomcat started on port(s): 8080`
3. Abre navegador: `http://localhost:8080/hola`
4. **¡MOMENTO WOW!** Celebra cuando funcione

### Ampliar el controlador

```java
// Añade esto al mismo controlador
@GetMapping("/saludo/{nombre}")
public String saludoPersonal(@PathVariable String nombre) {
    return "¡Hola " + nombre + "! Bienvenido a Springfield";
}
```

Prueba: `http://localhost:8080/saludo/Homer`

### Devolver JSON (si da tiempo)

```java
// Nueva clase Personaje.java
public class Personaje {
    private Long id;
    private String nombre;

    // Constructor, getters, setters...
}

// En el controlador
@GetMapping("/personaje")
public Personaje getPersonaje() {
    return new Personaje(1L, "Homer Simpson");
}
```

**Explica:** "Spring convierte automáticamente el objeto a JSON"

---

## BLOQUE 5: Práctica guiada (25 min)

### Que repliquen lo que has hecho

1. Crea el proyecto (5 min)
2. Escribe el controlador básico (5 min)
3. Ejecuta y prueba (5 min)

### Ejercicios para entregar (10 min)

Pídeles que hagan el **Ejercicio 1.5 y 1.6** de los ejercicios de clase.

**Pasa por las mesas** mientras trabajan. Errores típicos:
- Olvidar `@RestController`
- Olvidar `@GetMapping`
- No tener getters en el modelo (devuelve `{}` vacío)
- Puerto ocupado (cambiar en application.properties)

---

## BLOQUE 6: Cierre (15 min)

### Recogida de ejercicios
- Que te muestren en pantalla los endpoints funcionando
- Apunta quién ha completado qué (0.5 puntos)

### Resumen de lo aprendido
1. HTTP: métodos y códigos
2. REST: recursos y URLs
3. JSON: formato de datos
4. Spring Boot: crear proyecto y primer controlador

### Avance de la próxima clase
"La próxima clase crearemos una API completa con todas las operaciones CRUD"

### Tarea para casa (opcional)
- Leer la teoría de la Clase 2
- Explorar más la Simpsons API

---

## Errores comunes y soluciones

| Error | Causa | Solución |
|-------|-------|----------|
| Puerto 8080 ocupado | Otra app usa el puerto | `server.port=8081` en application.properties |
| No encuentra Java | JDK no configurado | File → Project Structure → SDK |
| Dependencias en rojo | Maven no descargó | Click derecho pom.xml → Maven → Reload |
| 404 al acceder | URL incorrecta o falta anotación | Verificar @GetMapping y URL |
| JSON vacío `{}` | Faltan getters | Añadir getters a la clase |
| No arranca | Otro error en el código | Leer el stacktrace completo |

---

## Notas post-clase

Después de la clase, anota:
- ¿Cuántos completaron los ejercicios?
- ¿Qué errores fueron más comunes?
- ¿Algún alumno con dificultades especiales?
- ¿Algo que mejorar para la siguiente clase?
