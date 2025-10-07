package ejercicio1;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SyncCounter counter = new SyncCounter();
        int numThreads = 5;
        int iterationsPerThread = 1000;

        CounterThread[] threads = new CounterThread[numThreads];

        // Crear e iniciar los hilos
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(counter, iterationsPerThread);
            threads[i].start();
        }

        // Esperar a que todos terminen
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        System.out.println("Valor esperado: " + (numThreads * iterationsPerThread));
        System.out.println("Valor obtenido: " + counter.getCount());

        if (counter.getCount() == numThreads * iterationsPerThread) {
            System.out.println("¡CORRECTO! La sincronización funciona.");
        } else {
            System.out.println("INCORRECTO. Hay un problema de sincronización.");
        }
    }
}
