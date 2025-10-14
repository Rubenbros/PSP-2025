package ejercicio2extra;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(1000);

        System.out.println("Saldo inicial: " + account.getBalance());
        System.out.println("---");

        // Crear varios hilos que intentan retirar dinero simultáneamente
        WithdrawThread t1 = new WithdrawThread(account, 600, "Hilo-1");
        WithdrawThread t2 = new WithdrawThread(account, 600, "Hilo-2");
        WithdrawThread t3 = new WithdrawThread(account, 300, "Hilo-3");

        // Iniciar todos los hilos
        t1.start();
        t2.start();
        t3.start();

        for(int i = 0; i < 5;i++){
            account.deposit(500);
            Thread.sleep(5000);
        }
        // Esperar a que terminen
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();

        System.out.println("---");
        System.out.println("Saldo final: " + account.getBalance());
        System.out.println("\nSi la sincronización funciona, solo uno o dos hilos deberían poder retirar.");
    }
}
