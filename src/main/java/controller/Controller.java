package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Main;

import java.io.IOException;

public class Controller {
    @FXML
    public Pane mainView;
    @FXML
    public Button mainPage, konferencje, rezerwacje, zamowienia, dostawy, magazyn, pracownicy, dania;

    @FXML
    public void changeToMain(){
        changeScene("main_page.fxml");
        currentButton(mainPage);
    }
    @FXML
    public void changeToKonferencje(){
        changeScene("konferencje.fxml");
        currentButton(konferencje);
    }
    @FXML
    public void changeToRezerwacje(){
        changeScene("rezerwacje.fxml");
        currentButton(rezerwacje);
    }
    @FXML
    public void changeToZamowiania(){
        changeScene("zamowienia.fxml");
        currentButton(zamowienia);
    }
    @FXML
    public void changeToDostawy(){
        changeScene("dostawy.fxml");
        currentButton(dostawy);
    }
    @FXML
    public void changeToMagazyn(){
        changeScene("magazyn.fxml");
        currentButton(magazyn);
    }
    @FXML
    public void changeToPracownicy(){
        changeScene("pracownicy.fxml");
        currentButton(pracownicy);
    }
    @FXML
    public void changeToDania(){
        changeScene("dania.fxml");
        currentButton(dania);
    }

    private void currentButton(Button button){
        while(mainPage.getStyleClass().remove("clicked_button"));
        while(konferencje.getStyleClass().remove("clicked_button"));
        while(rezerwacje.getStyleClass().remove("clicked_button"));
        while(zamowienia.getStyleClass().remove("clicked_button"));
        while(dostawy.getStyleClass().remove("clicked_button"));
        while(magazyn.getStyleClass().remove("clicked_button"));
        while(pracownicy.getStyleClass().remove("clicked_button"));
        while(dania.getStyleClass().remove("clicked_button"));
        button.getStyleClass().add("clicked_button");
    }

    private void changeScene(String fxml){
        mainView.getChildren().clear();
        try {
            mainView.getChildren().add(FXMLLoader.load(Main.class.getResource("../resources/fxml/"+fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        changeScene("main_page.fxml");
        currentButton(mainPage);
    }

}
