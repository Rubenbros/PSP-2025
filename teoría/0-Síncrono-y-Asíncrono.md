# Tipos de flujo de trabajo: síncrono, asíncrono y paralelo

Suele ocurrir que, al considerar un proceso complejo (llamémoslo “flujo de trabajo”), sus partes se comporten de forma distinta. A veces las acciones van una tras otra; a veces suceden en orden variable y se superponen; y a veces ocurren simultáneamente y en paralelo. El flujo puede evolucionar de formas distintas. Hay tres tipos de secuenciación en la ejecución de flujos de trabajo: sincrónica, asincrónica y paralela.

Muchos términos relacionados con el procesamiento en programas informáticos no son solo técnicos. Describen una gran variedad de fenómenos del mundo real. En cierto sentido, los procesos dentro de un ordenador no son tan diferentes de los de la vida cotidiana. Es más, en cierto nivel de abstracción, son prácticamente idénticos. Usemos entonces ejemplos reales para explorar sus conceptos básicos.

Un buen ejemplo de proceso complejo es la atención al cliente. Lo utilizaremos para estudiar los tipos básicos de flujo desde el punto de vista de la secuencia de ejecución.

## Flujo de trabajo síncrono

Existen muchos modelos para gestionar flujos de clientes. El enfoque más simple es una tienda con un solo vendedor. El vendedor atiende a cada cliente desde el principio hasta el final de la venta y asume todos los roles, desde almacenero hasta cajero.

![vendedor atendiendo a un cliente mientras otros esperan en fila](../recursos/9_%282%29.svg)

Cuando llegan muchos clientes a la vez, este enfoque dista de ser perfecto, porque el vendedor solo puede atender a uno por vez y los demás deben esperar. Atienden a cada cliente por separado, uno después de otro, es decir, empiezan con el siguiente solo cuando terminan con el actual. A este tipo de acción la llamamos sincrónica.

Los flujos síncronos son muy comunes. Muchas actividades deben ser sincrónicas si su objetivo es lograr resultados específicos. Hay infinidad de ejemplos: escenas en una película, el ensamblaje de un coche, las palabras en una oración, cocinar, etc.

## Flujo de trabajo asíncrono

Imaginemos que nuestra vieja tienda se convierte en una pizzería. Tras tomar el pedido del primer cliente, este debe esperar a que su pizza se cocine. En ese momento, el vendedor deja a ese cliente un rato y atiende al segundo, luego al tercero, y así sucesivamente.

![vendedor atendiendo a varios clientes en períodos superpuestos](../recursos/10_%282%29.svg)

Cuando la pizza del primero está lista, el vendedor vuelve con él para completar la venta. Y así se repite una y otra vez.

Nuestro conocido vendedor puede atender a varios clientes a la vez en períodos que se superponen. A ese comportamiento lo llamamos asíncrono.

Este tipo de operaciones surge a menudo cuando hay esperas. Imagina que lees en un avión mientras vuelas, o lavas los platos mientras se cocina algo: esas parejas de actividades son asincrónicas.

## Procesamiento en paralelo

A medida que crecen las ventas de la pizzería, un solo empleado ya no es suficiente. Contratamos a varios. Si cada vendedor tiene un horno compacto para preparar exactamente una pizza a la vez, podemos dividir la cola de clientes entre los vendedores.

![varios vendedores atendiendo a un cliente cada uno en paralelo](../recursos/11_%282%29.svg)

Ahora cada uno trabaja de forma independiente, y este es un caso de procesamiento en paralelo. Cada tarea en paralelo se ejecuta de forma continua como una unidad completa. La ejecución paralela solo es posible si hay más de un ejecutor. Ejemplos cotidianos: los cajeros de un supermercado o las autopistas.

## Conclusión

Existen tres tipos de procesamiento de flujos de trabajo. El primero es síncrono, el segundo es asíncrono y el tercero es paralelo.

- Síncrono: una tarea a la vez; la siguiente empieza cuando la actual termina.
- Asíncrono: varias tareas a la vez en períodos superpuestos, ejecutadas por partes.
- Paralelo: una o varias tareas divididas y ejecutadas de forma continua por distintos ejecutores en paralelo.