package ejercicio4;

/**
 * Hilo consumidor que retira productos del almacén
 */
public class ConsumerThread extends Thread {
    private Warehouse warehouse;
    private int consumptionCycles;
    private int quantityPerCycle;

    public ConsumerThread(Warehouse warehouse, int cycles, int quantity, String name) {
        super(name);
        this.warehouse = warehouse;
        this.consumptionCycles = cycles;
        this.quantityPerCycle = quantity;
    }

    @Override
    public void run() {
        for (int i = 0; i < consumptionCycles; i++) {
            boolean success = warehouse.consume(getName(), quantityPerCycle);

            if (!success) {
                // Si no puede consumir, espera un poco e intenta de nuevo
                try {
                    Thread.sleep(100);
                    i--; // Reintentar este ciclo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(getName() + " ha terminado su consumo.");
    }
}
