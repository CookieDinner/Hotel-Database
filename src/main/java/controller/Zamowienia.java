package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Zamowienia extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Zamowienia(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addZamowienia.fxml", new AddZamowienia(controller, this));
    }

    @FXML
    public void initialize(){
        title.setText("Zamówienia");
        Label danie = new Label("Danie");
        Label data = new Label("Data");

        danie.setStyle("-fx-padding: 0 300 0 0;");
        danie.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 120 0 0;");
        data.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(danie, data);

        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_zamowienia");

            int i = 0;
            while(rs.next()){
                String vId = rs.getString("id_zamowienia");
                Button current = new Button(vId);
                fillableRows.getChildren().add(current);
                current.setOnAction(e->moreInfo("1"));
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
                i++;
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void search() {

    }

    public void moreInfo(String id) {
        controller.changeScene("addZamowienia.fxml", new AddZamowienia(controller, this, id));
    }
}
