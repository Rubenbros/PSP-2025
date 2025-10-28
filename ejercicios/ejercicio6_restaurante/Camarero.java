package ejercicio6_restaurante;

import java.util.Random;

public class Camarero extends Thread {
    private final String nombre;
    private final Restaurante restaurante;
    private int cuentasProcesadas;
    private boolean trabajando;
    private final Random random;

    public Camarero(String nombre, Restaurante restaurante) {
        this.nombre = nombre;
        this.restaurante = restaurante;
        this.cuentasProcesadas = 0;
        this.trabajando = true;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (trabajando) {
            try {
                Mesa mesa = restaurante.atenderCuenta();

                if (mesa != null) {
                    int tiempo = 1000 + random.nextInt(1000);
                    Thread.sleep(tiempo);
                    cuentasProcesadas++;
                } else {
                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void terminarTurno() {
        trabajando = false;
        this.interrupt();
    }

    public int getCuentasProcesadas() {
        return cuentasProcesadas;
    }

    public String getNombre() {
        return nombre;
    }
}