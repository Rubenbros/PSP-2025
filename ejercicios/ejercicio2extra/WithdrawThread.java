package ejercicio2extra;

public class WithdrawThread extends Thread {
    private BankAccount account;
    private int amount;

    public WithdrawThread(BankAccount account, int amount, String name) {
        super(name);
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        while(true){
            if (!account.withdraw(amount)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("El main me mato");
                    break;
                }
            }
        }
    }
}
