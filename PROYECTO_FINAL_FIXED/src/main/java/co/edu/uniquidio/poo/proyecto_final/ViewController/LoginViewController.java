package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.LoginController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginViewController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private LoginController loginController;

    @FXML
    public void initialize() {
        loginController = new LoginController(SistemaGestionEventos.getInstance());
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("❌ Ingresa tu correo y contraseña");
            return;
        }

        Usuario usuario = loginController.autenticar(email, pass);
        if (usuario != null) {
            if (usuario.esAdmin()) {
                App.mostrarDashboardAdmin();
            } else {
                App.mostrarDashboardUsuario();
            }
        } else {
            lblMensaje.setText("❌ Credenciales incorrectas");
        }
    }

    @FXML
    private void irARegistro() {
        App.mostrarRegistro();
    }
}
