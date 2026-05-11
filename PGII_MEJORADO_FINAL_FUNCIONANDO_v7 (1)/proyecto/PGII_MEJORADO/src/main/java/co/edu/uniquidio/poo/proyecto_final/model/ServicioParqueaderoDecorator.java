package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Decorador para Parqueadero (RF-009).
 * Añade $15.000 al precio base de la compra.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ServicioParqueaderoDecorator extends ServicioAdicionalDecorator {

    private static final double PRECIO_PARQUEADERO = 15_000;

    public ServicioParqueaderoDecorator(Compra compraBase) { super(compraBase); }

    @Override
    public double getPrecio() { return compraBase.getTotalBase() + PRECIO_PARQUEADERO; }

    @Override
    public String getDescripcion() { return compraBase.getDescripcion() + " + Parqueadero ($15.000)"; }
}
