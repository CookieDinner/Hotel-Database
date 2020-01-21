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
import main.java.base.DataBase;

import java.sql.*;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    @FXML
    public Pane mainView;
    @FXML
    public Button mainPage, konferencje, rezerwacje, zamowienia, dostawy, magazyn, pracownicy, dania;
    private DataBase dataBase;

    public Controller(){
        dataBase = new DataBase();
        dataBase.connect();
    }

    @FXML
    public void changeToMain(){
        mainView.getChildren().clear();
        try{
            mainView.getChildren().add(FXMLLoader.load(Main.class.getResource("../resources/fxml/main_page.fxml")));
        }catch (IOException ignored){}
        currentButton(mainPage);
    }
    @FXML
    public void changeToKonferencje(){
        changeScene(new Konferencje(this, dataBase));
        currentButton(konferencje);
    }
    @FXML
    public void changeToRezerwacje(){
        changeScene(new Rezerwacje(this, dataBase));
        currentButton(rezerwacje);
    }
    @FXML
    public void changeToZamowiania(){
        changeScene(new Zamowienia(this, dataBase));
        currentButton(zamowienia);
    }
    @FXML
    public void changeToDostawy(){
        changeScene(new Dostawy(this, dataBase));
        currentButton(dostawy);
    }
    @FXML
    public void changeToMagazyn(){
        changeScene(new Magazyn(this, dataBase));
        currentButton(magazyn);
    }
    @FXML
    public void changeToPracownicy(){
        changeScene(new Pracownicy(this, dataBase));
        currentButton(pracownicy);
    }
    @FXML
    public void changeToDania(){
        changeScene(new Dania(this, dataBase));
        currentButton(dania);
    }
    @FXML
    public void addKonferencje(){
        System.out.println("test");
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

    private void changeScene(Object controller){
        changeScene("main_view.fxml", controller);
    }

    public void changeScene(String fxml, Object controller){
        mainView.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../resources/fxml/"+fxml));
            loader.setController(controller);
            Parent root = loader.load();
            mainView.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        changeToMain();
    }

}
