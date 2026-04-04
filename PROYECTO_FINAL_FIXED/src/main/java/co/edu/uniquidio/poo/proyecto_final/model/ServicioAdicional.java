package co.edu.uniquidio.poo.proyecto_final.model;

public abstract class ServicioAdicional {
    protected Compra compraBase;

    public ServicioAdicional(Compra compraBase) {
        this.compraBase = compraBase;
    }

    public abstract double getPrecio();
    public abstract String getDescripcion();

}