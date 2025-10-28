package ejercicio6_restaurante;

public class Mesa {
    private final int numeroMesa;
    private final int capacidad;
    private boolean ocupada;
    private String clienteActual;

    public Mesa(int numeroMesa, int capacidad) {
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.ocupada = false;
        this.clienteActual = null;
    }

    public boolean ocupar(String nombreCliente, int personas) {
        if (!ocupada && personas <= capacidad) {
            ocupada = true;
            clienteActual = nombreCliente;
            return true;
        }
        return false;
    }

    public void liberar() {
        ocupada = false;
        clienteActual = null;
    }

    public boolean estaDisponible(int personas) {
        return !ocupada && personas <= capacidad;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    @Override
    public String toString() {
        return "Mesa " + numeroMesa + " (cap: " + capacidad + ") - " +
               (ocupada ? "OCUPADA por " + clienteActual : "LIBRE");
    }
}