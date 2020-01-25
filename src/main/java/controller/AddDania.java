package main.java.controller;

import javafx.fxml.FXML;

public class AddDania {
    private Controller controller;
    private Dania dania;

    public AddDania(Controller controller, Dania dania){
        this.controller = controller;
        this.dania = dania;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dania);
    }
    @FXML
    private void addDania(){

    }
}
