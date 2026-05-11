package co.edu.uniquidio.poo.proyecto_final.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad Incidencia — registra eventos anómalos o excepciones operativas (RF-041, RF-042).
 *
 * <p>Tipos de incidencia comunes:</p>
 * <ul>
 *   <li>PAGO_FALLIDO           — error en el proceso de pago</li>
 *   <li>DOBLE_COMPRA_ASIENTO   — intento de comprar un asiento ya vendido</li>
 *   <li>CANCELACION_POST_PAGO  — cancelación luego del pago</li>
 *   <li>EVENTO_CANCELADO       — cancelación masiva del evento</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Incidencia {

    /** Identificador único de la incidencia. */
    private String idIncidencia;
    /** Tipo de incidencia (ej: PAGO_FALLIDO, DOBLE_COMPRA_ASIENTO). */
    private String tipo;
    /** Descripción detallada del problema. */
    private String descripcion;
    /** Fecha y hora de registro. */
    private String fecha;
    /** Entidad afectada (evento, compra, usuario). */
    private String entidadAfectada;

    /**
     * Construye una nueva incidencia con tipo y descripción.
     * La fecha se asigna automáticamente al momento de la creación.
     *
     * @param tipo        tipo de incidencia
     * @param descripcion descripción del problema
     */
    public Incidencia(String tipo, String descripcion) {
        this.idIncidencia   = "INC-" + System.nanoTime() % 100000;
        this.tipo           = tipo;
        this.descripcion    = descripcion;
        this.fecha          = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.entidadAfectada = "Sistema";
    }

    /**
     * Construye una incidencia con todos los atributos.
     *
     * @param tipo            tipo de incidencia
     * @param descripcion     descripción
     * @param entidadAfectada entidad afectada (ej: "Evento E001")
     */
    public Incidencia(String tipo, String descripcion, String entidadAfectada) {
        this(tipo, descripcion);
        this.entidadAfectada = entidadAfectada;
    }

    // ==================== GETTERS Y SETTERS ====================

    public String getIdIncidencia()        { return idIncidencia; }
    public String getTipo()                { return tipo; }
    public String getDescripcion()         { return descripcion; }
    public String getFecha()               { return fecha; }
    public String getEntidadAfectada()     { return entidadAfectada; }

    public void setIdIncidencia(String id) { this.idIncidencia = id; }
    public void setTipo(String t)          { this.tipo = t; }
    public void setDescripcion(String d)   { this.descripcion = d; }
    public void setFecha(String f)         { this.fecha = f; }
    public void setEntidadAfectada(String e){ this.entidadAfectada = e; }

    @Override
    public String toString() {
        return "[" + fecha + "] " + tipo + ": " + descripcion;
    }
}
