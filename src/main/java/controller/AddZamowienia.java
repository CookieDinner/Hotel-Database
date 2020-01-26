package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import main.java.Main;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddZamowienia {
    private Controller controller;
    private Zamowienia zamowienia;
    private boolean look;
    private String id;
    @FXML
    private ComboBox<String> epracownicy;
    @FXML
    private ComboBox<String> edania;
    @FXML
    private DatePicker zdata;
    @FXML
    private VBox daniaScroll;
    @FXML
    private Button saveButton, editButton;

    private ArrayList<String> dania = new ArrayList<>();


    public AddZamowienia(Controller controller, Zamowienia zamowienia){
        this.controller = controller;
        this.zamowienia = zamowienia;
        this.look = false;
    }

    public AddZamowienia(Controller controller, Zamowienia zamowienia, String id){
        this.controller = controller;
        this.zamowienia = zamowienia;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize() {
        if (look) {
            try {
                String str = "SELECT z.*, p.nazwisko FROM hotel_zamowienia z inner join hotel_pracownicy p on(p.pesel=z.pracownik) WHERE id_zamowienia=" + id;
                PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                epracownicy.setValue(rs.getString("nazwisko"));
                epracownicy.setDisable(true);
                zdata.setValue(rs.getDate("data_zamowienia").toLocalDate());
                zdata.setDisable(true);
                edania.setEditable(false);
                getDania();
                for(String i : dania)
                    daniaScroll.getChildren().add(createDanieButton(i));
                    saveButton.setVisible(false);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            editButton.setVisible(false);
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(zamowienia.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
        ObservableList<String> observDania = FXCollections.observableArrayList();
        observDania.addAll(zamowienia.dataBase.getSomeDania());
        edania.setItems(observDania);
    }
    @FXML
    private void addZamowienie(){
        if(look){
            epracownicy.setDisable(true);
            zdata.setDisable(true);
            edania.setEditable(false);
            saveButton.setVisible(false);
        }else if(checkCorrectness()){
            int idDania = 0;
            String pesel = null;
            try {
                String str = "SELECT * FROM hotel_dania WHERE nazwa=\'" + edania.getSelectionModel().getSelectedItem() + "\'";
                PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                idDania = rs.getInt("id_dania");
                stmt.close();
                str = "SELECT * FROM hotel_pracownicy WHERE nazwisko=\'" + epracownicy.getSelectionModel().getSelectedItem() + "\'";
                stmt = zamowienia.dataBase.getCon().prepareStatement(str);
                rs = stmt.executeQuery();
                rs.next();
                pesel = rs.getString("pesel");
                stmt.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            zamowienia.dataBase.addZamowienie(Date.valueOf(zdata.getValue()), pesel, dania);
            returnTo();
        }else{
            //TODO
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        epracownicy.setDisable(false);
        zdata.setDisable(false);
        edania.setEditable(true);
        saveButton.setVisible(true);
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (epracownicy.getValue() == null){
            correct = false;
            epracownicy.getStyleClass().add("wrong");
        }else{
            while (epracownicy.getStyleClass().remove("wrong"));
        }
        if (zdata.getValue() == null || zdata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            zdata.getStyleClass().add("wrong");
        }else{
            while (zdata.getStyleClass().remove("wrong"));
        }
        if (daniaScroll.getChildren().isEmpty()){
            correct = false;
            edania.getStyleClass().add("wrong");
        }else{
            while (edania.getStyleClass().remove("wrong"));
        }
        return correct;
    }

//    private boolean isDanieCorrect(String danie){
//        return true;
//    }
//
//    @FXML
//    private void danieReady(KeyEvent ke) {
//        if (ke.getCode().equals(KeyCode.ENTER)) {
//            if (isDanieCorrect(dania.getText())) {
//                daniaScroll.getChildren().add(createDanieButton(dania.getText()));
//                dania.setText("");
//            }
//        }
//    }
    @FXML
    private void chooseDanie(){
        String temp_danie = edania.getValue().toString();
        try {
            String str = "SELECT * FROM hotel_dania WHERE upper(nazwa)=upper(\'" + temp_danie + "\')";
            PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            dania.add(rs.getString("id_dania"));
            stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        daniaScroll.getChildren().add(createDanieButton(temp_danie));
    }

    private Button createDanieButton(String nazwa) {
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e -> deleteButton(button));
        return button;
    }
    private void deleteButton(Button toDelete){
        daniaScroll.getChildren().remove(toDelete);
        dania.clear();
        try {
            for (Node i : daniaScroll.getChildren()) {
                String temp_danie = ((Button) i).getText();
                String str = "SELECT * FROM hotel_dania WHERE upper(nazwa)=upper(\'" + temp_danie + "\')";
                PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                dania.add(rs.getString("id_dania"));
                stmt.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", zamowienia);
    }

    private void getDania(){
        try {
            String str = "select d.nazwa, z.*, p.nazwisko from hotel_pracownicy p inner join hotel_zamowienia z on (p.pesel=z.pracownik) inner join hotel_zamowienie_dania zd on(zd.zamowienie=z.id_zamowienia) inner join hotel_dania d on(zd.id_dania=d.id_dania) where z.id_zamowienia=" + id;
            PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dania.add(rs.getString("nazwa"));
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}
