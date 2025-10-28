package ejercicio6_restaurante;

import java.util.*;

public class Restaurante {
    private final List<Mesa> mesas;
    private final Queue<Mesa> mesasEsperandoCuenta;
    private int clientesAtendidos;
    private int clientesRechazados;

    public Restaurante(int[] capacidadesMesas) {
        this.mesas = new ArrayList<>();
        this.mesasEsperandoCuenta = new LinkedList<>();
        this.clientesAtendidos = 0;
        this.clientesRechazados = 0;

        for (int i = 0; i < capacidadesMesas.length; i++) {
            mesas.add(new Mesa(i + 1, capacidadesMesas[i]));
        }

        System.out.println("Restaurante abierto con " + mesas.size() + " mesas");
    }

    public Mesa solicitarMesa(String nombreCliente, int personas) {
        for (Mesa mesa : mesas) {
            if (mesa.estaDisponible(personas)) {
                if (mesa.ocupar(nombreCliente, personas)) {
                    clientesAtendidos++;
                    return mesa;
                }
            }
        }
        clientesRechazados++;
        return null;
    }

    public void liberarMesa(Mesa mesa) {
        mesa.liberar();
    }

    public void pedirCuenta(Mesa mesa) {
        mesasEsperandoCuenta.offer(mesa);
    }

    public Mesa atenderCuenta() {
        if (!mesasEsperandoCuenta.isEmpty()) {
            return mesasEsperandoCuenta.poll();
        }
        return null;
    }

    public void mostrarEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");
        System.out.println("Clientes atendidos: " + clientesAtendidos);
        System.out.println("Clientes rechazados: " + clientesRechazados);
        System.out.println("Mesas ocupadas: " + contarMesasOcupadas() + "/" + mesas.size());
    }

    private int contarMesasOcupadas() {
        int ocupadas = 0;
        for (Mesa mesa : mesas) {
            if (mesa.isOcupada()) {
                ocupadas++;
            }
        }
        return ocupadas;
    }
}