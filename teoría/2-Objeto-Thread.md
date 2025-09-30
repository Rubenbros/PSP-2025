# Hilos en Java

Java se diseñó originalmente con soporte incorporado para la multihilación (multithreading). Los hilos están soportados a nivel de la JVM, a nivel del lenguaje mediante palabras clave especiales y a nivel de la biblioteca estándar. Todo programa Java tiene al menos un hilo, llamado main, creado automáticamente por la JVM para ejecutar las sentencias dentro del método main. Además, todos los programas Java tienen otros hilos predeterminados (por ejemplo, un hilo separado para el recolector de basura).

A lo largo de las etapas de evolución del lenguaje Java, el enfoque de la multihilación ha pasado del uso de hilos de bajo nivel al uso de abstracciones de más alto nivel. Sin embargo, comprender bien la base fundamental sigue siendo muy importante para ser un buen desarrollador.

## Una clase para hilos

Cada hilo está representado por un objeto que es una instancia de la clase java.lang.Thread (o de una subclase). Esta clase tiene un método estático llamado currentThread para obtener una referencia al objeto del hilo que se está ejecutando actualmente:

```java
Thread thread = Thread.currentThread(); // el hilo actual
```

Cualquier hilo tiene un nombre, un identificador (long), una prioridad y otras características que pueden obtenerse a través de sus métodos.

## Información sobre el hilo principal

El siguiente ejemplo muestra cómo obtener las características del hilo principal al obtener una referencia a él mediante un objeto de la clase Thread.

```java
public class MainThreadDemo {
    public static void main(String[] args) {
        Thread t = Thread.currentThread(); // hilo principal

        System.out.println("Name: " + t.getName());
        System.out.println("ID: " + t.getId());
        System.out.println("Alive: " + t.isAlive());
        System.out.println("Priority: " + t.getPriority());
        System.out.println("Daemon: " + t.isDaemon());

        t.setName("my-thread");
        System.out.println("New name: " + t.getName());
    }
}
```

Todas las sentencias de este programa son ejecutadas por el hilo principal.

La invocación t.isAlive() devuelve si el hilo se ha iniciado y aún no ha terminado. Todo hilo tiene una prioridad, y el método getPriority() devuelve la prioridad de un hilo dado. Los hilos con mayor prioridad se ejecutan con preferencia sobre los hilos con menor prioridad. La invocación t.isDaemon() comprueba si el hilo es un daemon. Un hilo daemon (término tomado de UNIX) es un hilo de baja prioridad que se ejecuta en segundo plano para realizar tareas como la recolección de basura, etc. La JVM no espera a los hilos daemon antes de finalizar, mientras que sí espera a los hilos no daemon.

La salida del programa tendrá un aspecto similar a este:

```
Name: main
ID: 1
Alive: true
Priority: 5
Daemon: false
New name: my-thread
```

El mismo código puede aplicarse a cualquier hilo actual, no solo a main.
