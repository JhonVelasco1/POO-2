package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.LoginController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * ViewController para la pantalla de login (RF-001).
 *
 * <p>Responsabilidad: capturar datos de la UI, delegar al
 * {@link LoginController} y navegar según resultado.</p>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class LoginViewController {

    @FXML private TextField     txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label         lblMensaje;

    private LoginController loginController;

    @FXML
    public void initialize() {
        loginController = new LoginController(SistemaGestionEventosSingleton.getInstance());
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass  = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: #c62828;");
            lblMensaje.setText("❌ Ingresa tu correo y contraseña.");
            return;
        }

        Usuario usuario = loginController.autenticar(email, pass);
        if (usuario != null) {
            lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
            lblMensaje.setText("✅ Bienvenido, " + usuario.getNombreCompleto());
            if (usuario.esAdmin()) {
                App.mostrarDashboardAdmin();
            } else {
                App.mostrarDashboardUsuario();
            }
        } else {
            lblMensaje.setStyle("-fx-text-fill: #c62828;");
            lblMensaje.setText("❌ Correo o contraseña incorrectos.");
        }
    }

    @FXML
    private void irARegistro() { App.mostrarRegistro(); }
}
