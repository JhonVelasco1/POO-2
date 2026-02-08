package co.edu.uniquidio.poo.pruevas.model;

public abstract class Cuenta {
    private String numero_cuenta;
    private Usuario titular;
    protected double saldo_actual;
    private String fecha_apertura;
    public Estado estado;

    public Cuenta(String numero_cuenta, double saldo_actual, String fecha_apertura, Usuario titular) {
        this.numero_cuenta = numero_cuenta;
        this.estado = Estado.ACTIVO;
        this.saldo_actual = saldo_actual;
        this.fecha_apertura = fecha_apertura;
        this.titular = titular;
        titular.agregarCuenta(this);
    }
    public Usuario getTitular() {
        return titular;
    }

    public abstract void Depositar (double cantidad);
    public abstract void retirar (double cantidad);


    public void mostrarInformacion() {
        System.out.println("Cuenta: " + numero_cuenta);
        System.out.println("Titular: " + titular);
        System.out.println("Saldo: " + saldo_actual);
        System.out.println("Estado: " + estado);
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

    public void setTitular(Usuario titular) {
        this.titular = titular;
    }
}
