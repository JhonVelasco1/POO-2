module co.edu.uniquidio.poo.proyecto_final {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquidio.poo.proyecto_final to javafx.fxml;
    exports co.edu.uniquidio.poo.proyecto_final;
}