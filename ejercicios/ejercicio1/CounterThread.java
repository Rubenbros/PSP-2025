package ejercicio1;

public class CounterThread extends Thread {
    private SyncCounter counter;
    private int iterations;

    public CounterThread(SyncCounter counter, int iterations) {
        this.counter = counter;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            counter.increment();
        }
    }
}
