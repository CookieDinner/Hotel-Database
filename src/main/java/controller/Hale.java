package main.java.controller;

import javafx.fxml.FXML;
import main.java.base.DataBase;

public class Hale extends MainView {
    public Hale(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @FXML
    private void initialize(){
        title.setText("Hale konferencyjne");
    }
}
