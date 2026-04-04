package co.edu.uniquidio.poo.proyecto_patrones.model;


public class Entrada {
    private String idEntrada;
    private Zona zona;
    private Asiento asiento; // puede ser null si zona sin numeración
    private double precioFinal;
    private String estado = "Activa"; // Activa, Usada, Anulada (RF-038 a RF-040)

    public Entrada(String idEntrada, Zona zona, Asiento asiento, double precioFinal) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
    }

    // RF-038
    public static Entrada generarEntrada(Zona z, Asiento a, double precio) {
        return new Entrada("E" + System.currentTimeMillis(), z, a, precio);
    }

    public void anular() { this.estado = "Anulada"; }
    public String getIdEntrada() { return idEntrada; }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
}