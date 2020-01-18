package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Dostawy implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Dostawy(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Dostawy");
        Label dostawca = new Label("Dostawca");
        Label skladnik = new Label("Składnik");
        Label data = new Label("Data");
        Label cena = new Label("Cena");

        dostawca.setStyle("-fx-padding: 0 250 0 0;");
        dostawca.getStyleClass().add("tag");
        skladnik.setStyle("-fx-padding: 0 250 0 0;");
        skladnik.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 70 0 0;");
        data.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 0 0 0;");
        cena.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(dostawca, skladnik, data, cena);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
