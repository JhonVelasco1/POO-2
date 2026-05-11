package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Strategy para pago en efectivo en punto de venta (RF-021).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class PagoEfectivoStrategy implements PagoStrategyInterface {

    @Override
    public boolean procesarPago(double monto, String informacion) {
        System.out.println("💵 Pago en efectivo registrado: $" + String.format("%,.0f", monto));
        System.out.println("   Referencia: " + (informacion != null ? informacion : "N/A"));
        return true;
    }

    @Override
    public String getNombreMetodo() { return "Efectivo / Punto de Venta"; }
}
