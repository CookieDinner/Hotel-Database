package main.java.controller;

import javafx.fxml.FXML;
import main.java.base.DataBase;

public class Pokoje extends MainView {
    public Pokoje(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @FXML
    private void initialize(){
        title.setText("Pokoje");
    }
}
