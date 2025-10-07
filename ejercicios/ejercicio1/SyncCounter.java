package ejercicio1;

public class SyncCounter {
    private int count = 0;

    // COMPLETAR: Agrega synchronized para hacer este método seguro para hilos
    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
