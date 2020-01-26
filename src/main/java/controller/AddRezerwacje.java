package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.Main;
import main.java.base.DataBase;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddRezerwacje {
    private Controller controller;
    private Rezerwacje rezerwacje;
    private boolean rabat = false;
    private int which = 0;
    private String peselString, pracownikString, rabatString;
    private LocalDate dataZamString, dataWymString;
    @FXML
    private TextField rabatTextField;
    @FXML
    private TextField peselTextField;
    @FXML
    private ComboBox<String> epracownicy;
    @FXML
    private DatePicker zdata, wdata;
    @FXML
    private CheckBox rabatCheckBox;
    @FXML
    private Button saveButton, editButton;
    private boolean look, edit = false;
    public DataBase dataBase;
    private String id;
    private ArrayList<String> pokoje = new ArrayList<>();

    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.dataBase = rezerwacje.dataBase;
        this.look = false;
        this.edit = true;
    }
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje, String id){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.dataBase = rezerwacje.dataBase;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if (look) {
            try {
                String str = "SELECT * FROM hotel_rezerwacje WHERE id_rezerwacji=" + id;
                PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                peselTextField.setText(rs.getString("klient"));
                peselTextField.setEditable(false);
                epracownicy.setDisable(true);
                zdata.setValue(rs.getDate("data_zameldowania").toLocalDate());
                zdata.setDisable(true);
                wdata.setValue(rs.getDate("termin_wymeldowania").toLocalDate());
                wdata.setDisable(true);
                float temp_rabat = rs.getFloat("rabat");
                if(rabat)
                    rabatCheckBox.setSelected(true);
                else
                    rabatCheckBox.setSelected(false);
                rabatTextField.setText(Float.toString(temp_rabat));
                rabatTextField.setEditable(false);
                saveButton.setVisible(false);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            editButton.setVisible(false);
            rabatCheckBox.setSelected(rabat);
            rabatCheck();
            peselTextField.setText(peselString);
            rabatTextField.setText(rabatString);
            zdata.setValue(dataZamString);
            wdata.setValue(dataWymString);
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(rezerwacje.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    @FXML
    private void rabatCheck(){
        if(edit) {
            rabat = rabatCheckBox.isSelected();
            if (rabat) {
                rabatTextField.setEditable(true);
                while (rabatTextField.getStyleClass().remove("non-writable")) ;
                rabatTextField.getStyleClass().add("addTextWithButton");
            } else {
                rabatTextField.setEditable(false);
                rabatTextField.setText("");
                while (rabatTextField.getStyleClass().remove("addTextWithButton")) ;
                rabatTextField.getStyleClass().add("non-writable");
            }
        }else{
            rabatCheckBox.setSelected(!rabatCheckBox.isSelected());
        }
    }
    @FXML
    private void addRezerwacje(){
        if(look){
            peselTextField.setEditable(false);
            epracownicy.setDisable(true);
            zdata.setDisable(true);
            wdata.setDisable(true);
            rabatTextField.setEditable(false);
            edit = false;
            saveButton.setVisible(false);
        }else{
            try {
                String str = "SELECT pesel FROM hotel_pracownicy where upper(nazwisko)=upper(\'" + epracownicy.getValue().toString() + "\')";
                PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String temp_rabat;
                if(rabatCheckBox.isSelected())
                    rezerwacje.dataBase.addRezerwacje(Date.valueOf(zdata.getValue()), Date.valueOf(wdata.getValue()),
                            Float.parseFloat(rabatTextField.getText()), rs.getString("pesel"),
                            peselTextField.getText().toString(), pokoje);
                else
                    rezerwacje.dataBase.addRezerwacje(Date.valueOf(zdata.getValue()), Date.valueOf(wdata.getValue()),
                            0, rs.getString("pesel"),
                            peselTextField.getText().toString(), pokoje);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        returnTo();
    }
    @FXML
    private void addKlient(){
        peselString = peselTextField.getText();
        rabatString = rabatTextField.getText();
        dataZamString = zdata.getValue();
        dataWymString = wdata.getValue();
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, peselTextField.getText(), "addRezerwacje.fxml",false));
    }
    @FXML
    private void edit() {
        if (!look)
            return;
        saveButton.setVisible(true);
        peselTextField.setEditable(true);
        epracownicy.setDisable(false);
        zdata.setDisable(false);
        wdata.setDisable(false);
        rabatTextField.setEditable(true);
        edit = true;
    }
    @FXML
    private void peselTyped() {
        ArrayList<String> pesels = new ArrayList<>();
        try {
            String str = "SELECT pesel FROM hotel_klienci";
            PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                pesels.add(rs.getString("pesel"));
            }
            //System.out.println(peselTextField.getText());
            if (peselTextField.getText().length() == 11)
                if (pesels.indexOf(peselTextField.getText()) == -1) {
                    while (peselTextField.getStyleClass().remove("good")) ;
                    peselTextField.getStyleClass().add("wrong");
                } else {
                    while (peselTextField.getStyleClass().remove("wrong")) ;
                    peselTextField.getStyleClass().add("good");
                }
            else {
                while (peselTextField.getStyleClass().remove("wrong")) ;
                while (peselTextField.getStyleClass().remove("good")) ;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
