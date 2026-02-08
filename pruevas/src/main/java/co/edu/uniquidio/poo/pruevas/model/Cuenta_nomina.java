package co.edu.uniquidio.poo.pruevas.model;

public class Cuenta_nomina extends Cuenta {

        private double COMISION = 8000;
        private boolean recibioSalarioEsteMes;
        private int mesesSinSalario;

        public Cuenta_nomina(String numero_cuenta, double saldo_actual, String fecha_apertura, Usuario titular) {
            super(numero_cuenta, saldo_actual, fecha_apertura, titular);

            this.recibioSalarioEsteMes = false;
            this.mesesSinSalario = 0;
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


            recibioSalarioEsteMes = true;
            mesesSinSalario = 0;

            System.out.println("Salario recibido. Saldo: " + saldo_actual);
        }

        @Override
        public void retirar(double cantidad) {

            if (estado != Estado.ACTIVO) {
                System.out.println("Cuenta bloqueada");
                return;
            }

            if (cantidad > saldo_actual) {
                System.out.println("Fondos insuficientes");
                return;
            }

            saldo_actual -= cantidad;

            System.out.println("Retiro exitoso. Saldo: " + saldo_actual);
        }


        public void procesarMes() {

            if (recibioSalarioEsteMes==false) {

                saldo_actual -= COMISION;
                mesesSinSalario++;

                System.out.println("Se cobró comisión por no recibir salario");
            }

            recibioSalarioEsteMes = false;
        }


        public boolean debeConvertirse() {
            return mesesSinSalario >= 3;
        }

    public double getCOMISION() {
        return COMISION;
    }

    public void setCOMISION(double COMISION) {
        this.COMISION = COMISION;
    }

    public boolean isRecibioSalarioEsteMes() {
        return recibioSalarioEsteMes;
    }

    public void setRecibioSalarioEsteMes(boolean recibioSalarioEsteMes) {
        this.recibioSalarioEsteMes = recibioSalarioEsteMes;
    }

    public int getMesesSinSalario() {
        return mesesSinSalario;
    }

    public void setMesesSinSalario(int mesesSinSalario) {
        this.mesesSinSalario = mesesSinSalario;
    }
}

