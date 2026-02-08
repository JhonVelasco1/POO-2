package co.edu.uniquidio.poo.pruevas.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

        private final String cedula;   // inmutable
        private String nombre;
        private String telefono;
        private String correo;

        private List<Cuenta> cuentasUsuario;

        public Usuario(String cedula, String nombre, String telefono, String correo) {
            this.cedula = cedula;
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
            this.cuentasUsuario = new ArrayList<>();
        }


        public void agregarCuenta(Cuenta cuenta) {
            cuentasUsuario.add(cuenta);
        }

        public void mostrarCuentas() {
            for (Cuenta c : cuentasUsuario) {
                c.mostrarInformacion();
            }
        }


        public String getCedula() {
            return cedula;
        }

        public String getNombre() {
            return nombre;
        }

        public List<Cuenta> getCuentas() {
            return cuentasUsuario;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public List<Cuenta> getCuentasUsuario() {
        return cuentasUsuario;
    }

    public void setCuentasUsuario(List<Cuenta> cuentasUsuario) {
        this.cuentasUsuario = cuentasUsuario;
    }
}
