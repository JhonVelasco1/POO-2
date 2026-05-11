package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Decorador para el Seguro de Cancelación (RF-009).
 * Añade $25.000 al precio base de la compra.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class ServicioSeguroCancelacionDecorator extends ServicioAdicionalDecorator {

    private static final double PRECIO_SEGURO = 25_000;

    public ServicioSeguroCancelacionDecorator(Compra compraBase) { super(compraBase); }

    @Override
    public double getPrecio() { return compraBase.getTotalBase() + PRECIO_SEGURO; }

    @Override
    public String getDescripcion() { return compraBase.getDescripcion() + " + Seguro de Cancelación ($25.000)"; }
}
