package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.java.Main;

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
            imie.setText("");   // TODO
            imie.setEditable(false);
            nazwisko.setText("");   // TODO
            nazwisko.setEditable(false);
            pesel.setText("");  // TODO
            pesel.setEditable(false);
            adres.setText("");  // TODO
            adres.setEditable(false);
            etat.setText("");   // TODO
            etat.setEditable(false);
            placa.setText("");  // TODO
            placa.setEditable(false);
            umowa.setText("");  // TODO
            umowa.setEditable(false);
            premiaTextField.setText("");    // TODO
            premiaTextField.setEditable(false);
            dataUr.setValue(Main.dateCreate("2020-10-13")); // TODO
            dataUr.setDisable(true);
            dataZa.setValue(Main.dateCreate("2010-01-01")); // TODO
            dataZa.setDisable(true);
            premiaBox.setSelected(true);    // TODO
            premiaBox.setOnAction(e->{if(!edit) premiaBox.setSelected(!premiaBox.isSelected());});
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
        }else{
            // TODO
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
        boolean correct = false;
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
