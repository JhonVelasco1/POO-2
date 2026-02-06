package co.edu.uniquidio.poo.pruevas.model;

public class Cuenta {
    private String numero_cuenta;
    public String titular;
    protected double saldo_actual;
    public String fecha_apertura;
    public Estado estado;

    public Cuenta(String numero_cuenta, Estado estado, double saldo_actual, String fecha_apertura, String titular) {
        this.numero_cuenta = numero_cuenta;
        this.estado = estado;
        this.saldo_actual = saldo_actual;
        this.fecha_apertura = fecha_apertura;
        this.titular = titular;
    }

    public void Depositar (double cantidad){
        if (estado == Estado.ACTIVO) {
            if (cantidad > 0) {
                setSaldo_actual(saldo_actual + cantidad);
                System.out.println("su salario actual es de" + saldo_actual);
            }
            System.out.println("no se puede consignar un valor menor a 0 ");
        }
        System.out.println("su cuenta se encuentra suspendida");

    }

    public void retirar (double cantidad){
        if (estado==Estado.ACTIVO) {
            if (cantidad > 0) {
                setSaldo_actual(saldo_actual - cantidad);
                System.out.println("su salario actual es de" + saldo_actual + "y se retiro" + cantidad);
            }
            System.out.println("no se puede retirar un valor menor a 0 ");
        }
        System.out.println("su cuenta se encuentra suspendida");

    }


    @Override
    public String toString() {
        return "Cuenta{" +
                "numero_cuenta='" + numero_cuenta + '\'' +
                ", titular='" + titular + '\'' +
                ", saldo_actual=" + saldo_actual +
                ", fecha_apertura='" + fecha_apertura + '\'' +
                ", estado=" + estado +
                '}';
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getFecha_apertura() {
        return fecha_apertura;
    }

    public void setFecha_apertura(String fecha_apertura) {
        this.fecha_apertura = fecha_apertura;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(double saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
