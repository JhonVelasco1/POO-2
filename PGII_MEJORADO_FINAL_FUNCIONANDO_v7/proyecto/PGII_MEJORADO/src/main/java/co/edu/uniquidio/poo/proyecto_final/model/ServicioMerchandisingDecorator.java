package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Decorador para Merchandising (RF-009).
 * Añade $45.000 al precio base de la compra.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ServicioMerchandisingDecorator extends ServicioAdicionalDecorator {

    private static final double PRECIO_MERCH = 45_000;

    public ServicioMerchandisingDecorator(Compra compraBase) { super(compraBase); }

    @Override
    public double getPrecio() { return compraBase.getTotalBase() + PRECIO_MERCH; }

    @Override
    public String getDescripcion() { return compraBase.getDescripcion() + " + Merchandising ($45.000)"; }
}
