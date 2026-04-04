package co.edu.uniquidio.poo.proyecto_final.model;


import java.util.ArrayList;
import java.util.List;

public class Zona implements ComponenteSeating {  // Parte del Composite
    private String idZona;
    private String nombre;          // VIP, Preferencial, General
    private int capacidad;
    private double precioBase;
    private List<Asiento> asientos = new ArrayList<>();  // Composición

    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        // Crear asientos automáticamente (RF-031)
        for (int i = 1; i <= capacidad; i++) {
            asientos.add(new Asiento("A" + i, i, "Disponible", this));
        }
    }

    // RF-029
    public double getPrecioBase() { return precioBase; }
    public int getCapacidad() { return capacidad; }
    public String getNombre() { return nombre; }

    // RF-030 - Ocupación por zona
    public int getOcupacion() {
        return (int) asientos.stream().filter(a -> !a.getEstado().equals("Disponible")).count();
    }

    // Composite (RF-050)
    @Override
    public void mostrarMapa() {
        System.out.println("Zona: " + nombre + " - Asientos disponibles: " + getCapacidadDisponible());
        asientos.forEach(Asiento::mostrarMapa);
    }

    @Override
    public int getCapacidadDisponible() {
        return (int) asientos.stream().filter(a -> a.getEstado().equals("Disponible")).count();
    }

    @Override
    public boolean reservarAsiento(String fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila().equals(fila) && a.getNumero() == numero)
                .findFirst()
                .map(a -> a.reservar())
                .orElse(false);
    }

    public List<Asiento> getAsientos() { return asientos; }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

}
