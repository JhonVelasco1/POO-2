package co.edu.uniquidio.poo.proyecto_patrones.ViewController;


import co.edu.uniquidio.poo.proyecto_patrones.App;
import co.edu.uniquidio.poo.proyecto_patrones.Controller.LoginController; // Importar el nuevo controller
import co.edu.uniquidio.poo.proyecto_patrones.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_patrones.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private LoginController loginController; // Referencia al Controller

    @FXML
    public void initialize() {
        // Inicializamos el controller pasando la instancia del modelo (Singleton)
        loginController = new LoginController(SistemaGestionEventos.getInstance());
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();

        // Ahora la comunicación es: ViewController -> Controller -> Model
        Usuario usuario = loginController.autenticar(email, pass);

        if (usuario != null) {
            try {
                if (usuario.esAdmin()) {
                    App.mostrarDashboardAdmin();
                } else {
                    App.mostrarDashboardUsuario();
                }
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el dashboard");
            }
        } else {
            lblMensaje.setText("❌ Credenciales incorrectas");
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso denegado", "Email o contraseña inválidos");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}