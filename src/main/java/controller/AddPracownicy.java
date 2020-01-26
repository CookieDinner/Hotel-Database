package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.java.Main;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddPracownicy {
    private Controller controller;
    private Pracownicy pracownicy;
    private boolean premia = false, look, edit;
    private String peselString;
    @FXML
    private Button saveButton, editButton;
    @FXML
    private TextField premiaTextField, imie, nazwisko, pesel, adres, etat, placa, umowa;
    @FXML
    private DatePicker dataZa, dataUr;
    @FXML
    private CheckBox premiaBox;

    public AddPracownicy(Controller controller, Pracownicy pracownicy){
        this.controller = controller;
        this.pracownicy = pracownicy;
        this.look = false;
        this.edit = true;
    }
    public AddPracownicy(Controller controller, Pracownicy pracownicy, String peselString){
        this.controller = controller;
        this.pracownicy = pracownicy;
        this.peselString = peselString;
        this.look = true;
        this.edit = false;
    }
    @FXML
    private void initialize(){
        if(look){
            saveButton.setVisible(false);
            try {
                saveButton.setVisible(false);
                String str = "SELECT * FROM hotel_pracownicy WHERE pesel=" + peselString;
                PreparedStatement stmt = pracownicy.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                imie.setText(rs.getString("imie"));
                imie.setEditable(false);
                nazwisko.setText(rs.getString("nazwisko"));
                nazwisko.setEditable(false);
                pesel.setText(rs.getString("pesel"));
                pesel.setEditable(false);
                adres.setText(rs.getString("adres"));
                adres.setEditable(false);
                etat.setText(rs.getString("etat"));
                etat.setEditable(false);
                placa.setText(rs.getString("placa"));
                placa.setEditable(false);
                umowa.setText(rs.getString("umowa"));
                umowa.setEditable(false);
                premiaTextField.setText(rs.getString("premia"));
                premiaTextField.setEditable(false);
                dataUr.setValue(rs.getDate("data_urodzenia").toLocalDate());
                dataUr.setDisable(true);
                dataZa.setValue(rs.getDate("data_zatrudnienia").toLocalDate());
                dataZa.setDisable(true);
                if(rs.getFloat("premia") > 0)
                    premiaBox.setSelected(true);
                else
                    premiaBox.setSelected(false);
                premiaBox.setOnAction(e -> {
                    if (!edit) premiaBox.setSelected(!premiaBox.isSelected());
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            editButton.setVisible(false);
        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", pracownicy);
    }
    @FXML
    private void addPracownicy(){
        if(look){
            edit = false;
            saveButton.setVisible(false);
            imie.setEditable(false);
            nazwisko.setEditable(false);
            pesel.setEditable(false);
            adres.setEditable(false);
            etat.setEditable(false);
            placa.setEditable(false);
            umowa.setEditable(false);
            premiaTextField.setEditable(false);
            dataUr.setDisable(true);
            dataZa.setDisable(true);
        }else if(checkCorrectness()){
            pracownicy.dataBase.addPracownika(pesel.getText(), imie.getText(), nazwisko.getText(), etat.getText(),
                    Float.parseFloat(placa.getText()), Date.valueOf(dataUr.getValue()), Date.valueOf(dataZa.getValue()),
                    umowa.getText(), adres.getText(), ((premiaBox.isSelected()) ? Float.parseFloat(premiaTextField.getText()) : 0));
            returnTo();
        }else{
        }
    }
    @FXML
    private void premiaCheck(){
        if(edit) {
            premia = !premia;
            if (premia) {
                premiaTextField.setEditable(true);
                while (premiaTextField.getStyleClass().remove("non-writableSmall")) ;
                premiaTextField.getStyleClass().add("addTextWithButtonSmall");
            } else {
                premiaTextField.setEditable(false);
                premiaTextField.setText("");
                while (premiaTextField.getStyleClass().remove("addTextWithButtonSmall")) ;
                premiaTextField.getStyleClass().add("non-writableSmall");
            }
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        edit = true;
        saveButton.setVisible(true);
        imie.setEditable(true);
        nazwisko.setEditable(true);
        pesel.setEditable(true);
        adres.setEditable(true);
        etat.setEditable(true);
        placa.setEditable(true);
        umowa.setEditable(true);
        premiaTextField.setEditable(true);
        dataUr.setDisable(false);
        dataZa.setDisable(false);
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (imie.getText().isEmpty() || imie.getText().length() > 30){
            correct = false;
            imie.getStyleClass().add("wrong");
        }else{
            while (imie.getStyleClass().remove("wrong"));
        }
        if (nazwisko.getText().isEmpty() || nazwisko.getText().length() > 30){
            correct = false;
            nazwisko.getStyleClass().add("wrong");
        }else{
            while (nazwisko.getStyleClass().remove("wrong"));
        }
        if (!pesel.getText().matches("[0-9]{11}")){
            correct = false;
            pesel.getStyleClass().add("wrong");
        }else{
            while (pesel.getStyleClass().remove("wrong"));
        }
        if (adres.getText().isEmpty() || adres.getText().length() > 50){
            correct = false;
            adres.getStyleClass().add("wrong");
        }else{
            while (adres.getStyleClass().remove("wrong"));
        }
        if (etat.getText().isEmpty() || etat.getText().length() > 30){
            correct = false;
            etat.getStyleClass().add("wrong");
        }else{
            while (etat.getStyleClass().remove("wrong"));
        }
        if (!placa.getText().matches("^[0-9]{1,7}(\\.[0-9]{0,2}){0,1}$")){
            correct = false;
            placa.getStyleClass().add("wrong");
        }else{
            while (placa.getStyleClass().remove("wrong"));
        }
        if (umowa.getText().isEmpty() || umowa.getText().length() > 30){
            correct = false;
            umowa.getStyleClass().add("wrong");
        }else{
            while (umowa.getStyleClass().remove("wrong"));
        }
        if (dataUr.getValue() == null || dataUr.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            dataUr.getStyleClass().add("wrong");
        }else{
            while (dataUr.getStyleClass().remove("wrong"));
        }
        if (dataZa.getValue() == null || dataZa.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            dataZa.getStyleClass().add("wrong");
        }else{
            while (dataZa.getStyleClass().remove("wrong"));
        }
        return correct;
    }

}
