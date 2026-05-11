package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

import java.util.List;

/**
 * Controller para gestión de usuarios (RF-012, RF-020, RF-021, RF-022).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: operaciones CRUD sobre {@link Usuario}.</p>
 * <p><b>SOLID — OCP:</b> se puede extender con nuevas validaciones sin modificar
 * los métodos existentes.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class GestionUsuariosController {

    private final SistemaGestionEventosSingleton sistema;

    /**
     * @param sistema instancia del Singleton inyectada (DIP)
     */
    public GestionUsuariosController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Crea y registra un nuevo usuario en el sistema (RF-020).
     * Valida que el correo no esté duplicado.
     *
     * @param nombre   nombre completo
     * @param correo   correo electrónico (debe ser único)
     * @param password contraseña
     * @param telefono teléfono de contacto
     * @return el {@link Usuario} creado
     * @throws IllegalArgumentException si el correo ya existe
     */
    public Usuario crearUsuario(String nombre, String correo, String password, String telefono) {
        validarCorreoDisponible(correo);
        String id = "U" + System.currentTimeMillis() % 100000;
        Usuario u = new Usuario(id, nombre.trim(), correo.trim(), password, telefono.trim());
        sistema.crearUsuario(u);
        return u;
    }

    /**
     * Lista todos los usuarios del sistema (RF-012).
     *
     * @return lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        return sistema.listarUsuarios();
    }

    /**
     * Actualiza los datos de perfil de un usuario (RF-002, RF-012).
     *
     * @param usuario   el usuario a actualizar
     * @param nombre    nuevo nombre
     * @param correo    nuevo correo
     * @param telefono  nuevo teléfono
     */
    public void actualizarUsuario(Usuario usuario, String nombre, String correo, String telefono) {
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser null.");
        if (nombre == null || nombre.isBlank())  throw new IllegalArgumentException("El nombre es obligatorio.");
        if (correo == null || correo.isBlank())  throw new IllegalArgumentException("El correo es obligatorio.");
        usuario.actualizarPerfil(nombre.trim(), correo.trim(), telefono != null ? telefono.trim() : "");
    }

    /**
     * Elimina un usuario por su identificador (RF-012).
     *
     * @param idUsuario el identificador del usuario
     */
    public void eliminarUsuario(String idUsuario) {
        sistema.eliminarUsuario(idUsuario);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo el correo a buscar
     * @return el usuario encontrado, o {@code null}
     */
    public Usuario buscarPorCorreo(String correo) {
        return sistema.listarUsuarios().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst().orElse(null);
    }

    /**
     * Verifica si un correo está disponible para registro.
     *
     * @param correo el correo a verificar
     * @return {@code true} si el correo no está registrado
     */
    public boolean correoDisponible(String correo) {
        return sistema.listarUsuarios().stream()
                .noneMatch(u -> u.getCorreo().equalsIgnoreCase(correo));
    }

    private void validarCorreoDisponible(String correo) {
        if (!correoDisponible(correo)) {
            throw new IllegalArgumentException("Ya existe una cuenta con el correo: " + correo);
        }
    }
}
