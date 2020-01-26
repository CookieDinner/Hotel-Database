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

public class Dania extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;
    private CallableStatement cstmt = null;


    public Dania(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addDania.fxml", new AddDania(controller,  this));
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
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_dania order by nazwa asc");
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    public void populate(ResultSet rs){
        try{
            fillableRows.getChildren().clear();
            int i = 0;
            while(rs.next()){
                String vNazwa = rs.getString("nazwa");
                Float vCena = rs.getFloat("cena");
                cstmt = dataBase.getCon().prepareCall("{? = call dostepnoscDania(?)}");
                cstmt.setInt(2, rs.getInt("id_dania"));
                cstmt.registerOutParameter(1, Types.VARCHAR);
                cstmt.execute();
                String vDostepnosc = cstmt.getString(1);
                cstmt.close();
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nazwaL = new Label(vNazwa);
                nazwaL.setPrefWidth(510);
                Label cenaL = new Label(vCena.toString());
                cenaL.setPrefWidth(200);
                Label dostepnoscL = new Label(vDostepnosc);
                dostepnoscL.setPrefWidth(340);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(nazwaL, cenaL, dostepnoscL);
                current.setGraphic(aggregate);
                fillableRows.getChildren().add(current);
                current.setOnAction(e->moreInfo(vNazwa));
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
            PreparedStatement pstmt = dataBase.getCon().prepareStatement("SELECT * FROM hotel_dania where upper(nazwa) like '%'||UPPER(?)||'%' UNION SELECT * FROM hotel_dania where UPPER(dostepnoscDania(id_dania)) like '%'||UPPER(?)||'%'");
            pstmt.setString(1, searchField.getText());
            pstmt.setString(2, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void moreInfo(String nazwa) {
        controller.changeScene("addDania.fxml", new AddDania(controller, this, nazwa));
    }
}
