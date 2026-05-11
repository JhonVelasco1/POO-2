package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Entidad Entrada — ticket adquirido como parte de una compra (RF-038, RF-039, RF-040).
 *
 * <p>Estados posibles:</p>
 * <ul>
 *   <li>Activa  — entrada válida para usar</li>
 *   <li>Usada   — entrada ya utilizada en el evento</li>
 *   <li>Anulada — entrada cancelada/reembolsada</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Entrada {

    /** Identificador único de la entrada. */
    private String idEntrada;
    /** Zona a la que da acceso. */
    private Zona zona;
    /** Asiento numerado (puede ser {@code null} si la zona no tiene numeración). */
    private Asiento asiento;
    /** Precio final calculado (puede incluir servicios adicionales). */
    private double precioFinal;
    /** Estado de la entrada: Activa, Usada, Anulada. */
    private String estado = "Activa";

    /**
     * Construye una nueva entrada.
     *
     * @param idEntrada   identificador único
     * @param zona        zona de acceso
     * @param asiento     asiento (puede ser {@code null})
     * @param precioFinal precio final en pesos
     */
    public Entrada(String idEntrada, Zona zona, Asiento asiento, double precioFinal) {
        this.idEntrada   = idEntrada;
        this.zona        = zona;
        this.asiento     = asiento;
        this.precioFinal = precioFinal;
    }

    /**
     * Método de fábrica para generar una entrada asociada a una compra pagada (RF-038).
     *
     * @param z       zona
     * @param a       asiento (puede ser {@code null})
     * @param precio  precio de la entrada
     * @return nueva instancia de Entrada
     */
    public static Entrada generarEntrada(Zona z, Asiento a, double precio) {
        return new Entrada("ENT-" + System.nanoTime() % 100000, z, a, precio);
    }

    /** Anula la entrada por cancelación o reembolso (RF-040). */
    public void anular() {
        this.estado = "Anulada";
        if (asiento != null) asiento.cambiarEstado("Disponible");
    }

    /** Marca la entrada como usada (evento de control de acceso). */
    public void usar() { this.estado = "Usada"; }

    // ==================== GETTERS Y SETTERS ====================

    public String getIdEntrada()        { return idEntrada; }
    public Zona getZona()               { return zona; }
    public Asiento getAsiento()         { return asiento; }
    public double getPrecioFinal()      { return precioFinal; }
    public String getEstado()           { return estado; }

    public void setIdEntrada(String id) { this.idEntrada = id; }
    public void setZona(Zona z)         { this.zona = z; }
    public void setAsiento(Asiento a)   { this.asiento = a; }
    public void setPrecioFinal(double p){ this.precioFinal = p; }
    public void setEstado(String e)     { this.estado = e; }

    @Override
    public String toString() {
        return idEntrada + " | " + zona.getNombre()
               + (asiento != null ? " | " + asiento : "")
               + " | $" + String.format("%,.0f", precioFinal)
               + " | " + estado;
    }
}
