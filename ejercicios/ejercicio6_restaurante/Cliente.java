package ejercicio6_restaurante;

import java.util.Random;

public class Cliente extends Thread {
    private final String nombre;
    private final int personas;
    private final Restaurante restaurante;
    private final Random random;
    private boolean atendido;

    public Cliente(String nombre, int personas, Restaurante restaurante) {
        this.nombre = nombre;
        this.personas = personas;
        this.restaurante = restaurante;
        this.random = new Random();
        this.atendido = false;
    }

    @Override
    public void run() {
        System.out.println("[" + nombre + "] Llegando con " + personas + " personas");

        try {
            Mesa mesa = restaurante.solicitarMesa(nombre, personas);

            if (mesa != null) {
                System.out.println("[" + nombre + "] Conseguí mesa " + mesa.getNumeroMesa());
                atendido = true;

                int tiempoComiendo = 2000 + random.nextInt(3000);
                Thread.sleep(tiempoComiendo);

                System.out.println("[" + nombre + "] Pidiendo cuenta...");
                restaurante.pedirCuenta(mesa);
                Thread.sleep(500);

                restaurante.liberarMesa(mesa);
                System.out.println("[" + nombre + "] Me voy");

            } else {
                System.out.println("[" + nombre + "] No hay mesas, me voy");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean fueAtendido() {
        return atendido;
    }
}