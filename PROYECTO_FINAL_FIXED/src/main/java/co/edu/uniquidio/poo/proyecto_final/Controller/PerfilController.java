package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

public class PerfilController {

    private SistemaGestionEventos sistemaGestionEventos;

    public PerfilController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Obtiene el usuario logueado actualmente en el sistema.
     */
    public Usuario obtenerUsuarioPerfil() {
        return sistemaGestionEventos.getUsuarioLogueado();
    }

    /**
     * Coordina la actualización de los datos del usuario a través del modelo.
     */
    public void actualizarDatosUsuario(Usuario usuario, String nombre, String correo, String telefono) {
        if (usuario != null) {
            usuario.actualizarPerfil(nombre, correo, telefono);
        }
    }
}
