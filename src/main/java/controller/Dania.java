package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Dania implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Dania(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Dania");
        Label nazwa = new Label("Nazwa");
        Label cena = new Label("Cena");
        Label dostepnosc = new Label("Dostępność");

        nazwa.setStyle("-fx-padding: 0 450 0 0;");
        nazwa.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 150 0 0;");
        cena.getStyleClass().add("tag");
        dostepnosc.setStyle("-fx-padding: 0 0 0 0;");
        dostepnosc.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(nazwa, cena, dostepnosc);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
