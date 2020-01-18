package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.sql.*;

public class Pracownicy implements MainView{
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

    public Pracownicy(Controller controller, Connection con){
        this.controller = controller;
        this.con = con;
    }

    @Override
    public void plus() {
        System.out.println("plus");
    }

    @FXML
    public void initialize(){
        title.setText("Pracownicy");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label etat = new Label("Etat");
        Label placa = new Label("Płaca");
        Label data = new Label("Data zatrudnienia");

        imie.setStyle("-fx-padding: 0 150 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 150 0 0;");
        nazwisko.getStyleClass().add("tag");
        etat.setStyle("-fx-padding: 0 75 0 0;");
        etat.getStyleClass().add("tag");
        placa.setStyle("-fx-padding: 0 75 0 0;");
        placa.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 0 0 0;");
        data.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, etat, placa, data);
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_pracownicy");

            int i = 0;
            while(rs.next()){
                String vNazwisko = rs.getString("nazwisko");
                Node current = new Button(vNazwisko);
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
        System.out.println("test");
    }
}
