package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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

    private Connection con = null;
    private PreparedStatement stmt = null;

    public AddController(Connection con){
        this.con = con;
    }
    @FXML
    public void addKonferencje(){
        try {
            stmt = con.prepareStatement("INSERT INTO hotel_konferencje (nazwa, data_konferencji, liczba_osob, hala_konferencyjna) VALUES (?,?,?,1)");
            stmt.setString(1, enazwa.getText());
            stmt.setDate(2, Date.valueOf(edata.getValue()));
            stmt.setInt(3, Integer.valueOf(eliczba_osob.getText()));
            stmt.executeUpdate();
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    @FXML
    public void initialize(){}
}
