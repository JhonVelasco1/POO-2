package co.edu.uniquidio.poo.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario — RF-020, RF-021, RF-022.
 * Implementa {@link ObserverNotificacion} para recibir notificaciones
 * automáticas del sistema (patrón Observer, RF-051).
 *
 * <p>Atributos requeridos por el enunciado:</p>
 * <ul>
 *   <li>{@code idUsuario} — identificador único</li>
 *   <li>{@code nombreCompleto} — nombre completo</li>
 *   <li>{@code correo} — correo electrónico</li>
 *   <li>{@code telefono} — número de teléfono</li>
 *   <li>{@code metodosPago} — métodos de pago simulados (RF-021)</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Usuario implements ObserverNotificacion {

    /** Identificador único del usuario. */
    private String idUsuario;
    /** Nombre completo del usuario. */
    private String nombreCompleto;
    /** Correo electrónico (usado para autenticación). */
    private String correo;
    /** Contraseña (simulada, sin cifrado para efectos del proyecto). */
    private String password;
    /** Número de teléfono de contacto. */
    private String telefono;
    /** RF-021: Métodos de pago simulados. */
    private List<String> metodosPago;
    /** RF-022: Compras asociadas al usuario. */
    private List<Compra> compras = new ArrayList<>();
    /** Historial de notificaciones recibidas (RF-008). */
    private final List<String> notificaciones = new ArrayList<>();

    /**
     * Construye un nuevo usuario con todos sus datos básicos.
     *
     * @param idUsuario      identificador único
     * @param nombreCompleto nombre completo
     * @param correo         correo electrónico
     * @param password       contraseña
     * @param telefono       número de teléfono
     */
    public Usuario(String idUsuario, String nombreCompleto, String correo,
                   String password, String telefono) {
        this.idUsuario       = idUsuario;
        this.nombreCompleto  = nombreCompleto;
        this.correo          = correo;
        this.password        = password;
        this.telefono        = telefono;
        this.metodosPago     = new ArrayList<>(List.of("Tarjeta Visa", "PayPal", "PSE"));
    }

    // ==================== RF-001, RF-020 ====================

    /**
     * Verifica las credenciales de inicio de sesión (RF-001).
     *
     * @param email correo a verificar
     * @param pass  contraseña a verificar
     * @return {@code true} si las credenciales coinciden
     */
    public boolean iniciarSesion(String email, String pass) {
        return this.correo.equals(email) && this.password.equals(pass);
    }

    // ==================== RF-002 ====================

    /**
     * Actualiza los datos del perfil del usuario (RF-002).
     *
     * @param nuevoNombre    nuevo nombre completo
     * @param nuevoCorreo    nuevo correo electrónico
     * @param nuevoTelefono  nuevo número de teléfono
     */
    public void actualizarPerfil(String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        this.nombreCompleto = nuevoNombre;
        this.correo         = nuevoCorreo;
        this.telefono       = nuevoTelefono;
    }

    // ==================== OBSERVER ====================

    /**
     * Recibe y almacena una notificación del sistema (RF-008).
     * Implementación del patrón Observer.
     *
     * @param mensaje el mensaje de notificación
     */
    @Override
    public void actualizar(String mensaje) {
        notificaciones.add(mensaje);
        System.out.println("🔔 Notificación para " + nombreCompleto + ": " + mensaje);
    }

    // ==================== RF-022 ====================

    /**
     * Asocia una compra a este usuario (RF-022).
     *
     * @param c la compra a asociar
     */
    public void agregarCompra(Compra c) { compras.add(c); }

    /**
     * Indica si el usuario tiene rol de administrador.
     * Convención: los IDs de admin comienzan con "A".
     *
     * @return {@code true} si es administrador
     */
    public boolean esAdmin() {
        return idUsuario != null && idUsuario.startsWith("A");
    }

    // ==================== GETTERS Y SETTERS ====================

    /** @return identificador único del usuario */
    public String getIdUsuario()      { return idUsuario; }
    /** @return correo electrónico */
    public String getCorreo()         { return correo; }
    /** @return contraseña */
    public String getPassword()       { return password; }
    /** @return nombre completo */
    public String getNombreCompleto() { return nombreCompleto; }
    /** @return teléfono */
    public String getTelefono()       { return telefono; }
    /** @return lista de compras del usuario */
    public List<Compra> getCompras()  { return compras; }
    /** @return métodos de pago simulados */
    public List<String> getMetodosPago()      { return metodosPago; }
    /** @return historial de notificaciones recibidas */
    public List<String> getNotificaciones()   { return new ArrayList<>(notificaciones); }

    public void setIdUsuario(String id)            { this.idUsuario = id; }
    public void setNombreCompleto(String n)         { this.nombreCompleto = n; }
    public void setCorreo(String c)                { this.correo = c; }
    public void setPassword(String p)              { this.password = p; }
    public void setTelefono(String t)              { this.telefono = t; }
    public void setMetodosPago(List<String> m)     { this.metodosPago = m; }
    public void setCompras(List<Compra> compras)   { this.compras = compras; }

    @Override
    public String toString() {
        return nombreCompleto + " <" + correo + ">";
    }
}
