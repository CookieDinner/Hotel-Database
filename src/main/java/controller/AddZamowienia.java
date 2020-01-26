package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
            epracownicy.setValue("");       // TODO
            epracownicy.setDisable(true);
            zdata.setValue(Main.dateCreate("2010-12-15"));  // TODO
            zdata.setDisable(true);
            edania.setEditable(false);
//            for(String i : allDania)  // TODO
//                daniaScroll.getChildren().add(createDanieButton(i));
            saveButton.setVisible(false);
        } else {
            editButton.setVisible(false);
            ObservableList<String> observPracownicy = FXCollections.observableArrayList();
            observPracownicy.addAll(zamowienia.dataBase.getSomePracownicy());
            epracownicy.setItems(observPracownicy);
            ObservableList<String> observDania = FXCollections.observableArrayList();
            observDania.addAll(zamowienia.dataBase.getSomeDania());
            edania.setItems(observDania);
        }
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
            zamowienia.dataBase.addZamowienie(Date.valueOf(zdata.getValue()), pesel, idDania);
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
//    private Button createDanieButton(String nazwa) {
//        Button button = new Button(nazwa);
//        button.getStyleClass().add("danieButton");
//        button.setOnAction(e -> deleteButton(button));
//        return button;
//    }
//    private void deleteButton(Button toDelete){
//        daniaScroll.getChildren().remove(toDelete);
//    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", zamowienia);
    }

}
