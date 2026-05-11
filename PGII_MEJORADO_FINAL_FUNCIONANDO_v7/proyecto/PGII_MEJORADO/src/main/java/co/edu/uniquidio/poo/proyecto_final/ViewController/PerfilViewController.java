package co.edu.uniquidio.poo.proyecto_final.ViewController;

import co.edu.uniquidio.poo.proyecto_final.App;
import co.edu.uniquidio.poo.proyecto_final.Controller.PerfilController;
import co.edu.uniquidio.poo.proyecto_final.model.SistemaGestionEventosSingleton;
import co.edu.uniquidio.poo.proyecto_final.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * ViewController para la pantalla de perfil (RF-002, RF-021).
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class PerfilViewController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private Label     lblMensaje;
    @FXML private ListView<String> listMetodosPago;

    private PerfilController perfilController;
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        perfilController = new PerfilController(SistemaGestionEventosSingleton.getInstance());
        usuarioActual    = perfilController.obtenerUsuarioPerfil();
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombreCompleto());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtTelefono.setText(usuarioActual.getTelefono());
            // RF-021: Mostrar métodos de pago
            if (listMetodosPago != null)
                listMetodosPago.setItems(
                    javafx.collections.FXCollections.observableArrayList(usuarioActual.getMetodosPago()));
        }
    }

    /** RF-002: Guardar cambios del perfil. */
    @FXML
    private void guardarCambios() {
        try {
            perfilController.actualizarDatosUsuario(
                usuarioActual, txtNombre.getText(), txtCorreo.getText(), txtTelefono.getText());
            lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
            lblMensaje.setText("✅ Perfil actualizado correctamente.");
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #c62828;");
            lblMensaje.setText("❌ " + e.getMessage());
        }
    }

    @FXML
    private void volver() { App.mostrarDashboardUsuario(); }
}
