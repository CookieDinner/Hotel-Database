package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

public class MainView {
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public TextField searchField;
    @FXML
    public VBox fillableRows;
    public Controller controller;
    public DataBase dataBase;

    public void plus() {}
    public void search() {}
    public void moreInfo() {}

    public MainView(Controller controller, DataBase dataBase){
        this.controller = controller;
        this.dataBase = dataBase;
    }
}
