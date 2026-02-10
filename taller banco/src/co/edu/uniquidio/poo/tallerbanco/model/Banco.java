package co.edu.uniquidio.poo.tallerbanco.model;


import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nombre;
    private String ubicacion;
    private List<Cuenta> listcuenta;
    private List<Usuario> listrusuario;

    public Banco(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.listcuenta = new ArrayList<>();
        this.listrusuario=new ArrayList<>();
    }

    public void agregarcuenta(Cuenta cuenta) {
        listcuenta.add(cuenta);
    }
    public void agregarusuario(Usuario usuario) {
        listrusuario.add(usuario);
    }




    public void bloquearCuenta(String numeroCuenta) {

        for (Cuenta cuenta : listcuenta) {

            if (cuenta.getNumero_cuenta().equals(numeroCuenta)) {
                cuenta.setEstado(Estado.BLOQUEADO);
                System.out.println("Cuenta bloqueada correctamente");
                return;
            }
        }

        System.out.println("Cuenta no encontrada");
    }

    public void desbloquearCuenta(String numeroCuenta) {

        for (Cuenta cuenta : listcuenta) {

            if (cuenta.getNumero_cuenta().equals(numeroCuenta)) {
                cuenta.setEstado(Estado.ACTIVO);
                System.out.println("Cuenta desbloqueada correctamente");
                return;
            }
        }

        System.out.println("Cuenta no encontrada");
    }



    public void cambiarCuentaNominaACorriente() {

        for (int i = 0; i < listcuenta.size(); i++) {

            Cuenta c = listcuenta.get(i);

            if (c instanceof Cuenta_nomina nomina &&
                    nomina.getEstado() == Estado.ACTIVO &&
                    nomina.debeConvertirse()) {

                Cuenta_corriente nueva = new Cuenta_corriente(
                        nomina.getNumero_cuenta(),
                        nomina.getSaldo_actual(),
                        nomina.getFecha_apertura(),
                        nomina.getNombre()
                );

                listcuenta.set(i, nueva);

                System.out.println("Cuenta nómina convertida a corriente: "
                        + nomina.getNumero_cuenta());
            }
        }
    }


    public void procesarMes() {

        for (Cuenta c : listcuenta) {

            if (c instanceof Cuenta_nomina nomina) {
                nomina.procesarMes();
            }

            if (c instanceof Cuenta_corriente corriente) {
                corriente.cobrarComisionMensual();
                corriente.aplicarInteresSobregiro();
            }

            if (c instanceof Cuenta_Ahorros ahorro) {
                ahorro.generarInteresMensual();
            }
        }

        cambiarCuentaNominaACorriente();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Cuenta> getListcuenta() {
        return listcuenta;
    }

    public void setListcuenta(List<Cuenta> listcuenta) {
        this.listcuenta = listcuenta;
    }

    public List<Usuario> getListrusuario() {
        return listrusuario;
    }

    public void setListrusuario(List<Usuario> listrusuario) {
        this.listrusuario = listrusuario;
    }
}





