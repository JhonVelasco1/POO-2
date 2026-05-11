package co.edu.uniquidio.poo.proyecto_final.Controller;

import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;

/**
 * Controller para registro de nuevos usuarios (RF-001, RF-020).
 *
 * <p><b>SOLID — SRP:</b> única responsabilidad: validar y crear cuentas nuevas.
 * Delega la persistencia a {@link GestionUsuariosController}.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class RegistroController {

    private final SistemaGestionEventosSingleton sistema;
    private final GestionUsuariosController usuariosController;

    public RegistroController(SistemaGestionEventosSingleton sistema) {
        this.sistema = sistema;
        this.usuariosController = new GestionUsuariosController(sistema);
    }

    /**
     * Registra un nuevo usuario con validaciones completas (RF-001).
     *
     * @param nombre           nombre completo
     * @param correo           correo electrónico
     * @param password         contraseña
     * @param confirmar        confirmación de contraseña
     * @param telefono         teléfono
     * @return el usuario creado
     * @throws IllegalArgumentException si alguna validación falla
     */
    public Usuario registrar(String nombre, String correo,
                             String password, String confirmar, String telefono) {
        if (nombre  == null || nombre.isBlank())  throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (correo  == null || !correo.contains("@")) throw new IllegalArgumentException("El correo no es válido.");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        if (!password.equals(confirmar))           throw new IllegalArgumentException("Las contraseñas no coinciden.");
        if (!usuariosController.correoDisponible(correo)) throw new IllegalArgumentException("Ya existe una cuenta con ese correo.");
        return usuariosController.crearUsuario(nombre, correo, password, telefono != null ? telefono : "");
    }
}
