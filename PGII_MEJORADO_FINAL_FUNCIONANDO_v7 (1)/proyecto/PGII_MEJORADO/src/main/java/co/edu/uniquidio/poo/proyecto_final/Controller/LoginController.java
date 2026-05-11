package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

/**
 * Controller de autenticación (RF-001, RF-020).
 *
 * <p><b>SOLID — SRP:</b> esta clase tiene una única responsabilidad:
 * coordinar la autenticación delegando al modelo. No maneja UI.</p>
 *
 * <p><b>SOLID — DIP:</b> depende de la abstracción
 * {@link SistemaGestionEventosSingleton} inyectada, no de implementaciones concretas.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class LoginController {

    /** Sistema central (Singleton) inyectado. */
    private final SistemaGestionEventosSingleton sistema;

    /**
     * Construye el controller con el sistema inyectado (DIP).
     *
     * @param sistema instancia del Singleton
     */
    public LoginController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
    }

    /**
     * Autentica al usuario con correo y contraseña (RF-001).
     *
     * @param email    correo del usuario
     * @param password contraseña
     * @return el {@link Usuario} autenticado, o {@code null} si falla
     */
    public Usuario autenticar(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        return sistema.autenticar(email.trim(), password);
    }
}
