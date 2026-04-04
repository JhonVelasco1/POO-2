package co.edu.uniquidio.poo.proyecto_patrones.model;

public class Incidencia {
    private String idIncidencia;
    private String tipo;
    private String descripcion;
    private String fecha;

    public Incidencia(String tipo, String descripcion) {
        this.idIncidencia = "I" + System.currentTimeMillis();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = java.time.LocalDate.now().toString();
    }

    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }

    public String getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(String idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}