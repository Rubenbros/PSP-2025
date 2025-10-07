package ejercicio4;

/**
 * Hilo productor que añade productos al almacén
 */
public class ProducerThread extends Thread {
    private Warehouse warehouse;
    private int productionCycles;
    private int quantityPerCycle;

    public ProducerThread(Warehouse warehouse, int cycles, int quantity, String name) {
        super(name);
        this.warehouse = warehouse;
        this.productionCycles = cycles;
        this.quantityPerCycle = quantity;
    }

    @Override
    public void run() {
        for (int i = 0; i < productionCycles; i++) {
            boolean success = warehouse.produce(getName(), quantityPerCycle);

            if (!success) {
                // Si no puede producir, espera un poco e intenta de nuevo
                try {
                    Thread.sleep(100);
                    i--; // Reintentar este ciclo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(getName() + " ha terminado su producción.");
    }
}
