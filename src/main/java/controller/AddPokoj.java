package main.java.controller;

import javafx.fxml.FXML;

public class AddPokoj {
    private Controller controller;
    private Pokoje pokoje;

    public AddPokoj(Controller controller, Pokoje pokoje){
        this.controller = controller;
        this.pokoje = pokoje;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void addPokoj(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", pokoje);
    }
}
