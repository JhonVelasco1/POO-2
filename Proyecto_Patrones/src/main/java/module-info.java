module co.edu.uniquidio.poo.proyecto_patrones {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquidio.poo.proyecto_patrones to javafx.fxml;
    exports co.edu.uniquidio.poo.proyecto_patrones;
}