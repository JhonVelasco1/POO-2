package co.edu.uniquidio.poo.proyecto_final.Controller;


import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

public class LoginController {

    private SistemaGestionEventos sistemaGestionEventos;

    public LoginController(SistemaGestionEventos sistemaGestionEventos) {
        this.sistemaGestionEventos = sistemaGestionEventos;
    }

    /**
     * Coordina la autenticación con el modelo.
     */
    public Usuario autenticar(String email, String password) {
        return sistemaGestionEventos.autenticar(email, password);
    }
}