package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.java.base.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pokoje extends MainView {

    private Statement stmt = null;
    private ResultSet rs = null;

    public Pokoje(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }

    @Override
    public void plus(){
        controller.changeScene("addPokoj.fxml", new AddPokoj(controller, this));
    }

    @FXML
    private void initialize(){
        title.setText("Pokoje");
        Label numer = new Label("Numer");
        Label cena = new Label("Cena");
        Label liczbaLozek = new Label("Liczba łóżek");
        Label telewizor = new Label("Telewizor");
        Label lazienka = new Label("Łazienka");
        numer.setStyle("-fx-padding: 0 50 0 0;");
        numer.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 150 0 0;");
        cena.getStyleClass().add("tag");
        liczbaLozek.setStyle("-fx-padding: 0 200 0 0;");
        liczbaLozek.getStyleClass().add("tag");
        telewizor.setStyle("-fx-padding: 0 50 0 0;");
        telewizor.getStyleClass().add("tag");
        lazienka.setStyle("-fx-padding: 0 0 0 0;");
        lazienka.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(numer, cena, liczbaLozek, telewizor, lazienka);
        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_pokoje order by numer asc");
            populate(rs);
            rs.close();
            stmt.close();
        } catch(SQLException ex){
//            ex.printStackTrace();
        }
    }
    public void populate(ResultSet rs){
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while(rs.next()){
                String vNumer = rs.getString("numer");
                String vCena = rs.getString("cena_za_dobe");
                String vLoz = rs.getString("liczba_lozek");
                int vTele = rs.getInt("czy_telewizor");
                int vLaz = rs.getInt("czy_lazienka");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label numL = new Label(vNumer);
                numL.setPrefWidth(100);
                Label cenaL = new Label(vCena);
                cenaL.setPrefWidth(245);
                Label lozL = new Label(vLoz);
                lozL.setPrefWidth(276);
                Label teleL = new Label((vTele == 0) ? "Nie" : "Tak");
                teleL.setPrefWidth(115);
                Label lazL = new Label((vLaz == 0) ? "Nie" : "Tak");
                lazL.setPrefWidth(110);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(numL, cenaL, lozL, teleL, lazL);
                current.setGraphic(aggregate);
                fillableRows.getChildren().add(current);
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
                current.setOnAction(e->moreInfo(vNumer));
                i++;
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex){
//            ex.printStackTrace();
        }
    }

    @Override
    public void search() {
        try {
            String str = "SELECT * FROM hotel_pokoje where numer like ?||'%' order by numer";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(str);
            pstmt.setString(1, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
            rs.close();
            pstmt.close();
        }catch(SQLException ex){
//            ex.printStackTrace();
        }
    }
    public void moreInfo(String numer) {
        controller.changeScene("addPokoj.fxml", new AddPokoj(controller, this, numer));
    }
}
