package ejercicio4;

/**
 * Almacén compartido entre productores y consumidores
 * Tiene capacidad limitada y debe manejar productos de forma segura
 */
public class Warehouse {
    private int stock;
    private int capacity;
    private int totalProduced;
    private int totalConsumed;

    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.stock = 0;
        this.totalProduced = 0;
        this.totalConsumed = 0;
    }

    /**
     * COMPLETAR: Sincroniza este método para que sea thread-safe
     * Un productor añade productos al almacén
     * NO debe exceder la capacidad máxima
     */
    public boolean produce(String producerName, int quantity) {
        // Verificar si hay espacio suficiente
        if (stock + quantity > capacity) {
            System.out.println(producerName + " NO puede producir " + quantity +
                             " (stock: " + stock + ", capacidad: " + capacity + ")");
            return false;
        }

        // Simular tiempo de producción
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stock += quantity;
        totalProduced += quantity;
        System.out.println(producerName + " produjo " + quantity +
                         " productos. Stock actual: " + stock);
        return true;
    }

    /**
     * COMPLETAR: Sincroniza este método para que sea thread-safe
     * Un consumidor retira productos del almacén
     * NO debe consumir más de lo que hay disponible
     */
    public boolean consume(String consumerName, int quantity) {
        // Verificar si hay productos suficientes
        if (stock < quantity) {
            System.out.println(consumerName + " NO puede consumir " + quantity +
                             " (stock disponible: " + stock + ")");
            return false;
        }

        // Simular tiempo de consumo
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stock -= quantity;
        totalConsumed += quantity;
        System.out.println(consumerName + " consumió " + quantity +
                         " productos. Stock actual: " + stock);
        return true;
    }

    /**
     * COMPLETAR: Sincroniza este método para obtener estadísticas consistentes
     */
    public void printStats() {
        System.out.println("\n=== ESTADÍSTICAS DEL ALMACÉN ===");
        System.out.println("Stock actual: " + stock);
        System.out.println("Total producido: " + totalProduced);
        System.out.println("Total consumido: " + totalConsumed);
        System.out.println("Diferencia (producido - consumido): " + (totalProduced - totalConsumed));
        System.out.println("Stock debe ser igual a la diferencia: " +
                         (stock == (totalProduced - totalConsumed) ? "✓ CORRECTO" : "✗ ERROR"));
        System.out.println("================================\n");
    }

    public int getStock() {
        return stock;
    }

    public int getCapacity() {
        return capacity;
    }
}
