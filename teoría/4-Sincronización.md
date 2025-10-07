# Clase 4: Sincronización de hilos en Java

Trabajar concurrentemente con datos compartidos desde múltiples hilos puede causar comportamientos inesperados o erróneos. Afortunadamente, Java proporciona un mecanismo para controlar el acceso de múltiples hilos a un recurso compartido de cualquier tipo. El mecanismo se conoce como sincronización de hilos.

## Términos y conceptos importantes

Antes de comenzar a usar la sincronización en nuestro código, vamos a introducir los términos y conceptos que vamos a utilizar.

### 1) Sección crítica

Una **sección crítica** es una región de código que accede a recursos compartidos y no debería ser ejecutada por más de un hilo al mismo tiempo. Un recurso compartido puede ser una variable, archivo, puerto de entrada/salida, base de datos o cualquier otra cosa.

Consideremos un ejemplo. Una clase tiene un campo estático llamado `counter`:

```java
public static long counter = 0;
```

Dos hilos incrementan el campo (aumentan en 1) 10 000 000 de veces concurrentemente. El valor final debería ser 20 000 000. Pero, como hemos discutido en temas anteriores, el resultado a menudo puede resultar incorrecto, por ejemplo, 10 999 843.

Esto sucede porque a veces un hilo no ve los cambios de los datos compartidos realizados por otro hilo, y a veces un hilo puede ver un valor intermedio de una operación no atómica. Estos son problemas de visibilidad y atomicidad con los que tratamos al trabajar con datos compartidos.

Es por esto que incrementar un valor por múltiples hilos es una sección crítica. Por supuesto, este ejemplo es muy simple, una sección crítica puede ser mucho más complicada.

### 2) El monitor

El **monitor** es un mecanismo especial para controlar el acceso concurrente a un objeto. En Java, cada objeto tiene un monitor implícito asociado. Un hilo puede adquirir un monitor, entonces otros hilos no pueden adquirir este monitor al mismo tiempo. Esperarán hasta que el propietario (el hilo que adquirió el monitor) lo libere.

Por lo tanto, un hilo puede ser bloqueado por el monitor de un objeto y esperar su liberación. Este mecanismo permite a los programadores proteger las secciones críticas de ser accedidas por múltiples hilos concurrentemente.

## La palabra clave synchronized

La forma "clásica" y más simple de proteger el código de ser accedido por múltiples hilos concurrentemente es usando la palabra clave `synchronized`.

Se usa en dos formas diferentes:

1. **Método synchronized** (un método estático o de instancia)
2. **Bloques o sentencias synchronized** (dentro de un método estático o de instancia)

Un método o bloque sincronizado necesita un objeto para bloquear hilos. El monitor asociado con este objeto controla el acceso concurrente a la sección crítica especificada. Solo un hilo puede ejecutar código en un bloque o método sincronizado al mismo tiempo. Otros hilos están bloqueados hasta que el hilo dentro del bloque o método sincronizado lo abandone.

## Métodos estáticos synchronized

Cuando sincronizamos métodos estáticos usando la palabra clave `synchronized`, el monitor es la clase en sí misma. Solo un hilo puede ejecutar el cuerpo de un método estático sincronizado al mismo tiempo. Esto puede resumirse como "un hilo por clase".

Aquí hay un ejemplo de una clase con un único método estático sincronizado llamado `doSomething`:

```java
class SomeClass {
    public static synchronized void doSomething() {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s entered the method", threadName));
        System.out.println(String.format("%s leaves the method", threadName));
    }
}
```

El método `doSomething` está declarado como sincronizado. Puede ser invocado solo desde un hilo a la vez. El método está sincronizado en el objeto de la clase `SomeClass` al que pertenece el método estático. Java crea un único objeto especial para cada clase. Para obtenerlo, usa el nombre de la clase más el prefijo `.class`. En nuestro caso es `SomeClass.class`.

Llamemos al método desde dos hilos concurrentemente. El resultado siempre será similar a:

```
Thread-0 entered the method
Thread-0 leaves the method
Thread-1 entered the method
Thread-1 leaves the method
```

Es imposible que más de un hilo ejecute código dentro del método al mismo tiempo.

