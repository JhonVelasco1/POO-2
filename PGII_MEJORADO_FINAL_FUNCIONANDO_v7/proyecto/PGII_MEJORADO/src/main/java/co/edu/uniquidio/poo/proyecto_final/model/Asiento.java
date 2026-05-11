package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Entidad Asiento — unidad numerada dentro de una zona (RF-031, RF-032, RF-033).
 * Actúa como <b>hoja</b> del patrón Composite ({@link ComponenteSeatingComposite}).
 *
 * <p>Estados posibles (RF-032):</p>
 * <ul>
 *   <li>Disponible</li>
 *   <li>Reservado</li>
 *   <li>Vendido</li>
 *   <li>Bloqueado</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Asiento implements ComponenteSeatingComposite {

    /** Identificador único del asiento. */
    private String idAsiento;
    /** Fila a la que pertenece el asiento. */
    private String fila;
    /** Número del asiento en la fila. */
    private int numero;
    /** Estado actual del asiento (RF-032). */
    private String estado;
    /** Referencia a la zona padre (parte del Composite). */
    private Zona zonaPadre;

    /**
     * Construye un asiento con todos sus datos.
     *
     * @param fila         letra de fila (ej: "A", "B")
     * @param numero       número del asiento
     * @param estadoInicial estado inicial (generalmente "Disponible")
     * @param zonaPadre    zona a la que pertenece
     */
    public Asiento(String fila, int numero, String estadoInicial, Zona zonaPadre) {
        this.idAsiento = zonaPadre.getNombre() + "-" + fila + numero;
        this.fila      = fila;
        this.numero    = numero;
        this.estado    = estadoInicial;
        this.zonaPadre = zonaPadre;
    }

    /**
     * Intenta reservar el asiento (RF-032).
     * Solo funciona si el asiento está en estado "Disponible".
     *
     * @return {@code true} si fue reservado exitosamente
     */
    public boolean reservar() {
        if ("Disponible".equals(estado)) {
            this.estado = "Reservado";
            return true;
        }
        return false;
    }

    /**
     * Vende el asiento (Reservado → Vendido) (RF-032).
     *
     * @return {@code true} si la transición fue válida
     */
    public boolean vender() {
        if ("Reservado".equals(estado)) {
            this.estado = "Vendido";
            return true;
        }
        return false;
    }

    /**
     * Cambia el estado del asiento a cualquier valor válido (RF-032).
     *
     * @param nuevoEstado el nuevo estado (Disponible | Reservado | Vendido | Bloqueado)
     */
    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // ==================== COMPOSITE ====================

    @Override
    public void mostrarMapa() {
        String icono = switch (estado) {
            case "Disponible" -> "🟢";
            case "Reservado"  -> "🔴";
            case "Vendido"    -> "⚫";
            case "Bloqueado"  -> "🟡";
            default           -> "⬜";
        };
        System.out.print(icono + fila + numero + " ");
    }

    @Override
    public int getCapacidadDisponible() {
        return "Disponible".equals(estado) ? 1 : 0;
    }

    @Override
    public boolean reservarAsiento(String fila, int numero) {
        return this.fila.equals(fila) && this.numero == numero && reservar();
    }

    // ==================== GETTERS Y SETTERS ====================

    /** @return identificador único del asiento */
    public String getIdAsiento() { return idAsiento; }
    /** @return fila del asiento */
    public String getFila()      { return fila; }
    /** @return número del asiento */
    public int getNumero()       { return numero; }
    /** @return estado actual */
    public String getEstado()    { return estado; }
    /** @return zona padre */
    public Zona getZonaPadre()   { return zonaPadre; }

    public void setIdAsiento(String id)  { this.idAsiento = id; }
    public void setFila(String f)        { this.fila = f; }
    public void setNumero(int n)         { this.numero = n; }
    public void setEstado(String e)      { this.estado = e; }
    public void setZonaPadre(Zona z)     { this.zonaPadre = z; }

    @Override
    public String toString() {
        return "Asiento " + fila + "-" + numero + " [" + estado + "]";
    }
}
