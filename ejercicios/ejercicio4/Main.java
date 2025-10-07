package ejercicio4;

/**
 * EJERCICIO 7: Productor-Consumidor con Almacén
 *
 * OBJETIVO:
 * Implementar sincronización para un problema clásico: varios productores
 * añaden productos a un almacén mientras varios consumidores los retiran.
 *
 * PROBLEMAS A RESOLVER:
 * 1. Race condition: Varios hilos acceden al stock simultáneamente
 * 2. Capacidad excedida: Productores pueden exceder la capacidad del almacén
 * 3. Stock negativo: Consumidores pueden consumir más de lo disponible
 * 4. Estadísticas incorrectas: Los contadores se corrompen sin sincronización
 *
 * TAREAS:
 * 1. Sincronizar los métodos produce(), consume() y printStats() en Warehouse.java
 * 2. Asegurarse de que:
 *    - El stock nunca exceda la capacidad
 *    - El stock nunca sea negativo
 *    - Las estadísticas sean consistentes al final
 *
 * PISTAS:
 * - Usa synchronized en los métodos que modifican o leen el estado compartido
 * - Todos los métodos deben sincronizarse en el mismo monitor (this)
 * - Si las estadísticas finales no cuadran, hay un problema de sincronización
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Crear almacén con capacidad de 50 productos
        Warehouse warehouse = new Warehouse(50);

        System.out.println("=== SIMULACIÓN PRODUCTOR-CONSUMIDOR ===\n");
        System.out.println("Capacidad del almacén: " + warehouse.getCapacity());
        System.out.println("Stock inicial: " + warehouse.getStock());
        System.out.println("\nIniciando producción y consumo...\n");

        // Crear 3 productores
        ProducerThread producer1 = new ProducerThread(warehouse, 10, 5, "Productor-1");
        ProducerThread producer2 = new ProducerThread(warehouse, 8, 6, "Productor-2");
        ProducerThread producer3 = new ProducerThread(warehouse, 12, 4, "Productor-3");

        // Crear 2 consumidores
        ConsumerThread consumer1 = new ConsumerThread(warehouse, 15, 4, "Consumidor-1");
        ConsumerThread consumer2 = new ConsumerThread(warehouse, 10, 5, "Consumidor-2");

        // Iniciar todos los hilos
        producer1.start();
        producer2.start();
        producer3.start();
        consumer1.start();
        consumer2.start();

        // Esperar a que todos terminen
        producer1.join();
        producer2.join();
        producer3.join();
        consumer1.join();
        consumer2.join();

        System.out.println("\n=== TODOS LOS HILOS HAN TERMINADO ===\n");

        // Mostrar estadísticas finales
        warehouse.printStats();

        // Verificación final
        System.out.println("ANÁLISIS:");
        System.out.println("- Producción esperada: (10*5) + (8*6) + (12*4) = " + (10*5 + 8*6 + 12*4));
        System.out.println("- Consumo esperado: (15*4) + (10*5) = " + (15*4 + 10*5));
        System.out.println("- Stock esperado: " + ((10*5 + 8*6 + 12*4) - (15*4 + 10*5)));
        System.out.println("\nSi los valores coinciden, ¡la sincronización funciona correctamente! ✓");
    }
}
