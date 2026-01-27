# Clase 1: Fundamentos de HTTP y API REST

## ¿Qué es una API?

**API** (Application Programming Interface) es un conjunto de reglas que permite que dos aplicaciones se comuniquen entre sí.

### Analogía: El camarero del restaurante

Imagina un restaurante:
- **Tú** (cliente) quieres pedir comida
- **La cocina** (servidor) prepara la comida
- **El camarero** (API) lleva tu pedido a la cocina y te trae la respuesta

```
[Cliente] <---> [API/Camarero] <---> [Servidor/Cocina]
```

Tú no necesitas saber cómo funciona la cocina. Solo necesitas saber cómo hablar con el camarero.

---

## ¿Qué es REST?

**REST** (Representational State Transfer) es un estilo arquitectónico para diseñar APIs web.

Una API que sigue los principios REST se llama **API RESTful**.

### Principios básicos de REST

1. **Cliente-Servidor**: Separación clara entre quien pide (cliente) y quien responde (servidor)
2. **Sin estado (Stateless)**: Cada petición es independiente. El servidor no "recuerda" peticiones anteriores
3. **Recursos identificables**: Todo se identifica con URLs únicas
4. **Operaciones estándar**: Usamos verbos HTTP (GET, POST, PUT, DELETE)

---

## El protocolo HTTP

HTTP (HyperText Transfer Protocol) es el protocolo de comunicación de la web.

### Anatomía de una petición HTTP

```
[MÉTODO] [URL] HTTP/1.1
[Cabeceras]

[Cuerpo (opcional)]
```

**Ejemplo real:**
```http
GET /personajes/1 HTTP/1.1
Host: api.simpsons.com
Accept: application/json
```

### Anatomía de una respuesta HTTP

```
HTTP/1.1 [CÓDIGO DE ESTADO] [MENSAJE]
[Cabeceras]

[Cuerpo]
```

**Ejemplo real:**
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "nombre": "Homer Simpson",
    "ocupacion": "Inspector de seguridad"
}
```

---

## Métodos HTTP (Verbos)

| Método | Acción | ¿Cuerpo? | Ejemplo |
|--------|--------|----------|---------|
| **GET** | Obtener datos | No | Obtener lista de personajes |
| **POST** | Crear nuevo recurso | Sí | Crear un nuevo personaje |
| **PUT** | Actualizar recurso completo | Sí | Modificar todos los datos de un personaje |
| **PATCH** | Actualizar parcialmente | Sí | Cambiar solo el nombre |
| **DELETE** | Eliminar recurso | No | Borrar un personaje |

### Ejemplo práctico: API de Los Simpson

| Operación | Método | URL |
|-----------|--------|-----|
| Obtener todos los personajes | GET | `/personajes` |
| Obtener un personaje | GET | `/personajes/1` |
| Crear personaje | POST | `/personajes` |
| Actualizar personaje | PUT | `/personajes/1` |
| Eliminar personaje | DELETE | `/personajes/1` |

---

## Códigos de estado HTTP

Los códigos de estado indican el resultado de la petición.

### Familias de códigos

| Rango | Significado | Ejemplo |
|-------|-------------|---------|
| **1xx** | Información | 100 Continue |
| **2xx** | Éxito | 200 OK, 201 Created |
| **3xx** | Redirección | 301 Moved Permanently |
| **4xx** | Error del cliente | 404 Not Found, 400 Bad Request |
| **5xx** | Error del servidor | 500 Internal Server Error |

### Códigos que usarás constantemente

| Código | Nombre | Cuándo se usa |
|--------|--------|---------------|
| **200** | OK | GET exitoso |
| **201** | Created | POST exitoso (recurso creado) |
| **204** | No Content | DELETE exitoso |
| **400** | Bad Request | Datos enviados incorrectos |
| **404** | Not Found | Recurso no existe |
| **500** | Internal Server Error | Algo falló en el servidor |

---

## JSON: El lenguaje de las APIs

**JSON** (JavaScript Object Notation) es el formato más común para intercambiar datos en APIs REST.

### Sintaxis básica

```json
{
    "nombre": "Homer Simpson",
    "edad": 39,
    "casado": true,
    "hijos": ["Bart", "Lisa", "Maggie"],
    "trabajo": {
        "empresa": "Central Nuclear de Springfield",
        "puesto": "Inspector de seguridad"
    }
}
```

### Reglas de JSON

1. Las claves siempre van entre comillas dobles `"clave"`
2. Los strings van entre comillas dobles `"valor"`
3. Los números van sin comillas `39`
4. Booleanos: `true` o `false` (minúsculas)
5. Arrays: `["elemento1", "elemento2"]`
6. Objetos anidados: `{ "clave": { "subclave": "valor" } }`
7. Null: `null`

---

## URLs y Recursos

En REST, todo es un **recurso** identificado por una **URL**.

### Buenas prácticas para URLs

```
✅ BIEN                          ❌ MAL
/personajes                      /getPersonajes
/personajes/1                    /personaje?id=1
/personajes/1/frases             /getFrasesDePersonaje
```

### Reglas básicas

1. **Usar sustantivos**, no verbos (el verbo es el método HTTP)
2. **Plural** para colecciones: `/personajes`, no `/personaje`
3. **Minúsculas** y separar con guiones: `/frases-famosas`
4. **Jerarquía lógica**: `/personajes/1/frases` (frases del personaje 1)

---

## Herramientas para probar APIs

### 1. Navegador (solo GET)
Simplemente escribe la URL en el navegador.

### 2. Postman (recomendado)
Aplicación gráfica para probar cualquier tipo de petición.

### 3. cURL (línea de comandos)
```bash
curl -X GET https://api.ejemplo.com/personajes
```

### 4. IntelliJ HTTP Client
IntelliJ tiene un cliente HTTP integrado. Crea un archivo `.http`:
```http
### Obtener personajes
GET https://api.ejemplo.com/personajes

### Crear personaje
POST https://api.ejemplo.com/personajes
Content-Type: application/json

{
    "nombre": "Bart Simpson"
}
```

---

## Práctica: Explorando APIs reales

Vamos a explorar The Simpsons Quote API:

**URL base:** `https://thesimpsonsquoteapi.glitch.me/`

### Endpoints disponibles

| Endpoint | Descripción |
|----------|-------------|
| `/quotes` | Una frase aleatoria |
| `/quotes?count=5` | 5 frases aleatorias |
| `/quotes?character=homer` | Frases de Homer |

### Prueba en el navegador

Abre: `https://thesimpsonsquoteapi.glitch.me/quotes`

Verás algo como:
```json
[
    {
        "quote": "D'oh!",
        "character": "Homer Simpson",
        "image": "https://cdn.glitch.com/...",
        "characterDirection": "Right"
    }
]
```

---

## Resumen

| Concepto | Descripción |
|----------|-------------|
| **API** | Interfaz para comunicar aplicaciones |
| **REST** | Estilo arquitectónico para APIs web |
| **HTTP** | Protocolo de comunicación |
| **Métodos** | GET, POST, PUT, DELETE |
| **Códigos de estado** | 200 OK, 404 Not Found, etc. |
| **JSON** | Formato de datos |
| **Recurso** | Cualquier cosa identificable por URL |

---

## Siguiente clase

Crearemos nuestra primera API REST con Spring Boot.
