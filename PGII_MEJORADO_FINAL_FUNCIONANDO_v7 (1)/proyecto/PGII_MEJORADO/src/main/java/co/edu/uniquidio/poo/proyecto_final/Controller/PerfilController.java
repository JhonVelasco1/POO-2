package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.*;

/**
 * Controller para gestión del perfil de usuario (RF-002, RF-020, RF-021).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: actualizar datos del perfil.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class PerfilController {

    private final SistemaGestionEventosSingleton sistema;

    public PerfilController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Devuelve el usuario actualmente logueado (RF-002).
     *
     * @return usuario logueado, o {@code null}
     */
    public Usuario obtenerUsuarioPerfil() {
        return sistema.getUsuarioLogueado();
    }

    /**
     * Actualiza los datos del perfil del usuario logueado (RF-002).
     *
     * @param usuario   el usuario a actualizar
     * @param nombre    nuevo nombre
     * @param correo    nuevo correo
     * @param telefono  nuevo teléfono
     */
    public void actualizarDatosUsuario(Usuario usuario, String nombre,
                                       String correo, String telefono) {
        if (usuario == null) throw new IllegalStateException("No hay usuario logueado.");
        usuario.actualizarPerfil(
            nombre  != null ? nombre.trim()  : usuario.getNombreCompleto(),
            correo  != null ? correo.trim()  : usuario.getCorreo(),
            telefono!= null ? telefono.trim(): usuario.getTelefono()
        );
    }
}
