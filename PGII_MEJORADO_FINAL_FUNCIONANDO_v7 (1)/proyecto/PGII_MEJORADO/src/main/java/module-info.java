module co.edu.uniquidio.poo.proyecto_final {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquidio.poo.proyecto_final              to javafx.fxml;
    opens co.edu.uniquidio.poo.proyecto_final.ViewController to javafx.fxml;
    opens co.edu.uniquidio.poo.proyecto_final.Controller    to javafx.fxml;
    opens co.edu.uniquidio.poo.proyecto_final.model         to javafx.fxml;

    exports co.edu.uniquidio.poo.proyecto_final;
    exports co.edu.uniquidio.poo.proyecto_final.ViewController;
    exports co.edu.uniquidio.poo.proyecto_final.Controller;
    exports co.edu.uniquidio.poo.proyecto_final.model;
}
