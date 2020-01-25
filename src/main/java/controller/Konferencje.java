package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.java.base.DataBase;

import java.sql.*;

public class Konferencje extends MainView{

    private Statement stmt = null;
    private ResultSet rs = null;


    public Konferencje(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addKonferencje.fxml", new AddController(dataBase, this, controller));
    }

    @FXML
    public void initialize(){
        title.setText("Konferencje");
        Label nazwa = new Label("Nazwa");
        Label data = new Label("Data");
        Label liczbaOsob = new Label("Liczba osób");
        Label halaKonferencyjna = new Label("Hala konferencyjna");
        nazwa.setStyle("-fx-padding: 0 300 0 0;");
        nazwa.getStyleClass().add("tag");
        data.setStyle("-fx-padding: 0 100 0 0;");
        data.getStyleClass().add("tag");
        liczbaOsob.setStyle("-fx-padding: 0 50 0 0;");
        liczbaOsob.getStyleClass().add("tag");
        halaKonferencyjna.setStyle("-fx-padding: 0 0 0 0;");
        halaKonferencyjna.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(nazwa, data, liczbaOsob, halaKonferencyjna);
        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_konferencje");
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
                String vNazwa = rs.getString("nazwa");
                Date vData = rs.getDate("data_konferencji");
                String vLOsob = rs.getString("liczba_osob");
                String vHala = rs.getString("hala_konferencyjna");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nazwaL = new Label(vNazwa);
                nazwaL.setPrefWidth(350);
                Label dataL = new Label(vData.toString());
                dataL.setPrefWidth(200);
                Label personCount = new Label(vLOsob);
                personCount.setPrefWidth(190);
                Label halaKonf = new Label(vHala);
                halaKonf.setPrefWidth(110);
                aggregate.setStyle("-fx-alignment: center;");
                aggregate.getChildren().addAll(nazwaL, dataL, personCount, halaKonf);
                current.setGraphic(aggregate);
                String id_konf = rs.getString("id_konferencji");
                current.setOnAction(e->moreInfo(id_konf));
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
            PreparedStatement pstmt = dataBase.getCon().prepareStatement("SELECT * FROM hotel_konferencje where upper(nazwa) like UPPER(?)||'%'");
            pstmt.setString(1, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void moreInfo(String id_konf) {
        controller.changeScene("show_view.fxml", new ShowKonf(id_konf, this, controller));
    }
}
