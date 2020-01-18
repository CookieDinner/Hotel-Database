package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Zamowienia implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Zamowienia(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Zamówienia");
        Label danie = new Label("Danie");
        Label data = new Label("Data");

        danie.setStyle("-fx-padding: 0 300 0 0;");
        danie.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 120 0 0;");
        data.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(danie, data);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
