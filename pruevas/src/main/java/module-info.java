module co.edu.uniquidio.poo.pruevas {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquidio.poo.pruevas to javafx.fxml;
    exports co.edu.uniquidio.poo.pruevas;
}