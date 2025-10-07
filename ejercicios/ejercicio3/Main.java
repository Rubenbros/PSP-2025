package ejercicio3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MessagePrinter printer = new MessagePrinter();

        // Crear tres hilos que usan el mismo printer
        PrinterThread t1 = new PrinterThread(printer, "Hola", "Hilo-A");
        PrinterThread t2 = new PrinterThread(printer, "Adiós", "Hilo-B");
        PrinterThread t3 = new PrinterThread(printer, "Buenos días", "Hilo-C");

        // Iniciar todos los hilos
        t1.start();
        t2.start();
        t3.start();

        // Esperar a que terminen
        t1.join();
        t2.join();
        t3.join();

        System.out.println("\n¡Todos los hilos han terminado!");
        System.out.println("\nSi el bloque synchronized funciona, las líneas entre");
        System.out.println("'INICIO' y 'FIN' de cada hilo NO deberían mezclarse.");
    }
}
