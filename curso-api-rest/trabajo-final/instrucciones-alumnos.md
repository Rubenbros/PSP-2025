# Examen Practico - Springfield Evaluator

## Introduccion

Este examen consiste en **10 tareas** que deberas completar de forma secuencial. Cada tarea vale **1 punto**, sumando un total de **10 puntos**.

- **Tareas 1-3**: Tu haces peticiones HTTP al servidor del profesor
- **Tarea 4**: Configuras tu servidor y lo expones con ngrok
- **Tareas 5-8**: Se evaluan automaticamente cuando registras/actualizas tu servidor
- **Tareas 9-10**: Tareas avanzadas que combinan consumo y exposicion de APIs

> **Las tareas 5-10 se evaluan automaticamente** cada vez que registras tu URL con la Tarea 4. Tambien puedes lanzar la evaluacion manualmente en cualquier momento (ver seccion "Evaluar tus tareas").

---

## Requisitos Previos

- Java 17+
- Proyecto Spring Boot con `spring-boot-starter-web`
- Cuenta en [ngrok](https://ngrok.com) (a partir de la Tarea 4)

---

## URL Base

Sustituye `{BASE_URL}` por: **`https://springfield-evaluator.vercel.app`**

---

## Tareas 1-3: Peticiones al servidor del profesor

### Tarea 1 - Registro

Haz una peticion **GET** a `{BASE_URL}/api/tareas/1` pasando tu nombre y apellido como query parameter `nombre` (sin espacios ni tildes).

Guarda el `alumno_id` de la respuesta — lo necesitas para todo lo demas.

---

### Tarea 2 - Datos personales

Envia una peticion **POST** a `{BASE_URL}/api/tareas/2` con el siguiente body JSON:

```json
{
  "alumno_id": <tu_id>,
  "nombre": "...",
  "apellidos": "...",
  "email": "..."
}
```

---

### Tarea 3 - Filtrar y calcular

Esta tarea tiene dos pasos:

**Paso 1** — Haz un **GET** a `{BASE_URL}/api/tareas/3` pasando tu `alumno_id` como query parameter. Recibiras una lista de personajes de Springfield con nombre y edad.

**Paso 2** — Procesa los datos y envia el resultado con un **POST** al mismo endpoint:
- Filtra los personajes **mayores de 18 anios**
- Ordenalos **alfabeticamente por nombre**
- Calcula la **media de edad** de los filtrados

Body del POST:

```json
{
  "alumno_id": <tu_id>,
  "resultado": {
    "personajes_filtrados": [
      { "nombre": "...", "edad": ... },
      ...
    ],
    "media_edad": <numero>
  }
}
```

---

## Tarea 4: Exponer tu servidor

1. Implementa un endpoint **GET /ping** en tu servidor que devuelva `{"status": "ok"}`
2. Arranca tu servidor Spring Boot (puerto 8080)
3. Expone tu servidor con ngrok
4. Registra tu URL haciendo un **POST** a `{BASE_URL}/api/tareas/4`:

```json
{
  "alumno_id": <tu_id>,
  "url": "https://xxxx.ngrok-free.app"
}
```

El servidor del profesor llamara a tu `/ping` para verificar. Si el ping es exitoso, **automaticamente se evaluaran las tareas 5-10** y recibiras los resultados en la respuesta.

Manten ngrok activo durante todo el examen. Si reinicias ngrok la URL cambia y deberas volver a enviarla.

---

## Evaluar tus tareas

Puedes lanzar la evaluacion de tus tareas 5-10 en cualquier momento haciendo un **POST** a `{BASE_URL}/api/tareas/evaluar`:

```json
{"alumno_id": <tu_id>}
```

Si quieres evaluar una tarea concreta:

```json
{"alumno_id": <tu_id>, "tarea": 7}
```

La respuesta te dira que tareas has completado y cuales no, con mensajes de error para ayudarte a corregir.

---

## Tareas 5-8: Tu API REST

Los tests automaticos haran peticiones a tu servidor. Asegurate de que ngrok esta activo.

### Tarea 5 - Crear y leer personajes

Implementa estos endpoints:

| Metodo | Endpoint | Descripcion | Codigo |
|--------|----------|-------------|--------|
| POST | `/api/personajes` | Crear personaje | 201 |
| GET | `/api/personajes` | Listar todos | 200 |
| GET | `/api/personajes/{id}` | Obtener por id | 200 |

El body del POST sera un JSON con `nombre`, `edad` y `profesion`. La respuesta del POST debe incluir un campo `id`.

---

### Tarea 6 - CRUD completo

Anade estos endpoints al controlador anterior:

| Metodo | Endpoint | Descripcion | Codigo |
|--------|----------|-------------|--------|
| PUT | `/api/personajes/{id}` | Actualizar | 200 |
| DELETE | `/api/personajes/{id}` | Eliminar | 204 |

El test creara un personaje, lo actualizara, verificara el cambio, lo eliminara y comprobara que devuelve 404.

---

### Tarea 7 - Codigos HTTP exactos

Tu API debe devolver exactamente estos codigos:

| Situacion | Codigo esperado |
|-----------|-----------------|
| GET de un id que no existe | 404 |
| POST con datos validos | 201 |
| DELETE de un id existente | 204 |
| DELETE de un id que no existe | 404 |

---

### Tarea 8 - Busqueda y ordenacion

1. Implementa un endpoint de busqueda:

**GET /api/personajes/buscar?nombre=\<texto\>**

Debe devolver un array con los personajes cuyo nombre contenga el texto buscado (sin importar mayusculas/minusculas). Si no hay resultados, devuelve un array vacio.

2. Anade un parametro de ordenacion al listado de personajes:

**GET /api/personajes?ordenar=nombre** — devuelve todos los personajes ordenados alfabeticamente por nombre

**GET /api/personajes?ordenar=edad** — devuelve todos los personajes ordenados por edad de menor a mayor

---

## Tareas 9-10: Avanzadas

### Tarea 9 - Consumir API y exponer estadisticas

Tu servidor debe exponer un endpoint **GET /api/estadisticas**.

Cuando el evaluador llame a ese endpoint, tu servidor debe llamar a `{BASE_URL}/api/tareas/9/ciudadanos` (pasando tu `alumno_id` como query parameter) para obtener una lista de ciudadanos. Cada ciudadano tiene `nombre`, `edad` y `salario`.

Tu endpoint debe calcular y devolver:

```json
{
  "salario_medio": <media de todos los salarios>,
  "persona_mas_joven": "<nombre de la persona con menor edad>",
  "mayor_salario": "<nombre de la persona con mayor salario>"
}
```

---

### Tarea 10 - Flujo multi-paso

Esta tarea requiere que tu servidor haga varias peticiones al profesor y luego exponga un endpoint de verificacion.

**Paso 1** — Inicia el flujo haciendo un **POST** a `{BASE_URL}/api/tareas/10/iniciar`:

```json
{"alumno_id": <tu_id>}
```

Recibiras un `token` en la respuesta.

**Paso 2** — Haz un **GET** a `{BASE_URL}/api/tareas/10/productos` enviando el token en la cabecera `Authorization: Bearer <token>`. Recibiras una lista de productos con `nombre` y `precio`.

**Paso 3** — Procesa los productos: a los que tengan precio **mayor que 50**, aplica un **10% de descuento**. Los demas se quedan igual. Calcula el **total** sumando todos los precios finales.

**Paso 4** — Envia el resultado con un **POST** a `{BASE_URL}/api/tareas/10/resultado`:

```json
{
  "alumno_id": <tu_id>,
  "token": "<token>",
  "productos_procesados": [
    {
      "nombre": "...",
      "precio_original": ...,
      "precio_final": ...,
      "descuento_aplicado": true/false
    },
    ...
  ],
  "total": <total>
}
```

Recibiras un `codigo_verificacion` en la respuesta.

**Paso 5** — Expone un endpoint **GET /api/verificacion** en tu servidor que devuelva:

```json
{"codigo_verificacion": "SPR-XXXXXXXX"}
```

---

## Configuracion de ngrok

1. Crea cuenta en https://ngrok.com
2. Descarga e instala ngrok
3. Autenticate con tu authtoken
4. Expone tu servidor con ngrok apuntando al puerto 8080
5. Usa la URL HTTPS que aparece en la terminal

---

## Errores comunes

- **"alumno_id no encontrado"**: Asegurate de haberte registrado (Tarea 1) y usar el id correcto
- **"Campos obligatorios faltantes"**: Revisa que envias todos los campos y que el header `Content-Type: application/json` esta presente
- **No se puede conectar a tu servidor**: Verifica que ngrok y tu Spring Boot estan corriendo, y que la URL registrada es la actual
- **404 en tus endpoints**: Revisa que las rutas coinciden exactamente (`/api/personajes`, no `/personajes`)
- **Los datos desaparecen**: Si usas almacenamiento en memoria, se pierden al reiniciar. Re-ejecuta lo necesario

---

Buena suerte!
