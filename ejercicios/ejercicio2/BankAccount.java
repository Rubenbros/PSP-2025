package ejercicio2;

public class BankAccount {
    private int balance;

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    // COMPLETAR: Agrega synchronized para evitar retiradas concurrentes
    public void withdraw(int amount) {
        if (balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " retirando " + amount);
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " completó. Saldo: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " no puede retirar " + amount + ". Saldo insuficiente: " + balance);
        }
    }

    // COMPLETAR: Agrega synchronized para proteger el depósito
    public void deposit(int amount) {
        System.out.println(Thread.currentThread().getName() + " depositando " + amount);
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " completó. Saldo: " + balance);
    }

    public int getBalance() {
        return balance;
    }
}
