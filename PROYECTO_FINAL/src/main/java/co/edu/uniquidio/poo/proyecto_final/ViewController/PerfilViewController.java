package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.Controller.PerfilController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventos;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PerfilViewController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private Label lblMensaje;

    private PerfilController perfilController; // Referencia al Controller
    private Usuario usuarioActual;

    @FXML
    private void initialize() {
        // 1. Inicializamos el controller con la instancia del modelo
        perfilController = new PerfilController(SistemaGestionEventos.getInstance());

        // 2. Obtenemos los datos a través del controller
        usuarioActual = perfilController.obtenerUsuarioPerfil();

        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombreCompleto());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtTelefono.setText(usuarioActual.getTelefono());
        }
    }

    @FXML
    private void guardarCambios() {
        if (usuarioActual != null) {
            // 3. Delegamos la lógica de actualización al controller
            perfilController.actualizarDatosUsuario(
                    usuarioActual,
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText()
            );

            lblMensaje.setText("✅ Perfil actualizado");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Perfil", "Datos guardados correctamente");
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
