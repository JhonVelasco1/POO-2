package co.edu.uniquidio.poo.proyecto_patrones.model;



import java.util.ArrayList;
import java.util.List;

public class Usuario implements Observer {  // Observer para notificaciones
    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String password;  // simulada
    private List<String> metodosPago; // simulados
    private List<Compra> compras = new ArrayList<>();

    public Usuario(String idUsuario, String nombreCompleto, String correo, String password, String telefono) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.password = password;
        this.telefono = telefono;
        this.metodosPago = new ArrayList<>(List.of("Tarjeta Visa", "PayPal"));
    }

    // RF-001, RF-020
    public boolean iniciarSesion(String email, String pass) {
        return this.correo.equals(email) && this.password.equals(pass);
    }

    // RF-002 Gestionar perfil
    public void actualizarPerfil(String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        this.nombreCompleto = nuevoNombre;
        this.correo = nuevoCorreo;
        this.telefono = nuevoTelefono;
    }

    public boolean esAdmin() {
        return idUsuario.startsWith("A");
    }

    // Getters y setters (todos los RF que usan Usuario)
    public String getIdUsuario() { return idUsuario; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public List<Compra> getCompras() { return compras; }

    // Observer
    @Override
    public void actualizar(String mensaje) {
        System.out.println("Notificación para " + nombreCompleto + ": " + mensaje);
    }

    public void agregarCompra(Compra c) { compras.add(c); }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getMetodosPago() {
        return metodosPago;
    }

    public void setMetodosPago(List<String> metodosPago) {
        this.metodosPago = metodosPago;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}