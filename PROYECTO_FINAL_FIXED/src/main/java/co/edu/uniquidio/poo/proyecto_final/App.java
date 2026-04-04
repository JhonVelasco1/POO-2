package co.edu.uniquidio.poo.proyecto_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(550);
        mostrarLogin();
    }

    public static void mostrarLogin() {
        cambiarEscena("login.fxml", "Plataforma de Eventos - Iniciar Sesión", 480, 420);
    }

    public static void mostrarRegistro() {
        cambiarEscena("registro.fxml", "Plataforma de Eventos - Registro", 500, 500);
    }

    public static void mostrarDashboardAdmin() {
        cambiarEscena("adminDashboard.fxml", "Panel de Administrador", 950, 650);
    }

    public static void mostrarDashboardUsuario() {
        cambiarEscena("userDashboard.fxml", "Panel de Usuario", 950, 650);
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}
