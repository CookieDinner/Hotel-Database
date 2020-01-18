package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Rezerwacje implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Rezerwacje(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Rezerwacje");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label pokoj = new Label("Pokój");
        Label dataZameldowania = new Label("Data zameldowania");
        Label dataWymeldowania = new Label("Data wymeldowania");
        imie.setStyle("-fx-padding: 0 100 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 120 0 0;");
        nazwisko.getStyleClass().add("tag");
        pokoj.setStyle("-fx-padding: 0 50 0 0;");
        pokoj.getStyleClass().add("tag");
        dataZameldowania.setStyle("-fx-padding: 0 30 0 0;");
        dataZameldowania.getStyleClass().add("tag");
        dataWymeldowania.setStyle("-fx-padding: 0 0 0 0;");
        dataWymeldowania.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, pokoj, dataZameldowania, dataWymeldowania);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
