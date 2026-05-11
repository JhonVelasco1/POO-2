package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Strategy para pago con tarjeta de crédito/débito (RF-021).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class PagoTarjetaStrategy implements PagoStrategyInterface {

    @Override
    public boolean procesarPago(double monto, String informacion) {
        System.out.println("💳 Procesando pago con tarjeta por $" + String.format("%,.0f", monto));
        System.out.println("   Tarjeta: " + (informacion != null ? "****" + informacion.substring(
                Math.max(0, informacion.length() - 4)) : "****"));
        return true;
    }

    @Override
    public String getNombreMetodo() { return "Tarjeta de Crédito/Débito"; }
}
