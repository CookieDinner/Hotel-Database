package main.java.controller;

import javafx.fxml.FXML;

public class AddDostawy {
    private Controller controller;
    private Dostawy dostawy;

    public AddDostawy(Controller controller, Dostawy dostawy){
        this.controller = controller;
        this.dostawy = dostawy;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawy);
    }
}
