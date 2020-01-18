package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Pracownicy implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Pracownicy(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Pracownicy");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label etat = new Label("Etat");
        Label placa = new Label("Płaca");
        Label data = new Label("Data zatrudnienia");

        imie.setStyle("-fx-padding: 0 150 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 150 0 0;");
        nazwisko.getStyleClass().add("tag");
        etat.setStyle("-fx-padding: 0 75 0 0;");
        etat.getStyleClass().add("tag");
        placa.setStyle("-fx-padding: 0 75 0 0;");
        placa.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 0 0 0;");
        data.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, etat, placa, data);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
