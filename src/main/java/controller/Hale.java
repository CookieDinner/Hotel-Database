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

public class Hale extends MainView {

    private Statement stmt = null;
    private ResultSet rs = null;

    public Hale(Controller controller, DataBase dataBase) {
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addHala.fxml", new AddHala(controller, this));
    }

    @FXML
    private void initialize() {
        title.setText("Hale konferencyjne");
        Label numer = new Label("Numer");
        Label liczbaOsob = new Label("Liczba osób");
        numer.setStyle("-fx-padding: 0 300 0 0;");
        numer.getStyleClass().add("tag");
        liczbaOsob.setStyle("-fx-padding: 0 50 0 0;");
        liczbaOsob.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(numer, liczbaOsob);
        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_hale_konferencyjne order by numer_hali asc");
            populate(rs);
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void populate(ResultSet rs) {
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while (rs.next()) {
                String vNumer = rs.getString("numer_hali");
                String vLicz = rs.getString("liczba_miejsc");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label numL = new Label(vNumer);
                numL.setPrefWidth(400);
                Label liczL = new Label(vLicz);
                liczL.setPrefWidth(245);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(numL, liczL);
                current.setGraphic(aggregate);
                fillableRows.getChildren().add(current);
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
                current.setOnAction(e -> moreInfo(vNumer));
                i++;
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void search() {
        try {
            String str = "SELECT * FROM hotel_hale_konferencyjne where numer_hali like ?||'%' order by numer_hali";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(str);
            pstmt.setString(1, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void moreInfo(String id) {
        controller.changeScene("addHala.fxml", new AddHala(controller, this, id));
    }
}
