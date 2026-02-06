package co.edu.uniquidio.poo.pruevas.model;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    public String nombre,ubucacion;
    public List<Cuenta> listcuenta;

    public Banco(String nombre, String ubucacion) {
        this.nombre = nombre;
        this.ubucacion = ubucacion;
        this.listcuenta = new ArrayList<>();
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

    public void agregar(Cuenta cuenta){
        listcuenta.add(cuenta);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getListcuenta() {
        return listcuenta;
    }

    public void setListcuenta(List<Cuenta> listcuenta) {
        this.listcuenta = listcuenta;
    }

    public String getUbucacion() {
        return ubucacion;
    }

    public void setUbucacion(String ubucacion) {
        this.ubucacion = ubucacion;
    }


}
