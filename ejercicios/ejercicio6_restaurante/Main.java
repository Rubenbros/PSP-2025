package ejercicio6_restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== SIMULACIÓN DE RESTAURANTE ===\n");

        int[] capacidadesMesas = {2, 2, 4, 4, 6};
        Restaurante restaurante = new Restaurante(capacidadesMesas);

        List<Camarero> camareros = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Camarero camarero = new Camarero("Camarero-" + i, restaurante);
            camareros.add(camarero);
            camarero.start();
        }

        List<Cliente> clientes = new ArrayList<>();
        Random random = new Random();
        String[] nombres = {"Juan", "María", "Pedro", "Ana", "Luis",
                           "Carmen", "José", "Laura", "Miguel", "Sofia"};

        for (int i = 0; i < nombres.length; i++) {
            int personas = 1 + random.nextInt(6);
            Cliente cliente = new Cliente(nombres[i], personas, restaurante);
            clientes.add(cliente);
        }

        for (Cliente cliente : clientes) {
            cliente.start();
            try {
                Thread.sleep(500 + random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Cliente cliente : clientes) {
            try {
                cliente.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Camarero camarero : camareros) {
            camarero.terminarTurno();
        }

        for (Camarero camarero : camareros) {
            try {
                camarero.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n=== RESULTADOS ===");
        restaurante.mostrarEstadisticas();

        int atendidos = 0;
        for (Cliente cliente : clientes) {
            if (cliente.fueAtendido()) atendidos++;
        }
        System.out.println("Clientes que comieron: " + atendidos + "/" + clientes.size());

        System.out.println("\n=== CAMAREROS ===");
        for (Camarero camarero : camareros) {
            System.out.println(camarero.getNombre() + ": " +
                              camarero.getCuentasProcesadas() + " cuentas");
        }
    }
}