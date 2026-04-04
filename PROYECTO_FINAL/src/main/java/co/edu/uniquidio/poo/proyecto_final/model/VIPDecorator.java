package co.edu.uniquidio.poo.proyecto_final.model;

public class VIPDecorator extends ServicioAdicional {
    public VIPDecorator(Compra compraBase) {
        super(compraBase);
    }

    @Override
    public double getPrecio() {
        return compraBase.getTotal() + 80000;  // ¡Ahora funciona!
    }

    @Override
    public String getDescripcion() {
        return compraBase.getDescripcion() + " + Acceso VIP (precio extra $80.000)"; // ¡Ahora funciona!
    }

}