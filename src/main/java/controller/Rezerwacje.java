package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

import java.sql.*;

public class Rezerwacje extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Rezerwacje(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addRezerwacje.fxml", new AddRezerwacje(controller, this));
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
            String str = "select k.imie, k.nazwisko, rp.pokoj, r.* from hotel_klienci k inner join hotel_rezerwacje r on (r.klient=k.pesel) inner join hotel_pracownicy p on (r.pracownik=p.pesel) inner join hotel_rezerwacja_pokoju rp on (r.id_rezerwacji=rp.rezerwacja)";
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery(str);
            populate(rs);
            rs.close();
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    private void populate(ResultSet rs){
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while (rs.next()) {
                String vImie = rs.getString("imie");
                String vNazwisko = rs.getString("nazwisko");
                Integer vPokoj = rs.getInt("pokoj");
                Date vDatz = rs.getDate("data_zameldowania");
                Date vDatw = rs.getDate("termin_wymeldowania");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label imieL = new Label(vImie);
                imieL.setPrefWidth(140);
                Label nazwL = new Label(vNazwisko);
                nazwL.setPrefWidth(210);
                Label pokL = new Label(vPokoj.toString());
                pokL.setPrefWidth(110);
                Label datzL = new Label(vDatz.toString());
                datzL.setPrefWidth(190);
                Label datwL = new Label(vDatw.toString());
                datwL.setPrefWidth(120);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(imieL, nazwL, pokL, datzL, datwL);
                current.setGraphic(aggregate);
                fillableRows.getChildren().add(current);
                Integer vId = rs.getInt("id_rezerwacji");
                current.setOnAction(e->moreInfo());
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
                current.setOnAction(e -> moreInfo(vId.toString()));
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
            String str = "select * from (select k.imie, k.nazwisko, rp.pokoj, r.* from hotel_klienci k inner join hotel_rezerwacje r on (r.klient=k.pesel) inner join hotel_pracownicy p on (r.pracownik=p.pesel) inner join hotel_rezerwacja_pokoju rp on (r.id_rezerwacji=rp.rezerwacja) where upper(k.imie) LIKE upper(?) || '%' \n" +
                         "UNION \n"+
                         "select k.imie, k.nazwisko, rp.pokoj, r.* from hotel_klienci k inner join hotel_rezerwacje r on (r.klient=k.pesel) inner join hotel_pracownicy p on (r.pracownik=p.pesel) inner join hotel_rezerwacja_pokoju rp on (r.id_rezerwacji=rp.rezerwacja) where upper(k.nazwisko) LIKE upper(?) || '%')";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(str);
            pstmt.setString(1, searchField.getText());
            pstmt.setString(2, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
            rs.close();
            pstmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void moreInfo(String id) {
        controller.changeScene("addRezerwacje.fxml", new AddRezerwacje(controller, this, id));
    }
}
