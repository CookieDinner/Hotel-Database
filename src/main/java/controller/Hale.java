package main.java.controller;

import javafx.fxml.FXML;
import main.java.base.DataBase;

public class Hale extends MainView {
    public Hale(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus(){
        controller.changeScene("addHala.fxml", new AddHala(controller, this));
    }
    @FXML
    private void initialize(){
        title.setText("Hale konferencyjne");
    }
}
