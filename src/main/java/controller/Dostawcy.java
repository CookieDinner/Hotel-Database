package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dostawcy extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Dostawcy(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addDostawy.fxml", new AddDostawy(controller, this));
    }

    @FXML
    public void initialize(){
        title.setText("Dostawcy");
        Label dostawca = new Label("NIP");
        Label skladnik = new Label("Nazwa");
        //Label data = new Label("Data");
        //Label cena = new Label("Cena");

        dostawca.setStyle("-fx-padding: 0 300 0 0;");
        dostawca.getStyleClass().add("tag");
        skladnik.setStyle("-fx-padding: 0 250 0 0;");
        skladnik.getStyleClass().add("tag");
        //data.setStyle("-fx-padding: 0 70 0 0;");
        //data.getStyleClass().add("tag");
        //cena.setStyle("-fx-padding: 0 0 0 0;");
        //cena.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(dostawca, skladnik);//, data, cena);
        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_dostawcy");
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }


    }

    public void populate(ResultSet rs){
        try {
            fillableRows.getChildren().clear();
            int i = 0;
            while(rs.next()){
                String vNip = rs.getString("nip");
                String vNazwa = rs.getString("nazwa");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nipL = new Label(vNip);
                nipL.setPrefWidth(340);
                Label nazwaL = new Label(vNazwa);
                nazwaL.setPrefWidth(510);
                aggregate.setStyle("-fx-alignment: center;");
                aggregate.getChildren().addAll(nipL, nazwaL);
                current.setGraphic(aggregate);
                fillableRows.getChildren().add(current);
                current.setOnAction(e->moreInfo(vNip));
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
            PreparedStatement pstmt = dataBase.getCon().prepareStatement("SELECT * FROM hotel_dostawcy where upper(nazwa) like UPPER(?)||'%' UNION SELECT * FROM hotel_dostawcy where nip like ?||'%'");
            pstmt.setString(1, searchField.getText());
            pstmt.setString(2, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }


    public void moreInfo(String nip) {
        controller.changeScene("addDostawy.fxml", new AddDostawy(controller, this, nip));
    }
}
