module ub3.uebung3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ub3.uebung3 to javafx.fxml;
    exports ub3.uebung3;
}