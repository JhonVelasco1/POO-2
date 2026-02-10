package co.edu.uniquidio.poo.tallerbanco.model;


public class Cuenta_corriente extends Cuenta {

    private  double SOBREGIRO_MAXIMO = -500000;
    private  double COBRO_MENSUAL = 15000;
    private  double INTERES_MENSUAL = 0.02;

    public Cuenta_corriente(String numero_cuenta, double saldo_actual, String fecha_apertura, Usuario titular) {

        super(numero_cuenta, saldo_actual, fecha_apertura, titular);
    }

    @Override
    public void Depositar(double cantidad) {

        if (estado != Estado.ACTIVO) {
            System.out.println("Cuenta bloqueada");
            return;
        }

        if (cantidad <= 0) {
            System.out.println("Cantidad inválida");
            return;
        }

        saldo_actual += cantidad;

        System.out.println("Depósito exitoso. Saldo: " + saldo_actual);
    }

    @Override
    public void retirar(double cantidad) {

        if (estado != Estado.ACTIVO) {
            System.out.println("Cuenta bloqueada");
            return;
        }

        if (saldo_actual - cantidad < SOBREGIRO_MAXIMO) {
            System.out.println("Supera el límite de sobregiro");
            return;
        }

        saldo_actual -= cantidad;

        System.out.println("Retiro exitoso. Saldo: " + saldo_actual);
    }


    public void cobrarComisionMensual() {
        saldo_actual -= COBRO_MENSUAL;
        System.out.println("Se cobró comisión mensual: " + COBRO_MENSUAL);
    }


    public void aplicarInteresSobregiro() {

        if (saldo_actual < 0) {

            double interes = saldo_actual * INTERES_MENSUAL;

            saldo_actual -= interes; // aumenta la deuda

            System.out.println("Interés por sobregiro aplicado: " + interes);
        }
    }

    public double getSOBREGIRO_MAXIMO() {
        return SOBREGIRO_MAXIMO;
    }

    public void setSOBREGIRO_MAXIMO(double SOBREGIRO_MAXIMO) {
        this.SOBREGIRO_MAXIMO = SOBREGIRO_MAXIMO;
    }

    public double getCOBRO_MENSUAL() {
        return COBRO_MENSUAL;
    }

    public void setCOBRO_MENSUAL(double COBRO_MENSUAL) {
        this.COBRO_MENSUAL = COBRO_MENSUAL;
    }

    public double getINTERES_MENSUAL() {
        return INTERES_MENSUAL;
    }

    public void setINTERES_MENSUAL(double INTERES_MENSUAL) {
        this.INTERES_MENSUAL = INTERES_MENSUAL;
    }
}
