# Trabajo Final: Springfield Entertainment System

## Información

- **Fecha de entrega:** 28 de febrero de 2025
- **Modalidad:** Individual

---

## Sistema de evaluación

| Componente | Puntuación | Fecha |
|------------|------------|-------|
| Ejercicios Clase 1 | 0.5 puntos | Durante la clase 1 |
| Ejercicios Clase 2 | 0.5 puntos | Durante la clase 2 |
| Ejercicios Clase 3 | 0.5 puntos | Durante la clase 3 |
| Ejercicios Clase 4 | 0.5 puntos | Durante la clase 4 |
| **Trabajo Final** | **8 puntos** | 28 de febrero |
| **TOTAL** | **10 puntos** | |

Los ejercicios de clase se entregan al final de cada sesión. El trabajo final integra todo lo aprendido.

---

## Contexto

El Sr. Burns quiere modernizar Springfield creando una plataforma de entretenimiento digital. Tu misión es desarrollar la API REST que gestionará este sistema.

**Elige UNA de las siguientes opciones:**

---

## Opción A: Springfield Cinema 🎬

Sistema de gestión para el cine de Springfield.

### Entidades requeridas

```
Pelicula
├── id
├── titulo
├── director
├── genero
├── duracionMinutos
├── clasificacion (TP, +7, +12, +16, +18)
├── sinopsis
└── enCartelera (boolean)

Sala
├── id
├── nombre
├── capacidad
└── tipo (normal, 3D, IMAX)

Sesion
├── id
├── peliculaId
├── salaId
├── fechaHora
├── precio
└── asientosDisponibles

Entrada
├── id
├── sesionId
├── nombreCliente
├── asiento
├── fechaCompra
└── precio
```

### Funcionalidades obligatorias

1. **CRUD completo** para Películas y Salas

2. **Gestión de Sesiones:**
   - Crear sesión (asignar película a sala en horario)
   - Listar sesiones de hoy
   - Listar sesiones de una película
   - No permitir solapamiento de sesiones en la misma sala

3. **Compra de entradas:**
   - Comprar entrada (debe restar asientos disponibles)
   - No permitir compra si no hay asientos
   - Validar que el asiento no esté ocupado
   - **Manejar concurrencia** (dos personas comprando a la vez)

4. **Consultas especiales:**
   - Películas en cartelera
   - Sesiones disponibles (con asientos libres)
   - Recaudación total de una película
   - Ocupación de una sesión (% de asientos vendidos)

5. **Integración con API externa:**
   - Al crear una película, obtener una frase aleatoria de Los Simpson y guardarla como "crítica de Homer"

---

## Opción B: Springfield Music Festival 🎸

Sistema de gestión para un festival de música.

### Entidades requeridas

```
Artista
├── id
├── nombre
├── genero (rock, pop, jazz, etc.)
├── descripcion
├── paisOrigen
└── imagenUrl

Escenario
├── id
├── nombre
├── capacidad
└── ubicacion

Actuacion
├── id
├── artistaId
├── escenarioId
├── fechaHora
├── duracionMinutos
└── precioExtra (si es VIP)

Entrada
├── id
├── tipo (general, VIP)
├── nombreAsistente
├── email
├── fechaCompra
├── precio
└── codigoQR (generado)
```

### Funcionalidades obligatorias

1. **CRUD completo** para Artistas y Escenarios

2. **Gestión de Actuaciones:**
   - Programar actuación
   - No permitir solapamiento en el mismo escenario
   - Listar actuaciones por día
   - Listar actuaciones de un artista

3. **Venta de entradas:**
   - Comprar entrada (general o VIP)
   - Límite de entradas generales (ej: 5000)
   - Límite de entradas VIP (ej: 500)
   - **Manejar concurrencia** en la venta
   - Generar código QR único (puede ser un UUID)

4. **Consultas especiales:**
   - Cartel del festival (todos los artistas y horarios)
   - Entradas vendidas por tipo
   - Recaudación total
   - Artistas por género

5. **Integración con API externa:**
   - Al crear un artista, buscar una frase de Los Simpson de un personaje relacionado con la música (ej: "otto" el conductor del bus rockero)

---

## Opción C: Springfield Quiz Game 🎯

Sistema de trivial/quiz sobre Los Simpson.

### Entidades requeridas

```
Pregunta
├── id
├── enunciado
├── opciones (4 opciones)
├── respuestaCorrecta
├── categoria (personajes, episodios, lugares, frases)
├── dificultad (facil, media, dificil)
└── puntos

Partida
├── id
├── nombreJugador
├── fechaInicio
├── fechaFin
├── puntuacionTotal
├── estado (en_curso, finalizada)
└── respuestas (lista de RespuestaPartida)

RespuestaPartida
├── id
├── partidaId
├── preguntaId
├── respuestaSeleccionada
├── correcta (boolean)
├── tiempoRespuesta (segundos)
└── puntosObtenidos

Ranking
├── id
├── nombreJugador
├── mejorPuntuacion
├── partidasJugadas
└── ultimaPartida
```

