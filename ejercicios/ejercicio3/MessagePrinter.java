package ejercicio3;

public class MessagePrinter {

    public void printMessage(String message) {
        // Código no crítico - puede ejecutarse concurrentemente
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " está preparando el mensaje...");

        // COMPLETAR: Usa un bloque synchronized(this) para proteger solo esta parte
        // INICIO DEL BLOQUE SINCRONIZADO
        System.out.println("--- INICIO: " + threadName + " ---");
        for (int i = 0; i < 3; i++) {
            System.out.println(threadName + ": " + message + " (" + i + ")");
        }
        System.out.println("--- FIN: " + threadName + " ---");
        // FIN DEL BLOQUE SINCRONIZADO

        // Código no crítico - puede ejecutarse concurrentemente
        System.out.println(threadName + " ha terminado de imprimir.");
    }
}
