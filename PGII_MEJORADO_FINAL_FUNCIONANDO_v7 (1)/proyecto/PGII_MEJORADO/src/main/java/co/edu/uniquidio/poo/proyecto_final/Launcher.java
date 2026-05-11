package co.edu.uniquidio.poo.proyecto_final;

import javafx.application.Application;

/**
 * Clase de arranque separada para evitar problemas con el classpath de JavaFX
 * en entornos sin el módulo JavaFX en el classpath principal.
 *
 * @author Equipo PGII - Uniquindío
 * @version 2.0
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
