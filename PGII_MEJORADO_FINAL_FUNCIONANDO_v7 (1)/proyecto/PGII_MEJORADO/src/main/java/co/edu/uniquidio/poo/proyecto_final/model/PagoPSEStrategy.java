package co.edu.uniquidio.poo.proyecto_final.model;

/**
 * Strategy para pago via PSE (Pagos Seguros en Línea) (RF-021).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class PagoPSEStrategy implements PagoStrategyInterface {

    @Override
    public boolean procesarPago(double monto, String informacion) {
        System.out.println("🏦 Procesando pago PSE por $" + String.format("%,.0f", monto));
        System.out.println("   Banco: " + (informacion != null ? informacion : "No especificado"));
        return true;
    }

    @Override
    public String getNombreMetodo() { return "PSE (Pagos Seguros en Línea)"; }
}
