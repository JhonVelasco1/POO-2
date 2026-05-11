package co.edu.uniquidio.poo.proyecto_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX.
 * Gestiona la navegación entre pantallas mediante cambio de escena.
 *
 * <p>Pantallas disponibles:</p>
 * <ul>
 *   <li>Login (RF-001)</li>
 *   <li>Registro (RF-001)</li>
 *   <li>Dashboard Usuario (RF-003 a RF-011)</li>
 *   <li>Dashboard Admin (RF-012 a RF-019)</li>
 *   <li>Selección de Asientos (RF-005)</li>
 *   <li>Perfil (RF-002)</li>
 * </ul>
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(580);
        mostrarLogin();
    }

    /** Navega a la pantalla de login (RF-001). */
    public static void mostrarLogin() {
        cambiarEscena("login.fxml", "Plataforma de Eventos - Iniciar Sesión", 500, 440);
    }

    /** Navega a la pantalla de registro (RF-001). */
    public static void mostrarRegistro() {
        cambiarEscena("registro.fxml", "Plataforma de Eventos - Registro", 520, 520);
    }

    /** Navega al panel de administrador (RF-012 a RF-019). */
    public static void mostrarDashboardAdmin() {
        cambiarEscena("adminDashboard.fxml", "⚙️ Panel de Administrador - PGII", 1000, 680);
    }

    /** Navega al panel del usuario (RF-003 a RF-011). */
    public static void mostrarDashboardUsuario() {
        cambiarEscena("userDashboard.fxml", "🎟️ Panel de Usuario - PGII", 1000, 680);
    }

    /** Navega a la pantalla de selección de asientos (RF-005). */
    public static void mostrarSeleccionAsientos() {
        cambiarEscena("seleccionAsientos.fxml", "🗺️ Selección de Asientos", 860, 580);
    }

    /** Navega a la pantalla de perfil (RF-002). */
    public static void mostrarPerfil() {
        cambiarEscena("perfil.fxml", "👤 Mi Perfil", 480, 380);
    }

    /**
     * Cambia la escena principal cargando el FXML indicado.
     *
     * @param fxml   nombre del archivo FXML
     * @param titulo título de la ventana
     * @param w      ancho de la escena
     * @param h      alto de la escena
     */
    private static void cambiarEscena(String fxml, String titulo, double w, double h) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, w, h);
            primaryStage.setTitle(titulo);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("❌ Error cargando: " + fxml);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}
