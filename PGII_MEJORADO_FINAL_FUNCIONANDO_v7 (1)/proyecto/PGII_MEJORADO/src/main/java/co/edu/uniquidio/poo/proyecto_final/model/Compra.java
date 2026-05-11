package co.edu.uniquidio.poo.proyecto_final.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Compra — RF-034 a RF-037, RF-006, RF-007, RF-008.
 *
 * <p>Representa la adquisición de entradas por parte de un usuario.
 * Usa el patrón <b>State</b> para gestionar el ciclo de vida del estado
 * de la compra (Creada → Pagada → Confirmada / Cancelada / Reembolsada).
 * Se construye mediante el patrón <b>Builder</b> ({@link CompraBuilder}).</p>
 *
 * <p>Estados posibles (RF-008):</p>
 * <ul>
 *   <li>Creada</li>
 *   <li>Pagada</li>
 *   <li>Confirmada</li>
 *   <li>Cancelada</li>
 *   <li>Reembolsada</li>
 *   <li>Incidencia</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 * @see CompraBuilder
 * @see EstadoCompraState
 */
public class Compra {

    /** Identificador único de la compra. */
    private String idCompra;
    /** Usuario que realizó la compra (RF-034). */
    private Usuario usuario;
    /** Evento al que pertenecen las entradas (RF-034). */
    private Evento evento;
    /** Monto total de la compra. */
    private double total;
    /** Estado actual en texto (para visualización en tabla). */
    private String estado = "Creada";
    /** Lista de entradas generadas (RF-038). */
    private List<Entrada> entradas = new ArrayList<>();
    /** Servicio adicional aplicado como Decorator (RF-009, RF-050). */
    private ServicioAdicionalDecorator servicioDecorator;
    /** Estado actual para el patrón State (RF-051). */
    private EstadoCompraState estadoActual;
    /** Fecha y hora de creación de la compra. */
    private final String fechaCreacion;

    /**
     * Constructor de uso exclusivo del Builder (patrón Builder — RF-049).
     *
     * @param builder el builder con los atributos configurados
     */
    Compra(CompraBuilder builder) {
        this.idCompra          = builder.idCompra;
        this.usuario           = builder.usuario;
        this.evento            = builder.evento;
        this.total             = builder.total;
        this.servicioDecorator = builder.servicioDecorator;
        this.estadoActual      = new EstadoCreadaState();
        this.fechaCreacion     = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // ==================== MÉTODOS DE NEGOCIO (State RF-051) ====================

    /**
     * Intenta pagar la compra.
     * Delegado al estado actual (State pattern).
     */
    public void pagar()    { estadoActual.pagar(this); }

    /**
     * Intenta cancelar la compra.
     * Delegado al estado actual (State pattern).
     */
    public void cancelar() { estadoActual.cancelar(this); }

    /**
     * Intenta confirmar la compra.
     * Delegado al estado actual (State pattern).
     */
    public void confirmar() { estadoActual.confirmar(this); }

    /**
     * Solicita reembolso de la compra.
     * Delegado al estado actual (State pattern).
     */
    public void reembolsar() { estadoActual.reembolsar(this); }

    // ==================== GETTERS ====================

    /** @return identificador único de la compra */
    public String getIdCompra() { return idCompra; }

    /** @return usuario comprador */
    public Usuario getUsuario() { return usuario; }

    /** @return evento al que pertenece la compra */
    public Evento getEvento() { return evento; }

    /**
     * Devuelve el total base de la compra SIN aplicar ningún Decorator.
     * Este método es usado internamente por los decoradores para evitar
     * la recursión infinita (RF-050).
     *
     * @return total base en pesos sin servicios adicionales
     */
    public double getTotalBase() {
        return total;
    }

    /**
     * Calcula el total real de la compra, incluyendo servicios adicionales (Decorator).
     *
     * @return total en pesos (con servicios adicionales si aplica)
     */
    public double getTotal() {
        return (servicioDecorator != null) ? servicioDecorator.getPrecio() : total;
    }

    /**
     * Devuelve la descripción de la compra, incluyendo servicios adicionales (Decorator).
     *
     * @return descripción de la compra
     */
    public String getDescripcion() {
        String base = "Compra de entradas para "
                + (evento != null ? evento.getNombre() : "Evento");
        return (servicioDecorator != null)
                ? servicioDecorator.getDescripcion()
                : base;
    }

    /** @return estado actual de la compra en texto */
    public String getEstado() { return estado; }

    /** @return lista de entradas generadas para esta compra */
    public List<Entrada> getEntradas() { return new ArrayList<>(entradas); }

    /** @return servicio adicional tipo Decorator (puede ser null) */
    public ServicioAdicionalDecorator getServicioDecorator() { return servicioDecorator; }

    /** @return estado actual (objeto State) */
    public EstadoCompraState getEstadoActual() { return estadoActual; }

    /** @return fecha y hora de creación de la compra */
    public String getFechaCreacion() { return fechaCreacion; }

    // ==================== SETTERS (usados por el patrón State) ====================

    /** @param nuevoEstado nuevo estado en texto */
    public void setEstado(String nuevoEstado) { this.estado = nuevoEstado; }

    /** @param nuevoEstado nueva instancia de estado (State pattern) */
    public void setEstadoActual(EstadoCompraState nuevoEstado) { this.estadoActual = nuevoEstado; }

    /** @param sd decorador de servicio adicional */
    public void setServicioDecorator(ServicioAdicionalDecorator sd) { this.servicioDecorator = sd; }

    /** @param id nuevo identificador */
    public void setIdCompra(String id)        { this.idCompra = id; }
    /** @param u usuario comprador */
    public void setUsuario(Usuario u)         { this.usuario = u; }
    /** @param e evento asociado */
    public void setEvento(Evento e)           { this.evento = e; }
    /** @param t monto total base */
    public void setTotal(double t)            { this.total = t; }
    /** @param lista lista de entradas */
    public void setEntradas(List<Entrada> lista) { this.entradas = lista; }

    /**
     * Agrega una entrada a la lista de entradas de esta compra (RF-038).
     *
     * @param e la entrada a agregar
     */
    public void agregarEntrada(Entrada e) { entradas.add(e); }

    @Override
    public String toString() {
        return idCompra + " | " + (evento != null ? evento.getNombre() : "?")
               + " | $" + (long) getTotal() + " | " + estado;
    }
}
