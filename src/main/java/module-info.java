module com.isep.jeu6quiprend {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.json;


    opens com.isep.jeu6quiprend to javafx.fxml;
    exports com.isep.jeu6quiprend;
}