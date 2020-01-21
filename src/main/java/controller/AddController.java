package main.java.controller;

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

    public AddController(DataBase dataBase){
        this.dataBase = dataBase;
    }
    @FXML
    public void addKonferencje(){
        dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()), Integer.parseInt(eliczba_osob.getText()));
    }
}
