# Ejercicio 6: Restaurante Concurrente

## Meta General del Ejercicio

Este ejercicio simula un **restaurante con múltiples hilos** que interactúan y compiten por recursos compartidos. Es un problema clásico de concurrencia donde debes proteger el acceso a recursos limitados (mesas) y coordinar la comunicación entre diferentes tipos de hilos (clientes y camareros).

**Conceptos clave a practicar:**
- Sincronización de acceso a recursos compartidos
- Protección contra condiciones de carrera (race conditions)
- Uso correcto de `synchronized` en métodos y objetos compartidos
- (Opcional/Avanzado) Coordinación entre hilos con `wait()` y `notify()`

---

## Descripción de las Clases

### **Mesa.java**
Representa una mesa del restaurante con capacidad limitada.

**Estado:**
- `numeroMesa`: Identificador único de la mesa
- `capacidad`: Número máximo de personas que pueden sentarse
- `ocupada`: Si la mesa está actualmente en uso
- `clienteActual`: Quién está usando la mesa

**Métodos principales:**
- `ocupar()`: Intenta ocupar la mesa para un cliente
- `liberar()`: Libera la mesa cuando el cliente termina
- `estaDisponible()`: Verifica si la mesa puede ser usada

⚠️ **Problema actual**: Múltiples hilos pueden verificar `ocupada` al mismo tiempo y todos pensar que la mesa está libre, causando que varios clientes ocupen la misma mesa.

### **Restaurante.java**
Gestor central que coordina las mesas, clientes y camareros.

**Responsabilidades:**
- Asignar mesas a clientes que llegan
- Gestionar la cola de mesas que esperan por la cuenta
- Mantener estadísticas (clientes atendidos/rechazados)
- Coordinar entre clientes y camareros

**Métodos principales:**
- `solicitarMesa()`: Un cliente pide una mesa para N personas
- `liberarMesa()`: Un cliente libera su mesa al irse
- `pedirCuenta()`: Un cliente pide que un camarero traiga la cuenta
- `atenderCuenta()`: Un camarero toma una mesa que pidió la cuenta

⚠️ **Problema actual**: Varios hilos acceden simultáneamente a las listas de mesas y colas sin protección, causando inconsistencias.

### **Cliente.java** (Thread)
Representa un cliente que llega al restaurante.

**Flujo de ejecución:**
1. Llega al restaurante y solicita una mesa
2. Si consigue mesa, come durante 2-5 segundos
3. Pide la cuenta al restaurante
4. Espera un poco y libera la mesa
5. Se va del restaurante

**Comportamiento concurrente:**
- Cada cliente es un hilo independiente
- Múltiples clientes llegan casi simultáneamente
- Compiten por las mismas mesas

### **Camarero.java** (Thread)
Representa un camarero que procesa cuentas.

**Flujo de ejecución:**
1. Constantemente pregunta al restaurante si hay cuentas por procesar
2. Si hay una cuenta, la procesa (tarda 1-2 segundos)
3. Si no hay cuentas, espera un poco e intenta de nuevo
4. Continúa hasta que termine su turno

**Comportamiento concurrente:**
- Hay 2 camareros trabajando simultáneamente
- Ambos compiten por las mismas cuentas en la cola
- Deben evitar procesar la misma cuenta dos veces

### **Main.java**
Orquesta toda la simulación.

**Proceso:**
1. Crea el restaurante con 5 mesas
2. Inicia 2 camareros
3. Crea y lanza 10 clientes (con delay entre cada uno)
4. Espera a que todos los clientes terminen
5. Detiene a los camareros
6. Muestra estadísticas finales

---

## El Problema

El código actual **NO está sincronizado**. Esto causa varios problemas:

1. **Dos clientes ocupan la misma mesa**: Ambos verifican que está libre, ambos la marcan como ocupada
2. **Estadísticas incorrectas**: `clientesAtendidos++` no es atómico, se pierden incrementos
3. **Cuentas procesadas dos veces**: Dos camareros toman la misma cuenta de la cola
4. **Mesa dice estar ocupada pero aparece como libre**: Escrituras/lecturas se intercalan

---

## Tu Tarea

Implementar la sincronización necesaria para garantizar que:

1. ✅ Solo un cliente pueda ocupar cada mesa
2. ✅ Las estadísticas sean correctas y consistentes
3. ✅ Los camareros no procesen la misma cuenta dos veces
4. ✅ No haya condiciones de carrera en ninguna operación

**Restricciones:**
- NO cambiar la lógica del negocio (flujo de clientes/camareros)
- Solo agregar mecanismos de sincronización (`synchronized`, etc.)

---

## Compilar y ejecutar

```bash
cd ejercicios/ejercicio6_restaurante
javac *.java
java ejercicio6_restaurante.Main
```

Si ves mensajes intercalados, clientes en mesas imposibles, o estadísticas extrañas, necesitas más sincronización.

---

## Pistas

1. Identifica cuáles son los **recursos compartidos** (objetos que múltiples hilos acceden)
2. Identifica las **secciones críticas** (bloques de código que no deben ejecutarse simultáneamente)
3. Revisa los ejercicios anteriores sobre `synchronized`
4. Pregúntate: "¿Qué pasa si dos hilos ejecutan este método al mismo tiempo?"

**¿Dónde necesitas sincronización?**
- Métodos que modifican el estado de Mesa
- Métodos de Restaurante que acceden a las listas de mesas
- Acceso a contadores (clientesAtendidos, clientesRechazados)
- Acceso a la cola de cuentas
