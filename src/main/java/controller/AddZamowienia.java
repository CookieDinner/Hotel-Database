package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddZamowienia {

    @FXML
    ComboBox epracownicy;
    @FXML
    ComboBox edania;
    @FXML
    DatePicker edata;

    private Controller controller;
    private Zamowienia zamowienia;

    public AddZamowienia(Controller controller, Zamowienia zamowienia){
        this.controller = controller;
        this.zamowienia = zamowienia;
    }
    @FXML
    private void initialize(){
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(zamowienia.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
        ObservableList<String> observDania = FXCollections.observableArrayList();
        observDania.addAll(zamowienia.dataBase.getSomeDania());
        edania.setItems(observDania);
    }
    @FXML
    private void addZamowienie(){
        int idDania = 0;
        String pesel = null;
        try {
            String str = "SELECT * FROM hotel_dania WHERE nazwa=\'"+edania.getSelectionModel().getSelectedItem()+"\'";
            PreparedStatement stmt = zamowienia.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            idDania = rs.getInt("id_dania");
            stmt.close();
            str = "SELECT * FROM hotel_pracownicy WHERE nazwisko=\'"+epracownicy.getSelectionModel().getSelectedItem()+"\'";
            stmt = zamowienia.dataBase.getCon().prepareStatement(str);
            rs = stmt.executeQuery();
            rs.next();
            pesel = rs.getString("pesel");
            stmt.close();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        zamowienia.dataBase.addZamowienie(Date.valueOf(edata.getValue()), pesel, idDania);
        returnTo();
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", zamowienia);
    }

}
