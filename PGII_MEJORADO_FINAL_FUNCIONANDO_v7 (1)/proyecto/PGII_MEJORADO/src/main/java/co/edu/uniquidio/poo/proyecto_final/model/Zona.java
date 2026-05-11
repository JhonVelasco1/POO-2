package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Zona — sector del recinto (VIP, Preferencial, General).
 * Actúa como <b>nodo</b> del patrón Composite: contiene asientos (RF-028, RF-029, RF-030).
 * Implementa {@link ComponenteSeatingComposite} (RF-050).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Zona implements ComponenteSeatingComposite {

    /** Identificador único de la zona. */
    private String idZona;
    /** Nombre de la zona (VIP, Preferencial, General). */
    private String nombre;
    /** Capacidad total de asientos. */
    private int capacidad;
    /** Precio base en pesos (RF-029). */
    private double precioBase;
    /** Lista de asientos (Composición — RF-031). */
    private List<Asiento> asientos = new ArrayList<>();

    /**
     * Construye la zona y genera automáticamente sus asientos.
     *
     * @param idZona    identificador único
     * @param nombre    nombre de la zona
     * @param capacidad capacidad total de asientos
     * @param precioBase precio base en pesos
     */
    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this.idZona     = idZona;
        this.nombre     = nombre;
        this.capacidad  = capacidad;
        this.precioBase = precioBase;
        // Generación automática de asientos (RF-031)
        generarAsientos();
    }

    /**
     * Genera los asientos de la zona automáticamente según la capacidad.
     * Usa filas A-Z con hasta 12 asientos por fila.
     */
    private void generarAsientos() {
        asientos.clear();
        int asientosPorFila = 12;
        int filasNecesarias = (int) Math.ceil((double) capacidad / asientosPorFila);

        int contador = 0;
        for (int f = 0; f < filasNecesarias && contador < capacidad; f++) {
            String fila = String.valueOf((char) ('A' + f));
            for (int num = 1; num <= asientosPorFila && contador < capacidad; num++) {
                asientos.add(new Asiento(fila, num, "Disponible", this));
                contador++;
            }
        }
    }

    // ==================== RF-030 ====================

    /**
     * Calcula el número de asientos ocupados (no disponibles) en esta zona (RF-030).
     *
     * @return número de asientos en estado no-Disponible
     */
    public int getOcupacion() {
        return (int) asientos.stream()
                .filter(a -> !"Disponible".equals(a.getEstado()))
                .count();
    }

    // ==================== COMPOSITE (RF-050, RF-033) ====================

    @Override
    public void mostrarMapa() {
        System.out.println("🗺️ Zona: " + nombre
            + " | Disponibles: " + getCapacidadDisponible() + "/" + capacidad);
        asientos.forEach(Asiento::mostrarMapa);
    }

    @Override
    public int getCapacidadDisponible() {
        return (int) asientos.stream()
                .filter(a -> "Disponible".equals(a.getEstado()))
                .count();
    }

    @Override
    public boolean reservarAsiento(String fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila().equals(fila) && a.getNumero() == numero)
                .findFirst()
                .map(Asiento::reservar)
                .orElse(false);
    }

    // ==================== RF-032: cambiar estado de asiento ====================

    /**
     * Bloquea un asiento específico (RF-032 — estado Bloqueado).
     *
     * @param fila   fila del asiento
     * @param numero número del asiento
     * @return {@code true} si el asiento fue bloqueado
     */
    public boolean bloquearAsiento(String fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila().equals(fila) && a.getNumero() == numero)
                .findFirst()
                .map(a -> { a.cambiarEstado("Bloqueado"); return true; })
                .orElse(false);
    }

    /**
     * Libera un asiento bloqueado o reservado (RF-032).
     *
     * @param fila   fila del asiento
     * @param numero número del asiento
     * @return {@code true} si el asiento fue liberado
     */
    public boolean liberarAsiento(String fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila().equals(fila) && a.getNumero() == numero)
                .findFirst()
                .map(a -> { a.cambiarEstado("Disponible"); return true; })
                .orElse(false);
    }

    // ==================== GETTERS Y SETTERS ====================

    /** @return identificador único de la zona */
    public String getIdZona()           { return idZona; }
    /** @return nombre de la zona */
    public String getNombre()           { return nombre; }
    /** @return capacidad total */
    public int getCapacidad()           { return capacidad; }
    /** @return precio base en pesos */
    public double getPrecioBase()       { return precioBase; }
    /** @return lista de asientos */
    public List<Asiento> getAsientos()  { return asientos; }

    public void setIdZona(String id)             { this.idZona = id; }
    public void setNombre(String n)              { this.nombre = n; }
    public void setCapacidad(int c)              { this.capacidad = c; }
    public void setPrecioBase(double p)          { this.precioBase = p; }
    public void setAsientos(List<Asiento> lista) { this.asientos = lista; }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%,.0f", precioBase)
               + " | " + getCapacidadDisponible() + " disp.)";
    }
}
