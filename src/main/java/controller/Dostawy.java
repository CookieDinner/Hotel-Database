package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

import java.sql.*;

public class Dostawy extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Dostawy(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Dostawy");
        Label dostawca = new Label("Dostawca");
        Label skladnik = new Label("Składnik");
        Label data = new Label("Data");
        Label cena = new Label("Cena");

        dostawca.setStyle("-fx-padding: 0 250 0 0;");
        dostawca.getStyleClass().add("tag");
        skladnik.setStyle("-fx-padding: 0 250 0 0;");
        skladnik.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 70 0 0;");
        data.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 0 0 0;");
        cena.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(dostawca, skladnik, data, cena);

        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_dostawcy");

            int i = 0;
            while(rs.next()){
                String vNazwa = rs.getString("nazwa");
                Node current = new Button(vNazwa);
                fillableRows.getChildren().add(current);
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

    @Override
    public void moreInfo() {

    }
}
