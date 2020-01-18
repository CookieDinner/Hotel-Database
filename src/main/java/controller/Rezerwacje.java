package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Rezerwacje implements MainView{
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

    public Rezerwacje(Controller controller, Connection con){
        this.controller = controller;
        this.con = con;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Rezerwacje");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label pokoj = new Label("Pokój");
        Label dataZameldowania = new Label("Data zameldowania");
        Label dataWymeldowania = new Label("Data wymeldowania");
        imie.setStyle("-fx-padding: 0 100 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 120 0 0;");
        nazwisko.getStyleClass().add("tag");
        pokoj.setStyle("-fx-padding: 0 50 0 0;");
        pokoj.getStyleClass().add("tag");
        dataZameldowania.setStyle("-fx-padding: 0 30 0 0;");
        dataZameldowania.getStyleClass().add("tag");
        dataWymeldowania.setStyle("-fx-padding: 0 0 0 0;");
        dataWymeldowania.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, pokoj, dataZameldowania, dataWymeldowania);

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_rezerwacje");

            int i = 0;
            while(rs.next()){
                String vId = rs.getString("id_rezerwacji");
                Node current = new Button(vId);
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
