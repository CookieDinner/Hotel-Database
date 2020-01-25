package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.base.DataBase;

public class Pokoje extends MainView {
    public Pokoje(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus(){
        controller.changeScene("addPokoj.fxml", new AddPokoj(controller, this));
    }
    @FXML
    private void initialize(){
        title.setText("Pokoje");
        Label numer = new Label("Numer");
        Label cena = new Label("Cena");
        Label liczbaLozek = new Label("Liczba łóżek");
        Label telewizor = new Label("Telewizor");
        Label lazienka = new Label("Łazienka");
        numer.setStyle("-fx-padding: 0 50 0 0;");
        numer.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 150 0 0;");
        cena.getStyleClass().add("tag");
        liczbaLozek.setStyle("-fx-padding: 0 200 0 0;");
        liczbaLozek.getStyleClass().add("tag");
        telewizor.setStyle("-fx-padding: 0 50 0 0;");
        telewizor.getStyleClass().add("tag");
        lazienka.setStyle("-fx-padding: 0 0 0 0;");
        lazienka.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(numer, cena, liczbaLozek, telewizor, lazienka);
    }
}