## Métodos de instancia synchronized

Los métodos de instancia se sincronizan en la instancia (objeto). El monitor es el objeto `this` actual que posee el método. Si tenemos dos instancias de una clase, cada instancia tiene su propio monitor para sincronización.

Solo un hilo puede ejecutar código en un método de instancia sincronizado de una instancia particular. Pero diferentes hilos pueden ejecutar métodos de diferentes objetos al mismo tiempo. Esto puede resumirse como "un hilo por instancia".

Aquí hay un ejemplo de una clase con un único método de instancia sincronizado llamado `doSomething`. La clase también tiene un constructor para distinguir instancias:

```java
class SomeClass {
    private String name;

    public SomeClass(String name) {
        this.name = name;
    }

    public synchronized void doSomething() {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s entered the method of %s", threadName, name));
        System.out.println(String.format("%s leaves the method of %s", threadName, name));
    }
}
```

Aquí hay una clase para crear hilos que toma una instancia de `SomeClass`:

```java
class MyThread extends Thread {
    private SomeClass someClass;

    public MyThread(SomeClass someClass) {
        this.someClass = someClass;
    }

    @Override
    public void run() {
        someClass.doSomething();
    }
}
```

Creemos dos instancias de la clase y tres hilos invocando `doSomething`. El primer y segundo hilo toman la misma instancia de la clase, y el tercer hilo toma otra:

```java
SomeClass instance1 = new SomeClass("instance-1");
SomeClass instance2 = new SomeClass("instance-2");

MyThread first = new MyThread(instance1);
MyThread second = new MyThread(instance1);
MyThread third = new MyThread(instance2);

first.start();
second.start();
third.start();
```

El resultado se verá así:

```
Thread-0 entered the method of instance-1
Thread-2 entered the method of instance-2
Thread-0 leaves the method of instance-1
Thread-1 entered the method of instance-1
Thread-2 leaves the method of instance-2
Thread-1 leaves the method of instance-1
```

Como puedes ver, no hay hilos ejecutando el código en `doSomething` de `instance-1` al mismo tiempo. Intenta ejecutarlo muchas veces.

## Bloques synchronized (sentencias)

A veces necesitas sincronizar solo una parte de un método. Esto es posible usando bloques sincronizados (sentencias). Deben especificar un objeto para bloquear hilos.

Aquí hay una clase con un método estático y uno de instancia. Ambos métodos no están sincronizados pero tienen partes sincronizadas dentro:

```java
class SomeClass {
    public static void staticMethod() {
        // código no sincronizado
        ...
        synchronized (SomeClass.class) { // sincronización en objeto de la clase SomeClass
            // código sincronizado
            ...
        }
    }

    public void instanceMethod() {
        // código no sincronizado
        ...
        synchronized (this) { // sincronización en esta instancia
            // código sincronizado
            ...
        }
    }
}
```

El bloque dentro de `staticMethod` está sincronizado en el objeto `SomeClass.class`, lo que significa que solo un hilo puede ejecutar código en este bloque.

El bloque dentro de `instanceMethod` está sincronizado en esta instancia, lo que significa que solo un hilo puede ejecutar el bloque de la instancia. Pero algún otro hilo puede ejecutar el bloque de diferentes instancias al mismo tiempo.

Los bloques sincronizados pueden parecerse a los métodos sincronizados, pero permiten a los programadores sincronizar solo las partes necesarias de los métodos.

## Conclusión

Hemos cubierto la palabra clave `synchronized` - mecanismo básico de sincronización de hilos. Usándola puedes controlar el acceso a secciones críticas y estar seguro de que solo un hilo procesa una pieza de código protegida. La palabra clave puede aplicarse tanto a métodos estáticos como de instancia, así como a bloques de código.

La principal diferencia entre métodos estáticos y de instancia es el monitor de sincronización:
- Aplicar la palabra clave `synchronized` a un método **estático** usa el objeto de la clase como monitor
- La sincronización de método de **instancia** usa la instancia misma como monitor
- Aplicar sincronización a un **bloque** es la forma más ágil. Permite configurar los límites de la sección crítica así como especificar el monitor de sincronización.
