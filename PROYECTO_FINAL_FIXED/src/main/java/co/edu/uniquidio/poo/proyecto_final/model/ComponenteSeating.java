package co.edu.uniquidio.poo.proyecto_final.model;


public interface ComponenteSeating {
    void mostrarMapa();
    int getCapacidadDisponible();
    boolean reservarAsiento(String fila, int numero);
}
