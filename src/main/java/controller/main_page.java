package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class main_page {
    @FXML
    private BorderPane borderPane;

    @FXML
    public void changeToKonferencje(){
        System.out.println("Change to konferencje");
    }
    @FXML
    public void changeToRezerwacje(){
        System.out.println("Change to rezerwacje");
    }
    @FXML
    public void changeToZamowiania(){
        System.out.println("Change to zamowienia");
    }
    @FXML
    public void changeToDostawy(){
        System.out.println("Change to dostawy");
    }
    @FXML
    public void changeToMagazyn(){
        System.out.println("Change to magazyn");
    }
    @FXML
    public void changeToPracownicy(){
        System.out.println("Change to pracownicy");
    }
    @FXML
    public void changeToDania(){
        System.out.println("Change to dania");
    }
}