### Funcionalidades obligatorias

1. **CRUD de Preguntas:**
   - Crear, editar, eliminar preguntas
   - Listar por categoría y dificultad
   - Obtener pregunta aleatoria

2. **Gestión de Partidas:**
   - Iniciar nueva partida
   - Obtener siguiente pregunta (aleatoria, no repetida en la partida)
   - Responder pregunta (verificar si es correcta, sumar puntos)
   - Finalizar partida
   - Ver historial de partidas de un jugador

3. **Sistema de puntuación:**
   - Puntos según dificultad (fácil: 10, media: 20, difícil: 30)
   - Bonus por respuesta rápida (< 5 segundos: +50%)
   - Racha de aciertos: multiplicador x2 a partir de 3 seguidos

4. **Ranking global:**
   - Top 10 jugadores
   - Actualizar ranking al finalizar partida
   - **Manejar concurrencia** (dos jugadores terminando a la vez)

5. **Integración con API externa:**
   - Endpoint que devuelva una "pista del día" (frase aleatoria de la API de Simpsons)
   - Cachear la pista durante 24 horas

---

## Requisitos técnicos obligatorios

### Arquitectura
- [ ] Estructura en capas (Controller, Service, Repository)
- [ ] Uso de Spring Data JPA con base de datos H2
- [ ] Validación de datos con anotaciones (`@Valid`, `@NotBlank`, etc.)
- [ ] Manejo de errores con `@RestControllerAdvice`
- [ ] Uso correcto de códigos HTTP (200, 201, 204, 400, 404)

### Concurrencia
- [ ] Al menos una operación que requiera manejo de concurrencia
- [ ] Uso de `@Transactional` donde corresponda
- [ ] Uso de `@Version` o `synchronized` para evitar race conditions

### API externa
- [ ] Consumir The Simpsons Quote API con RestTemplate
- [ ] Manejar errores de la API externa (timeouts, API caída)

### Documentación
- [ ] README.md explicando cómo ejecutar el proyecto
- [ ] Archivo `.http` o colección de Postman con todos los endpoints
- [ ] Comentarios en código donde sea necesario

---

## Rúbrica de evaluación del Trabajo Final (8 puntos)

| Criterio | Puntos | Descripción |
|----------|--------|-------------|
| **Funcionalidad** | 3.0 | Todos los endpoints funcionan correctamente |
| **Arquitectura** | 1.5 | Separación correcta en capas, código limpio |
| **Concurrencia** | 1.5 | Manejo correcto de operaciones concurrentes |
| **API externa** | 1.0 | Integración correcta con Simpsons API |
| **Validaciones y errores** | 0.5 | Validación de datos, códigos HTTP correctos |
| **Documentación** | 0.5 | README, archivo de requests |

### Desglose detallado

**Funcionalidad (3.0 puntos)**
- CRUD completo de entidades principales: 1.0
- Operaciones específicas del dominio: 1.0
- Consultas especiales y filtros: 0.5
- Todo funciona sin errores: 0.5

**Arquitectura (1.5 puntos)**
- Separación correcta Controller/Service/Repository: 0.75
- Código limpio, organizado, nombres descriptivos: 0.75

**Concurrencia (1.5 puntos)**
- Identificación correcta del problema de concurrencia: 0.5
- Implementación correcta de la solución: 1.0

**API externa (1.0 punto)**
- Consumo correcto de Simpsons API: 0.5
- Manejo de errores (timeout, API caída): 0.5

**Validaciones y errores (0.5 puntos)**
- Validaciones en entidades: 0.25
- Códigos HTTP correctos y manejador de excepciones: 0.25

**Documentación (0.5 puntos)**
- README con instrucciones claras: 0.25
- Archivo de requests completo: 0.25

---

## Entrega

1. **Repositorio Git** (GitHub, GitLab, o similar)
   - Código fuente completo
   - README.md
   - Archivo de requests (.http o colección Postman)

2. **Formato de entrega:**
   - Enlace al repositorio
   - El repositorio debe ser público o dar acceso al profesor

3. **Fecha límite:** 28 de febrero de 2025, 23:59

---

## Penalizaciones

- Entrega con retraso: -10% por día
- No compila: -50%
- Plagio: 0 y expediente

---

## Consejos

1. **Empieza por lo básico**: Primero haz que funcione el CRUD, luego añade complejidad
2. **Prueba constantemente**: Usa Postman o el cliente HTTP de IntelliJ
3. **Commits frecuentes**: Haz commits pequeños y descriptivos
4. **No dejes todo para el final**: La concurrencia puede dar problemas, testa bien
5. **Pregunta dudas**: Mejor preguntar antes que entregar algo incompleto

---

## Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [The Simpsons Quote API](https://thesimpsonsquoteapi.glitch.me/)
- [HTTP Status Codes](https://developer.mozilla.org/es/docs/Web/HTTP/Status)
- [Postman](https://www.postman.com/)

---

¡Buena suerte! 🍩
