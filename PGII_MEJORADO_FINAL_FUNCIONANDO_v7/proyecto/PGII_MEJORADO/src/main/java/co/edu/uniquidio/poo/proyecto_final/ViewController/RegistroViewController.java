package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.RegistroController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * ViewController para registro de nuevos usuarios (RF-001, RF-020).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class RegistroViewController {

    @FXML private TextField     txtNombre;
    @FXML private TextField     txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private TextField     txtTelefono;
    @FXML private Label         lblMensaje;
    @FXML private ProgressBar   progressBar;

    private RegistroController registroController;

    @FXML
    public void initialize() {
        registroController = new RegistroController(SistemaGestionEventosSingleton.getInstance());
        progressBar.setVisible(false);
        txtPassword.textProperty().addListener((obs, o, n) -> actualizarFortaleza(n));
    }

    private void actualizarFortaleza(String pass) {
        if (pass.isEmpty()) { progressBar.setVisible(false); return; }
        progressBar.setVisible(true);
        double f = 0;
        if (pass.length() >= 4)          f += 0.25;
        if (pass.length() >= 8)          f += 0.25;
        if (pass.matches(".*[A-Z].*"))   f += 0.25;
        if (pass.matches(".*[0-9!@#$%].*")) f += 0.25;
        progressBar.setProgress(f);
        progressBar.setStyle(f < 0.4 ? "-fx-accent:#e53935;" :
                             f < 0.7 ? "-fx-accent:#fb8c00;" : "-fx-accent:#43a047;");
    }

    @FXML
    private void handleRegistro() {
        lblMensaje.setStyle("-fx-text-fill: #c62828;");
        try {
            Usuario nuevo = registroController.registrar(
                txtNombre.getText(), txtCorreo.getText(),
                txtPassword.getText(), txtConfirmarPassword.getText(),
                txtTelefono.getText()
            );
            lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
            lblMensaje.setText("✅ Cuenta creada para " + nuevo.getNombreCompleto() + ". ¡Inicia sesión!");
            txtNombre.clear(); txtCorreo.clear(); txtPassword.clear();
            txtConfirmarPassword.clear(); txtTelefono.clear(); progressBar.setVisible(false);
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(App::mostrarLogin);
            }).start();
        } catch (IllegalArgumentException ex) {
            lblMensaje.setText("❌ " + ex.getMessage());
        }
    }

    @FXML
    private void irALogin() { App.mostrarLogin(); }
}
