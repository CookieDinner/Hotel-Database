package main.java.controller;

import javafx.fxml.FXML;

public class AddHala {
    private Controller controller;
    private Hale hale;

    public AddHala(Controller controller, Hale hale){
        this.controller = controller;
        this.hale = hale;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void addHale(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", hale);
    }
}
