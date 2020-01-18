package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Dania implements MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Controller controller;
    private Connection con;
    private Statement stmt = null;
    private ResultSet rs = null;


    public Dania(Controller controller, Connection con){
        this.controller = controller;
        this.con = con;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Dania");
        Label nazwa = new Label("Nazwa");
        Label cena = new Label("Cena");
        Label dostepnosc = new Label("Dostępność");

        nazwa.setStyle("-fx-padding: 0 450 0 0;");
        nazwa.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 150 0 0;");
        cena.getStyleClass().add("tag");
        dostepnosc.setStyle("-fx-padding: 0 0 0 0;");
        dostepnosc.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(nazwa, cena, dostepnosc);

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_dania");

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
