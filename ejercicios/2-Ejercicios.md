# Ejercicios Clase 3: Hilos en Java

## Ejercicio 1: Implementar NumbersThread

Completa la clase `NumbersThread` que extiende `Thread`. La clase debe:
- Tener un constructor que acepte dos parámetros enteros: `from` y `to` como bordes del rango
- Implementar el método `run` que imprima todos los números del rango dado (inclusive) a la salida estándar

**Plantilla proporcionada:**

```java
class NumbersThread extends Thread {

    public NumbersThread(int from, int to) {
        // Implementar el constructor
    }

    // Debes sobrescribir algún método aquí
}
```

**Entrada de ejemplo 1:**
```
1 3
```

**Salida de ejemplo 1:**
```
1
2
3
```

**Entrada de ejemplo 2:**
```
2 2
```

**Salida de ejemplo 2:**
```
2
```

---
