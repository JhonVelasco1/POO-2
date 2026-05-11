package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Clase abstracta que representa un evento programado en la plataforma.
 * Subclase base para {@link Concierto}, {@link Teatro} y {@link Conferencia}.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public abstract class Evento {

    protected String idEvento;
    protected String nombre;
    protected String categoria;
    protected String descripcion;
    protected String ciudad;
    protected String fechaHora;
    protected String estado = "Borrador";
    protected Recinto recinto;
    protected String politicasCancelacion = "Sin política definida.";
    protected String politicasReembolso   = "Sin política definida.";
    protected int aforoMaximo;

    protected Evento(String idEvento, String nombre, String categoria, String descripcion,
                     String ciudad, String fechaHora, Recinto recinto) {
        this.idEvento    = idEvento;
        this.nombre      = nombre;
        this.categoria   = categoria;
        this.descripcion = descripcion;
        this.ciudad      = ciudad;
        this.fechaHora   = fechaHora;
        this.recinto     = recinto;
    }

    /**
     * Publica el evento (Borrador → Publicado).
     * Notifica solo si el singleton ya está completamente inicializado (evita recursión).
     */
    public void publicar() {
        this.estado = "Publicado";
        SistemaGestionEventosSingleton s = SistemaGestionEventosSingleton.getInstanciaONull();
        if (s != null) {
            s.notificarTodos("Evento '" + nombre + "' publicado en " + ciudad);
        }
    }

    /** Pausa el evento (Publicado → Pausado). */
    public void pausar() {
        this.estado = "Pausado";
    }

    /**
     * Cancela el evento. Notifica a todos los observadores (RF-017).
     */
    public void cancelar() {
        this.estado = "Cancelado";
        SistemaGestionEventosSingleton s = SistemaGestionEventosSingleton.getInstanciaONull();
        if (s != null) {
            s.notificarTodos("Evento '" + nombre + "' ha sido cancelado.");
            s.registrarIncidencia(new Incidencia("EVENTO_CANCELADO",
                "El evento '" + nombre + "' fue cancelado."));
        }
    }

    /** Marca el evento como finalizado. */
    public void finalizar() {
        this.estado = "Finalizado";
    }

    public String getIdEvento()             { return idEvento; }
    public String getNombre()               { return nombre; }
    public String getCategoria()            { return categoria; }
    public String getDescripcion()          { return descripcion; }
    public String getCiudad()               { return ciudad; }
    public String getFechaHora()            { return fechaHora; }
    public String getEstado()               { return estado; }
    public Recinto getRecinto()             { return recinto; }
    public String getPoliticasCancelacion() { return politicasCancelacion; }
    public String getPoliticasReembolso()   { return politicasReembolso; }

    public void setIdEvento(String id)                { this.idEvento = id; }
    public void setNombre(String nombre)              { this.nombre = nombre; }
    public void setCategoria(String categoria)        { this.categoria = categoria; }
    public void setDescripcion(String descripcion)    { this.descripcion = descripcion; }
    public void setCiudad(String ciudad)              { this.ciudad = ciudad; }
    public void setFechaHora(String fechaHora)        { this.fechaHora = fechaHora; }
    public void setEstado(String estado)              { this.estado = estado; }
    public void setRecinto(Recinto recinto)           { this.recinto = recinto; }
    public void setPoliticasCancelacion(String p)     { this.politicasCancelacion = p; }
    public void setPoliticasReembolso(String p)       { this.politicasReembolso = p; }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ") - " + ciudad + " | " + estado;
    }
}
