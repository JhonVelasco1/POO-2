package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

/**
 * Controller de registro (RF-001, RF-020).
 * Principio SRP: maneja solo la lógica de creación de cuenta nueva.
 */
public class RegistroController {

    private final SistemaGestionEventos sistema;
    private final GestionUsuariosController usuariosController;

    public RegistroController(SistemaGestionEventos sistema) {
        this.sistema = sistema;
        this.usuariosController = new GestionUsuariosController(sistema);
    }

    /**
     * Registra un nuevo usuario validando los datos de entrada.
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Usuario registrar(String nombre, String correo, String password,
                             String confirmarPassword, String telefono) {

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (correo == null || !correo.contains("@"))
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        if (!password.equals(confirmarPassword))
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        if (!usuariosController.correoDisponible(correo))
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo.");

        return usuariosController.crearUsuario(nombre.trim(), correo.trim(), password, telefono.trim());
    }
}
