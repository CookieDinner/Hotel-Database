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
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.base.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddController {
    @FXML
    private TextField enazwa;
    @FXML
    private DatePicker edata;
    @FXML
    private TextField eliczba_osob;
    @FXML
    private ComboBox<String> ehala;
    @FXML
    private ComboBox<String> epracownicy;
    @FXML
    private ImageView editImageView;
    @FXML
    private Button editButton, saveButton;
    @FXML
    private VBox pracownicyScroll;

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
            edata.setDisable(true);
            eliczba_osob.setEditable(false);
            ehala.setDisable(true);
            epracownicy.setDisable(true);
            saveButton.setVisible(false);
        }else {
            dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()), Integer.parseInt(eliczba_osob.getText()), epracownicy.getValue(), Integer.parseInt(ehala.getValue()));
        }
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void initialize(){
        if(look){
            enazwa.setText("nazwa");    // TODO
            enazwa.setEditable(false);
            edata.setValue(Main.dateCreate("2020-01-13"));  // TODO
            edata.setDisable(true);
            eliczba_osob.setText("-1/12");  // TODO
            eliczba_osob.setEditable(false);
            ehala.setValue("2");    // TODO
            ehala.setDisable(true);
//            for (String i : pracownicy)   // TODO
//                pracownicyScroll.getChildren().add(createPracownikButton(i));
            epracownicy.setDisable(true);
            saveButton.setVisible(false);
        }else {
            editImageView.setImage(null);   // TODO
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
        edata.setDisable(false);
        eliczba_osob.setEditable(true);
        ehala.setDisable(false);
        epracownicy.setDisable(false);
        saveButton.setVisible(true);
    }

    @FXML
    private void choosePracownik(){
        pracownicyScroll.getChildren().add(createPracownikButton(epracownicy.getValue().toString()));
    }

    private Button createPracownikButton(String nazwa){
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e->deleteButton(button));
        return button;
    }

    private void deleteButton(Button toDelete){
        pracownicyScroll.getChildren().remove(toDelete);
    }
}
