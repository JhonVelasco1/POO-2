package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.RegistroController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * ViewController para la pantalla de registro de nuevos usuarios (RF-001, RF-020).
 */
public class RegistroViewController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;
    @FXML private ProgressBar progressBar;

    private RegistroController registroController;

    @FXML
    public void initialize() {
        registroController = new RegistroController(SistemaGestionEventos.getInstance());
        progressBar.setVisible(false);

        // Feedback visual en tiempo real al escribir
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> actualizarFortaleza(newVal));
    }

    /** Muestra la fuerza de la contraseña visualmente */
    private void actualizarFortaleza(String pass) {
        if (pass.length() == 0) {
            progressBar.setVisible(false);
        } else {
            progressBar.setVisible(true);
            double fortaleza = calcularFortaleza(pass);
            progressBar.setProgress(fortaleza);
            if (fortaleza < 0.4) {
                progressBar.setStyle("-fx-accent: #e53935;");
            } else if (fortaleza < 0.7) {
                progressBar.setStyle("-fx-accent: #fb8c00;");
            } else {
                progressBar.setStyle("-fx-accent: #43a047;");
            }
        }
    }

    private double calcularFortaleza(String pass) {
        double puntos = 0;
        if (pass.length() >= 4) puntos += 0.25;
        if (pass.length() >= 8) puntos += 0.25;
        if (pass.matches(".*[A-Z].*")) puntos += 0.25;
        if (pass.matches(".*[0-9!@#$%].*")) puntos += 0.25;
        return puntos;
    }

    @FXML
    private void handleRegistro() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = txtPassword.getText();
        String confirmar = txtConfirmarPassword.getText();
        String telefono = txtTelefono.getText().trim();

        lblMensaje.setStyle("-fx-text-fill: #c62828;");

        try {
            Usuario nuevo = registroController.registrar(nombre, correo, pass, confirmar, telefono);
            lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
            lblMensaje.setText("✅ Cuenta creada para " + nuevo.getNombreCompleto() + ". ¡Ya puedes iniciar sesión!");

            // Limpiar campos
            txtNombre.clear(); txtCorreo.clear();
            txtPassword.clear(); txtConfirmarPassword.clear(); txtTelefono.clear();
            progressBar.setVisible(false);

            // Redirigir al login después de 1.5 seg
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(App::mostrarLogin);
            }).start();

        } catch (IllegalArgumentException ex) {
            lblMensaje.setText("❌ " + ex.getMessage());
        }
    }

    @FXML
    private void irALogin() {
        App.mostrarLogin();
    }
}
