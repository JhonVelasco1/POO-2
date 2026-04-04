package co.edu.uniquidio.poo.proyecto_patrones.Controller;

import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_patrones.model.Usuario;

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