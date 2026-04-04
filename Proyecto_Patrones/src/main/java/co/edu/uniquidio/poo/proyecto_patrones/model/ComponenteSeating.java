package co.edu.uniquidio.poo.proyecto_patrones.model;

public interface ComponenteSeating {
    void mostrarMapa();
    int getCapacidadDisponible();
    boolean reservarAsiento(String fila, int numero);
}

// Zona y Asiento implementan ComponenteSeating (Composite)