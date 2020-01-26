package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
    }
    @FXML
    private void initialize(){
        rabatCheckBox.setSelected(rabat);
        rabatCheck();
        peselTextField.setText(peselString);
        rabatTextField.setText(rabatString);
        zdata.setValue(dataZamString);
        wdata.setValue(dataWymString);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    @FXML
    private void rabatCheck(){
        rabat = rabatCheckBox.isSelected();
        if(rabat){
            rabatTextField.setEditable(true);
            while(rabatTextField.getStyleClass().remove("non-writable"));
            rabatTextField.getStyleClass().add("addTextWithButton");
        }else{
            rabatTextField.setEditable(false);
            rabatTextField.setText("");
            while(rabatTextField.getStyleClass().remove("addTextWithButton"));
            rabatTextField.getStyleClass().add("non-writable");
        }
    }
    @FXML
    private void addRezerwacje(){

    }
    @FXML
    private void addKlient(){
        peselString = peselTextField.getText();
        rabatString = rabatTextField.getText();
        dataZamString = zdata.getValue();
        dataWymString = wdata.getValue();
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, peselTextField.getText(), "addRezerwacje.fxml"));
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
