package main.java.controller;

import javafx.fxml.FXML;

public class AddMagazyn {
    private Controller controller;
    private Magazyn magazyn;

    public AddMagazyn(Controller controller, Magazyn magazyn){
        this.controller = controller;
        this.magazyn = magazyn;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", magazyn);
    }
}
