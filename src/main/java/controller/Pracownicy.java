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
import java.sql.*;

public class Pracownicy extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Pracownicy(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addPracownicy.fxml", new AddPracownicy(controller, this));
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
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_pracownicy order by nazwisko asc");
            populate(rs);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void populate(ResultSet rs){
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while(rs.next()){
                String vNazwisko = rs.getString("nazwisko");
                String vImie = rs.getString("imie");
                String vEtat = rs.getString("etat");
                String vPlaca = rs.getString("placa");
                Date vData = rs.getDate("data_zatrudnienia");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nazwL = new Label(vNazwisko);
                nazwL.setPrefWidth(190);
                Label imieL = new Label(vImie);
                imieL.setPrefWidth(200);
                Label etatL = new Label(vEtat);
                etatL.setPrefWidth(145);
                Label placaL = new Label(vPlaca);
                placaL.setPrefWidth(110);
                Label dataL = new Label(vData.toString());
                dataL.setPrefWidth(110);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(nazwL, imieL, etatL, placaL, dataL);
                current.setGraphic(aggregate);
                String pesel = rs.getString("pesel");
                current.setOnAction(e -> moreInfo(pesel));
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
        try {
            String str = "SELECT * FROM (\n" +
                    "SELECT * FROM hotel_pracownicy where upper(nazwisko) like UPPER(?)||'%'\n" +
                    "UNION\n" +
                    "SELECT * FROM hotel_pracownicy where upper(imie) like UPPER(?)||'%') order by nazwisko";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(str);
            pstmt.setString(1, searchField.getText());
            pstmt.setString(2, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void moreInfo(String pesel) {
        controller.changeScene("addPracownicy.fxml", new AddPracownicy(controller, this, pesel));
    }
}
