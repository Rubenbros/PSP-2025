# Lección 1 — Ejercicio: Procesos e Hilos

Instrucciones
- Responde editando este mismo archivo (.md). No borres el enunciado.
- Marca con [x] las opciones correctas. Puede haber más de una.
- Apóyate en la teoría: teoría/1-Procesos-e-Hilos.md

---

## Ejercicio 1 — ¿Qué responsabilidades abarca un proceso?
Piensa en la analogía de la pizzería. Un "proceso" es como la tienda: proporciona el entorno y los recursos para que el trabajo (hilos) ocurra. Selecciona una o más opciones correctas.

[ ] Organizar el acceso a recursos compartidos y escasos (por ejemplo, ingredientes compartidos entre trabajadores).

[ ] Realizar por sí mismo todo el trabajo operativo (por ejemplo, amasar y hornear pizzas sin ayuda de trabajadores).

[ ] Organizar el entorno de ejecución para sus hilos/trabajadores (cocina, hornos, reglas, memoria y recursos compartidos).

[ ] Proveer soporte a la comunicación y coordinación entre hilos/trabajadores (canales, colas, señales, protocolos dentro del proceso).

Pista: Un proceso es el contenedor y el organizador; los hilos son quienes ejecutan el trabajo.

---

## Ejercicio 2 — Concurrencia interna (mismo hilo)
¿Cuáles de las siguientes tareas pueden ejecutarse de forma concurrente dentro del mismo hilo? Selecciona una o más opciones correctas y marca con [x].

Tip: Piensa en recursos compartidos. Si no se necesitan o no hay que compartirlos, entonces las tareas pueden progresar de forma concurrente dentro del mismo hilo.

Pista rápida: ¡Puedes hablar mientras caminas! ;-)


[ ] Cocinar en dos pizzerías distintas al mismo tiempo.

[ ] Cobrar a dos clientes por la misma porción de pizza.

[ ] Cortar la pizza y decirle al cocinero que prepare una de pepperoni más.

[ ] Atender a clientes y chatear con amigos por teléfono.

---
