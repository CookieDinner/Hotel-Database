package main.java.controller;

import javafx.fxml.FXML;

public class AddPracownicy {
    private Controller controller;
    private Pracownicy pracownicy;

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
}
