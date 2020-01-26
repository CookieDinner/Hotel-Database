package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.java.base.DataBase;

import java.sql.*;

public class Klienci extends MainView {

    private Statement stmt = null;
    private ResultSet rs = null;

    public Klienci(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }
    @Override
    public void plus() {
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, "", "main_view.fxml"));
    }

    @FXML
    private void initialize() {
        title.setText("Klienci");
        Label imie = new Label("Imię");
        Label nazwisko = new Label("Nazwisko");
        Label numerTel = new Label("Numer telefonu");
        Label pesel = new Label("Pesel");
        imie.setStyle("-fx-padding: 0 150 0 0;");
        imie.getStyleClass().add("tag");
        nazwisko.setStyle("-fx-padding: 0 150 0 0;");
        nazwisko.getStyleClass().add("tag");
        numerTel.setStyle("-fx-padding: 0 150 0 0;");
        numerTel.getStyleClass().add("tag");
        pesel.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(imie, nazwisko, numerTel, pesel);
        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_klienci order by nazwisko asc");
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
                String vTele = rs.getString("numer_telefonu");
                String vPesel = rs.getString("pesel");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nazwL = new Label(vNazwisko);
                nazwL.setPrefWidth(190);
                Label imieL = new Label(vImie);
                imieL.setPrefWidth(245);
                Label teleL = new Label(vTele);
                teleL.setPrefWidth(230);
                Label peseL = new Label(vPesel);
                peseL.setPrefWidth(110);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(nazwL, imieL, teleL, peseL);
                current.setGraphic(aggregate);
                current.setOnAction(e->moreInfo(vPesel));
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
                    "SELECT * FROM hotel_klienci where upper(nazwisko) like UPPER(?)||'%'\n" +
                    "UNION\n" +
                    "SELECT * FROM hotel_klienci where upper(imie) like UPPER(?)||'%') order by nazwisko";
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

    }

}
