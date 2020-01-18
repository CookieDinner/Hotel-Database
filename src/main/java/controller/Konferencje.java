package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Konferencje implements MainView{
    @FXML
    public Label title;
    @FXML
    private HBox tagsHBox;
    @FXML
    public TextField searchField;
    private Controller controller;


    public Konferencje(Controller controller){
        this.controller = controller;
    }

    @Override
    public void plus() {
        controller.changeScene("addKonferencje.fxml", null);
    }

    @FXML
    public void initialize(){
        title.setText("Konferencje");
        Label nazwa = new Label("Nazwa");
        Label data = new Label("Data");
        Label liczbaOsob = new Label("Liczba osób");
        Label halaKonferencyjna = new Label("Hala konferencyjna");
        nazwa.setStyle("-fx-padding: 0 300 0 0;");
        nazwa.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 100 0 0;");
        data.getStyleClass().add("tag");
        liczbaOsob.setStyle("-fx-padding: 0 50 0 0;");
        liczbaOsob.getStyleClass().add("tag");
        halaKonferencyjna.setStyle("-fx-padding: 0 0 0 0;");
        halaKonferencyjna.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(nazwa, data, liczbaOsob, halaKonferencyjna);
    }

    @Override
    public void search() {
        System.out.println(searchField.getText());
    }

    @Override
    public void moreInfo() {

    }
}
