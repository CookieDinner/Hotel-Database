package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddKlienci {
    private Controller controller;
    private Object toReturnTo;
    private String peselFilled;
    private String fxml;
    @FXML
    private TextField pesel;

    public AddKlienci(Controller controller, Object toReturnTo, String peselFilled, String fxml){
        this.controller = controller;
        this.toReturnTo = toReturnTo;
        this.peselFilled = peselFilled;
        this.fxml = fxml;
    }
    @FXML
    private void initialize(){
        pesel.setText(peselFilled);
    }
    @FXML
    private void returnTo(){
        controller.changeScene(fxml, toReturnTo);
    }
    @FXML
    private void addKlienci(){

    }
}
