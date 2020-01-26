package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.base.DataBase;

public class Klienci extends MainView {

    public Klienci(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus() {
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, "", "main_view.fxml", false));
    }

    @FXML
    private void initialize() {
        title.setText("Klienci");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label numerTel = new Label("Numer telefonu");
        Label pesel = new Label("Pesel");
        imie.setStyle("-fx-padding: 0 150 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 150 0 0;");
        nazwisko.getStyleClass().add("tag");
        numerTel.setStyle("-fx-padding: 0 150 0 0;");
        numerTel.getStyleClass().add("tag");
        pesel.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, numerTel, pesel);

        Button button = new Button("halo");
        button.setOnAction(e->moreInfo("98092200114"));
        button.getStyleClass().add("tag");
        button.getStyleClass().add("field");
        fillableRows.getChildren().add(button);
    }
    @Override
    public void search() {
        System.out.println(searchField.getText());
    }

    public void moreInfo(String pesel) {
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, pesel, "main_view.fxml", true));
    }

}
