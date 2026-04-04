package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

import java.util.List;

/**
 * Controller para gestión de usuarios (RF-012, RF-020, RF-021).
 * Principio SRP: solo gestiona operaciones sobre Usuario.
 */
public class GestionUsuariosController {

    private final SistemaGestionEventos sistema;

    public GestionUsuariosController(SistemaGestionEventos sistema) {
        this.sistema = sistema;
    }

    /** RF-020: Registrar nuevo usuario */
    public Usuario crearUsuario(String nombre, String correo, String password, String telefono) {
        // Validar que el correo no exista
        boolean existe = sistema.listarUsuarios().stream()
                .anyMatch(u -> u.getCorreo().equalsIgnoreCase(correo));
        if (existe) throw new IllegalArgumentException("Ya existe un usuario con ese correo.");

        String id = "U" + System.currentTimeMillis();
        Usuario u = new Usuario(id, nombre, correo, password, telefono);
        sistema.crearUsuario(u);
        return u;
    }

    /** RF-012: Listar todos los usuarios */
    public List<Usuario> listarUsuarios() {
        return sistema.listarUsuarios();
    }

    /** RF-002: Actualizar datos de perfil */
    public void actualizarUsuario(Usuario usuario, String nombre, String correo, String telefono) {
        if (usuario != null) {
            usuario.actualizarPerfil(nombre, correo, telefono);
        }
    }

    /** RF-012: Eliminar usuario por ID */
    public void eliminarUsuario(String idUsuario) {
        sistema.eliminarUsuario(idUsuario);
    }

    /** RF-020: Buscar usuario por correo */
    public Usuario buscarPorCorreo(String correo) {
        return sistema.listarUsuarios().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
    }

    /** Validar si un correo ya está registrado */
    public boolean correoDisponible(String correo) {
        return sistema.listarUsuarios().stream()
                .noneMatch(u -> u.getCorreo().equalsIgnoreCase(correo));
    }
}
