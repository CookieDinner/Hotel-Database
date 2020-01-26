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
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddKonferencje {
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
    private ArrayList<String> pracownicy = new ArrayList<>();

    public AddKonferencje(DataBase dataBase, Konferencje konferencje, Controller controller){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.look = false;
    }
    public AddKonferencje(DataBase dataBase, Konferencje konferencje, Controller controller, String id_konf){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.id_konf = id_konf;
        this.look = true;
    }
    @FXML
    public void addKonferencje(){
        if(look){
            String pesel = null;
            enazwa.setEditable(false);
            edata.setDisable(true);
            eliczba_osob.setEditable(false);
            ehala.setDisable(true);
            epracownicy.setDisable(true);
            saveButton.setVisible(false);
        }else if(checkCorrectness()){
            String pesel = null;
            try {
                String str = "SELECT * FROM hotel_pracownicy WHERE nazwisko=\'" + epracownicy.getSelectionModel().getSelectedItem() + "\'";
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                pesel = rs.getString("pesel");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            String hala = ehala.getSelectionModel().getSelectedItem().toString();
            dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()),
                    Integer.parseInt(eliczba_osob.getText()), pesel,
                    Integer.parseInt(hala.substring(0, hala.indexOf(","))),pracownicy);
            returnTo();
        }
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void initialize(){
        if(look){
            try {
                String str = "SELECT * FROM hotel_konferencje WHERE id_konferencji=" + id_konf;
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                enazwa.setText(rs.getString("nazwa"));    // TODO
                enazwa.setEditable(false);
                edata.setValue(rs.getDate("data_konferencji").toLocalDate());//Main.dateCreate("2020-01-13"));  // TODO
                edata.setDisable(true);
                eliczba_osob.setText(Integer.toString(rs.getInt("liczba_osob")));  // TODO
                eliczba_osob.setEditable(false);
                ehala.setValue(Integer.toString(rs.getInt("hala_konferencyjna")));    // TODO
                ehala.setDisable(true);
                getPracownicy();
                //pracownicyScroll.getChildren().add(createPracownikButton(epracownicy.getValue().toString()));
                for (String i : pracownicy)   // TODO
                    pracownicyScroll.getChildren().add(createPracownikButton(i));
                epracownicy.setDisable(true);
                saveButton.setVisible(false);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            editImageView.setImage(null);   // TODO
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(konferencje.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
        ObservableList<String> observHale = FXCollections.observableArrayList();
        observHale.addAll(konferencje.dataBase.getSomeHale());
        ehala.setItems(observHale);
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
        String temp_nazwisko = epracownicy.getValue().toString();
        try {
            String str = "SELECT * FROM hotel_pracownicy WHERE upper(nazwisko)=upper(\'" + temp_nazwisko + "\')";
            PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            pracownicy.add(rs.getString("pesel"));
            stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        pracownicyScroll.getChildren().add(createPracownikButton(temp_nazwisko));
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (enazwa.getText().isEmpty() || enazwa.getText().length() > 100){
            correct = false;
            enazwa.getStyleClass().add("wrong");
        }else{
            while (enazwa.getStyleClass().remove("wrong"));
        }
        if (edata.getValue() == null || !edata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            edata.getStyleClass().add("wrong");
        }else{
            while (edata.getStyleClass().remove("wrong")) ;
        }
        if (eliczba_osob.getText().isEmpty() || Integer.parseInt(eliczba_osob.getText()) < 0){
            correct = false;
            eliczba_osob.getStyleClass().add("wrong");
        }else{
            while (eliczba_osob.getStyleClass().remove("wrong"));
        }
        if (ehala.getValue() == null){
            correct = false;
            ehala.getStyleClass().add("wrong");
        }else{
            while (ehala.getStyleClass().remove("wrong"));
        }
        if (pracownicyScroll.getChildren().isEmpty()){
            correct = false;
            epracownicy.getStyleClass().add("wrong");
        }else{
            while (epracownicy.getStyleClass().remove("wrong"));
        }
        return correct;
    }

    private Button createPracownikButton(String nazwa) {
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e -> deleteButton(button));
        return button;
    }

    private void deleteButton(Button toDelete) {
        pracownicyScroll.getChildren().remove(toDelete);
        pracownicy.clear();
        getPracownicy();
        try {
            for (Node i : pracownicyScroll.getChildren()) {
                String temp_nazwisko = ((Button) i).getText();
                String str = "SELECT * FROM hotel_pracownicy WHERE upper(nazwisko)=upper(\'" + temp_nazwisko + "\')";
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                pracownicy.add(rs.getString("pesel"));
                stmt.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getPracownicy(){
        try {
            String str = "select n.*, p.nazwisko from hotel_pracownicy p inner join hotel_nadzor_konferencji n on (p.pesel=n.pracownik) where n.id_konferencji=" + id_konf;
            PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                pracownicy.add(rs.getString("nazwisko"));
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}
