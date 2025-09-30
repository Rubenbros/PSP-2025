# Procesos e hilos: la pizzería como analogía

Imagina que llegas a un patio de comidas a la hora del almuerzo y ves una hilera de pizzerías. La misión de cada tienda es vender pizza y cada una cuenta con varias personas trabajadoras para lograrlo. El propósito de cada trabajador es vender pizza, pero no puede hacerlo sin el equipamiento que le proporciona la tienda. Del mismo modo, una pizzería no puede vender nada sin sus trabajadores. Es decir, debe haber al menos una persona trabajando en la pizzería para que funcione. En resumen, los trabajadores dependen del equipo del local para hacer su trabajo, y el local depende de esos trabajadores para funcionar.

Esto se parece a cómo un ordenador ejecuta aplicaciones y gestiona la multitarea y la ejecución en paralelo. Para profundizar, exploremos los conceptos de proceso e hilo, trazando paralelismos con la dinámica de una pizzería.

![fila de pizzerías en un patio de comidas](../recursos/shops.svg)

## Proceso

Un proceso es una unidad de ejecución autocontenida que incluye todo lo necesario para completar sus tareas. En pocas palabras, un proceso es el contenedor de sus hilos (threads), abarcando lo que necesitan para operar y sus recursos compartidos. Es más barato organizar el acceso a recursos compartidos una vez que hacerlo cada vez que se crea un nuevo hilo. Todo proceso debe tener al menos un hilo, ya que los hilos realizan el trabajo. No existe un hilo sin su proceso, ni un proceso sin al menos un hilo.

En el negocio de la pizza, una pizzería individual es la analogía del proceso. Proporciona el entorno y el equipo necesarios para que un trabajador desempeñe su labor. El equipo es caro, por lo que es más económico y eficiente que se comparta entre trabajadores; no hace falta que cada uno tenga equipos propios. Por otro lado, una tienda no puede funcionar sin trabajadores; al menos uno es imprescindible, pues sin ellos todo el equipo quedaría inactivo. Juntos, estos elementos constituyen el “proceso” de hacer y vender pizza.

![estructura de un proceso](../recursos/the-process.svg)

## Hilo (thread)

En informática, un hilo de ejecución es una secuencia de instrucciones dentro de un proceso que puede planificarse y ejecutarse de forma independiente. Cada hilo dispone de su propio ejecutor (executor), que solo puede gestionar un hilo a la vez. Varios hilos dentro del mismo proceso pueden operar de forma concurrente (alternando tareas) o en paralelo (simultáneamente si hay varios ejecutores disponibles), según la planificación y los recursos disponibles.

Para entender “hilo”, piensa en las personas empleadas en la pizzería. Realizan tareas según su puesto, siguen las normas del local y utilizan recursos compartidos.

En esta analogía:
- Los trabajadores de la pizzería representan a los ejecutores de hilos.
- Las tareas que realizan son los hilos del “proceso” pizzería.

## Concurrencia y paralelismo

Concurrencia y paralelismo describen formas distintas de manejar varias tareas con eficiencia.

- Concurrencia: imagina a un chef preparando dos platos “a la vez”. Pica verduras para la ensalada, y mientras se enfrían, empieza a asar el pollo. No trabaja exactamente en ambos platos al mismo tiempo, sino que alterna entre tareas y hace progresar ambas. Eso es concurrencia: gestión de múltiples tareas alternando entre ellas para ganar eficiencia.

- Paralelismo: ahora imagina una cocina grande con dos chefs trabajando al mismo tiempo: una persona asa el pollo y otra prepara la ensalada. Cada una trabaja de manera independiente y simultánea. Eso es paralelismo: varias tareas suceden a la vez, cada una con recursos separados.

En resumen, la concurrencia alterna tareas dando la apariencia de progreso simultáneo; el paralelismo ejecuta tareas realmente al mismo tiempo. Ambos buscan optimizar tiempo y uso de recursos, pero lo hacen de forma distinta: concurrencia = cambio rápido de contexto; paralelismo = ejecución simultánea con múltiples recursos.

## Conclusión

- Los procesos son como pizzerías: contenedores de hilos de trabajo, recursos compartidos y parámetros necesarios. Todo proceso debe tener al menos un hilo.
- Los hilos son unidades de ejecución dentro de un proceso; pueden operar de forma concurrente o en paralelo.
- Las tareas concurrentes que solo compiten por el tiempo del ejecutor y no requieren muchos recursos pueden ejecutarse dentro del mismo hilo (tareas ligeras, concurrencia interna). Es más eficiente que crear hilos adicionales. La ejecución dentro de un hilo puede ser sincrónica o asincrónica, pero nunca paralela.
- Entender estos conceptos con la analogía de la pizzería facilita comprender cómo cooperan procesos e hilos en los sistemas informáticos.