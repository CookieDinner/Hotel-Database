package main.java.controller;

import javafx.fxml.FXML;
import main.java.base.DataBase;

public class Pokoje extends MainView {
    public Pokoje(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus(){
        controller.changeScene("addPokoj.fxml", new AddPokoj(controller, this));
    }
    @FXML
    private void initialize(){
        title.setText("Pokoje");
    }
}
