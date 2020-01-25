package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import main.java.Main;
import main.java.base.DataBase;

import java.io.IOException;

public class Controller {
    @FXML
    public Pane mainView;
    @FXML
    public Button konferencje, rezerwacje, zamowienia, dostawcy, magazyn, klienci, pracownicy, dania, hale, pokoje;
    private DataBase dataBase;

    public Controller(){
        dataBase = new DataBase();
        dataBase.connect();
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
    public void changeToDostawcy(){
        changeScene(new Dostawcy(this, dataBase));
        currentButton(dostawcy);
    }
    @FXML
    public void changeToMagazyn(){
        changeScene(new Magazyn(this, dataBase));
        currentButton(magazyn);
    }
    @FXML
    private void changeToKlienci(){
        changeScene(new Klienci(this, dataBase));
        currentButton(klienci);
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
    private void changeToHale(){
        changeScene(new Hale(this, dataBase));
        currentButton(hale);
    }
    @FXML
    private void changeToPokoje(){
        changeScene(new Pokoje(this, dataBase));
        currentButton(pokoje);
    }
    @FXML
    public void addKonferencje(){
        System.out.println("test");
    }

    private void currentButton(Button button){
        while(hale.getStyleClass().remove("clicked_button"));
        while(pokoje.getStyleClass().remove("clicked_button"));
        while(konferencje.getStyleClass().remove("clicked_button"));
        while(rezerwacje.getStyleClass().remove("clicked_button"));
        while(zamowienia.getStyleClass().remove("clicked_button"));
        while(dostawcy.getStyleClass().remove("clicked_button"));
        while(magazyn.getStyleClass().remove("clicked_button"));
        while(klienci.getStyleClass().remove("clicked_button"));
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
        changeToKonferencje();
    }

}
