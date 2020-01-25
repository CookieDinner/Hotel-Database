package main.java.controller;

import javafx.fxml.FXML;

public class AddZamowienia {
    private Controller controller;
    private Zamowienia zamowienia;
    public AddZamowienia(Controller controller, Zamowienia zamowienia){
        this.controller = controller;
        this.zamowienia = zamowienia;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", zamowienia);
    }
    @FXML
    private void addRezerwacje(){

    }
}
