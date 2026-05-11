package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Decorador para el servicio de Acceso VIP (RF-009).
 * Añade $80.000 al precio base de la compra.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ServicioVIPDecorator extends ServicioAdicionalDecorator {

    private static final double PRECIO_VIP = 80_000;

    public ServicioVIPDecorator(Compra compraBase) { super(compraBase); }

    @Override
    public double getPrecio() { return compraBase.getTotalBase() + PRECIO_VIP; }

    @Override
    public String getDescripcion() { return compraBase.getDescripcion() + " + Acceso VIP ($80.000)"; }
}
