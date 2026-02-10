package co.edu.uniquidio.poo.tallerbanco.model;


public class Cuenta_Ahorros extends Cuenta {
    private  double SALDO_MINIMO = 100000;
    private  double TASA_ANUAL = 0.036;
    private  double TASA_MENSUAL = TASA_ANUAL / 12;

    public Cuenta_Ahorros(String numero_cuenta, double saldo_actual, String fecha_apertura, Usuario titular) {

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

        if (saldo_actual - cantidad < SALDO_MINIMO) {
            System.out.println("No puede quedar por debajo del saldo mínimo: " + SALDO_MINIMO);
            return;
        }

        saldo_actual -= cantidad;

        System.out.println("Retiro exitoso. Saldo: " + saldo_actual);
    }


    public void generarInteresMensual() {

        if (estado != Estado.ACTIVO) return;

        double interes = saldo_actual * TASA_MENSUAL;

        saldo_actual -= interes;

        System.out.println("Interés mensual aplicado: +" + interes +
                " | Nuevo saldo: " + saldo_actual);
    }

    public double getTASA_ANUAL() {
        return TASA_ANUAL;
    }



    public double getTASA_MENSUAL() {
        return TASA_MENSUAL;
    }



    public double getSALDO_MINIMO() {
        return SALDO_MINIMO;
    }

    public void setSALDO_MINIMO(double SALDO_MINIMO) {
        this.SALDO_MINIMO = SALDO_MINIMO;
    }

    public void setTASA_ANUAL(double TASA_ANUAL) {
        this.TASA_ANUAL = TASA_ANUAL;
    }

    public void setTASA_MENSUAL(double TASA_MENSUAL) {
        this.TASA_MENSUAL = TASA_MENSUAL;
    }
}
