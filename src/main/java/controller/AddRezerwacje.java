package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.Main;

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
    private String id;
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.look = false;
        this.edit = true;
    }
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje, String id){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if (look) {
            peselTextField.setText("");     // TODO
            peselTextField.setEditable(false);
            epracownicy.setValue("");       // TODO
            epracownicy.setDisable(true);
            zdata.setValue(Main.dateCreate("2020-01-12"));  // TODO
            zdata.setDisable(true);
            wdata.setValue(Main.dateCreate("2020-01-12"));  // TODO
            wdata.setDisable(true);
            rabatCheckBox.setSelected(true);    // TODO
            rabatTextField.setText("");     // TODO
            rabatTextField.setEditable(false);
            saveButton.setVisible(false);
        }else {
            editButton.setVisible(false);
            rabatCheckBox.setSelected(rabat);
            rabatCheck();
            peselTextField.setText(peselString);
            rabatTextField.setText(rabatString);
            zdata.setValue(dataZamString);
            wdata.setValue(dataWymString);
        }
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
            if(checkCorrectness()){
                // TODO
            }
        }
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

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!peselTextField.getText().matches("^[0-9]{11}$")){
            correct = false;
            peselTextField.getStyleClass().add("wrong");
        }else{
            while(peselTextField.getStyleClass().remove("wrong"));
        }
        if (epracownicy.getValue() == null){
            correct = false;
            epracownicy.getStyleClass().add("wrong");
        }else{
            while(epracownicy.getStyleClass().remove("wrong"));
        }
        if (zdata.getValue() == null || !zdata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            zdata.getStyleClass().add("wrong");
        }else{
            while(zdata.getStyleClass().remove("wrong"));
        }
        if (wdata.getValue() == null || !wdata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            wdata.getStyleClass().add("wrong");
        }else{
            while(wdata.getStyleClass().remove("wrong"));
        }
        return correct;
    }

    @FXML
    private void peselTyped(){
        ArrayList<String> pesels = new ArrayList<>();
        pesels.add("98092200114");
        pesels.add("94102199893");
        pesels.add("94603139293");
        pesels.add("92091233333");
        System.out.println(peselTextField.getText());
        if(peselTextField.getText().length() == 11)
            if(pesels.indexOf(peselTextField.getText()) == -1){
                while(peselTextField.getStyleClass().remove("good"));
                peselTextField.getStyleClass().add("wrong");
            }else{
                while(peselTextField.getStyleClass().remove("wrong"));
                peselTextField.getStyleClass().add("good");
            }
        else{
            while(peselTextField.getStyleClass().remove("wrong"));
            while(peselTextField.getStyleClass().remove("good"));
        }
    }

}
