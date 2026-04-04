package co.edu.uniquidio.poo.proyecto_final.model;


public abstract class Evento {
    protected String idEvento;
    protected String nombre;
    protected String categoria;
    protected String descripcion;
    protected String ciudad;
    protected String fechaHora;
    protected String estado = "Borrador";
    protected Recinto recinto;
    protected String politicasCancelacion;

    public Evento(String idEvento, String nombre, String categoria, String descripcion,
                  String ciudad, String fechaHora, Recinto recinto) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
        this.recinto = recinto;
    }

    // RF-024 - Método cancelar() CORREGIDO (ya no da error)
    public void cancelar() {
        this.estado = "Cancelado";
        // Llamada segura al Singleton
        SistemaGestionEventos.getInstance().notificarTodos("Evento " + nombre + " cancelado");
    }
    public String getIdEvento() { return idEvento; }   // ← Agregar


    public void publicar() { this.estado = "Publicado"; }
    public String getEstado() { return estado; }
    public String getNombre() { return nombre; }
    public Recinto getRecinto() { return recinto; }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public String getPoliticasCancelacion() {
        return politicasCancelacion;
    }

    public void setPoliticasCancelacion(String politicasCancelacion) {
        this.politicasCancelacion = politicasCancelacion;
    }
}