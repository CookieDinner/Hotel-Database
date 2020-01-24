package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.java.base.DataBase;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddController {
    @FXML
    TextField enazwa;
    @FXML
    DatePicker edata;
    @FXML
    TextField eliczba_osob;
    @FXML
    ComboBox ehala;
    @FXML
    ComboBox epracownicy;

    private DataBase dataBase;
    private Konferencje konferencje;
    private Controller controller;

    public AddController(DataBase dataBase, Konferencje konferencje, Controller controller){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
    }
    @FXML
    public void addKonferencje(){
        dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()), Integer.parseInt(eliczba_osob.getText()));
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void initialize(){
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(konferencje.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
    }
}
