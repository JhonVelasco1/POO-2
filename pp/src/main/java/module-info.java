module co.edu.uniquidio.poo.pp {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquidio.poo.pp to javafx.fxml;
    exports co.edu.uniquidio.poo.pp;
}