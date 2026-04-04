package co.edu.uniquidio.poo.proyecto_final.Controller;


import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

import java.util.List;

public class PerfilController {

    private SistemaGestionEventos sistemaGestionEventos;

    public PerfilController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Obtiene el usuario que se va a mostrar (según tu lógica, el primero de la lista).
     */
    public Usuario obtenerUsuarioPerfil() {
        List<Usuario> usuarios = sistemaGestionEventos.listarUsuarios();
        if (usuarios != null && !usuarios.isEmpty()) {
            return usuarios.get(0);
        }
        return null;
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