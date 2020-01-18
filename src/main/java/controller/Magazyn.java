package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Magazyn implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    private Controller controller;

    public Magazyn(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Magazyn");
        Label skladnik = new Label("Składnik");
        Label ilosc = new Label("Ilość");
        Label dostawca = new Label("Dostawca");
        Label cena = new Label("Cena");

        skladnik.setStyle("-fx-padding: 0 250 0 0;");
        skladnik.getStyleClass().add("tag");
        ilosc.setStyle("-fx-padding: 0 50 0 0;");
        ilosc.getStyleClass().add("tag");
        dostawca.setStyle("-fx-padding: 0 250 0 0;");
        dostawca.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 0 0 0;");
        cena.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(skladnik, ilosc, dostawca, cena);
    }

    @Override
    public void search() {

    }

    @Override
    public void moreInfo() {

    }
}
