package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddPracownicy {
    private Controller controller;
    private Pracownicy pracownicy;
    private boolean premia = false;
    @FXML
    private TextField premiaTextField;

    public AddPracownicy(Controller controller, Pracownicy pracownicy){
        this.controller = controller;
        this.pracownicy = pracownicy;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", pracownicy);
    }
    @FXML
    private void addPracownicy(){

    }
    @FXML
    private void premiaCheck(){
        premia = !premia;
        if(premia){
            premiaTextField.setEditable(true);
            while(premiaTextField.getStyleClass().remove("non-writableSmall"));
            premiaTextField.getStyleClass().add("addTextWithButtonSmall");
        }else{
            premiaTextField.setEditable(false);
            premiaTextField.setText("");
            while(premiaTextField.getStyleClass().remove("addTextWithButtonSmall"));
            premiaTextField.getStyleClass().add("non-writableSmall");
        }
    }
}
