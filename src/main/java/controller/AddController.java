package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView editImageView;
    @FXML
    private Button editButton, saveButton;

    private DataBase dataBase;
    private Konferencje konferencje;
    private Controller controller;
    private boolean look;
    private String id_konf;

    public AddController(DataBase dataBase, Konferencje konferencje, Controller controller){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.look = false;
    }

    public AddController(DataBase dataBase, Konferencje konferencje, Controller controller, String id_konf){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.id_konf = id_konf;
        this.look = true;
    }
    @FXML
    public void addKonferencje(){
        if(look){
            enazwa.setEditable(false);
            edata.setEditable(false);
            eliczba_osob.setEditable(false);
            ehala.setEditable(false);
            epracownicy.setEditable(false);
            saveButton.setVisible(false);
        }else {
            dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()), Integer.parseInt(eliczba_osob.getText()));
        }
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void initialize(){
        if(look){
            enazwa.setText("nazwa");
            enazwa.setEditable(false);
            edata.setValue(null);
            edata.setEditable(false);
            eliczba_osob.setText("-1/12");
            eliczba_osob.setEditable(false);
//            ehala
            ehala.setEditable(false);
//            epracownicy
            epracownicy.setEditable(false);
            saveButton.setVisible(false);
        }else {
            editImageView.setImage(null);
            ObservableList<String> observPracownicy = FXCollections.observableArrayList();
            observPracownicy.addAll(konferencje.dataBase.getSomePracownicy());
            epracownicy.setItems(observPracownicy);
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        enazwa.setEditable(true);
        edata.setEditable(true);
        eliczba_osob.setEditable(true);
        ehala.setEditable(true);
        epracownicy.setEditable(true);
        saveButton.setVisible(true);
    }
}
