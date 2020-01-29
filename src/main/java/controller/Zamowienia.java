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
        Label id = new Label("Id Zamowienia");
        Label danie = new Label("Danie");
        Label data = new Label("Data");

        id.setStyle("-fx-padding: 0 100 0 0;");
        id.getStyleClass().add("tag");
        danie.setStyle("-fx-padding: 0 200 0 0;");
        danie.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 120 0 0;");
        data.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(id, danie, data);

        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT d.*, z.* FROM hotel_zamowienie_dania zd inner join hotel_dania d on(zd.id_dania=d.id_dania) inner join hotel_zamowienia z on(zd.zamowienie=z.id_zamowienia) order by id_zamowienia asc");
            populate(rs);
            rs.close();
            stmt.close();

        }catch(SQLException ex){
//            ex.printStackTrace();
        }
    }
    public void populate(ResultSet rs){
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while(rs.next()){
                Integer vId = rs.getInt("id_zamowienia");
                String vNazwa = rs.getString("nazwa");
                Date vData = rs.getDate("data_zamowienia");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label idL = new Label(Integer.toString(vId));
                idL.setPrefWidth(210);
                Label nazwaL = new Label(vNazwa);
                nazwaL.setPrefWidth(250);
                Label dataL = new Label(vData.toString());
                dataL.setPrefWidth(245);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(idL, nazwaL, dataL);
                current.setGraphic(aggregate);
                int id = rs.getInt("id_zamowienia");
                current.setOnAction(e->moreInfo(Integer.toString(id)));
                fillableRows.getChildren().add(current);
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
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
            String str = "SELECT d.*, z.* FROM hotel_zamowienie_dania zd inner join hotel_dania d on(zd.id_dania=d.id_dania) inner join hotel_zamowienia z on(zd.zamowienie=z.id_zamowienia) " +
                    "where UPPER(nazwa) like UPPER(?)||'%' order by nazwa asc";
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

    public void moreInfo(String id) {
        controller.changeScene("addZamowienia.fxml", new AddZamowienia(controller, this, id));
    }
}
