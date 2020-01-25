package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.base.DataBase;

public class Hale extends MainView {
    public Hale(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus(){
        controller.changeScene("addHala.fxml", new AddHala(controller, this));
    }
    @FXML
    private void initialize(){
        title.setText("Hale konferencyjne");
        Label numer = new Label("Numer");
        Label liczbaOsob = new Label("Liczba osób");
        numer.setStyle("-fx-padding: 0 300 0 0;");
        numer.getStyleClass().add("tag");
        liczbaOsob.setStyle("-fx-padding: 0 50 0 0;");
        liczbaOsob.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(numer, liczbaOsob);
    }
}
