package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddRezerwacje {
    private Controller controller;
    private Rezerwacje rezerwacje;
    private boolean rabat = false;
    @FXML
    private TextField rabatTextField;
    @FXML
    private TextField peselTextField;
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    @FXML
    private void rabatCheck(){
        rabat = !rabat;
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
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, peselTextField.getText(), "addRezerwacje.fxml"));
    }

}
