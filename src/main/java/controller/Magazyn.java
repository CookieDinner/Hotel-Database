package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.base.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Magazyn extends MainView{
    @FXML
    public Label title;
    @FXML
    public HBox tagsHBox;
    @FXML
    public VBox fillableRows;
    @FXML
    private ImageView plusButton;

    private Statement stmt = null;
    private ResultSet rs = null;

    public Magazyn(Controller controller, DataBase dataBase){
        super(controller, dataBase);
    }

    @Override
    public void plus() {
        controller.changeScene("addSkladnik.fxml", new AddSkladnik(controller, this));
    }

    @FXML
    public void initialize(){
        //plusButton.setImage(null);
        title.setText("Magazyn");
        Label skladnik = new Label("Składnik");
        Label ilosc = new Label("Ilość");
        Label dostawca = new Label("Dostawca");
        Label cena = new Label("Cena");

        skladnik.setStyle("-fx-padding: 0 250 0 0;");
        skladnik.getStyleClass().add("tag");
        ilosc.setStyle("-fx-padding: 0 50 0 0;");
        ilosc.getStyleClass().add("tag");
        dostawca.setStyle("-fx-padding: 0 250 0 0;");
        dostawca.getStyleClass().add("tag");
        cena.setStyle("-fx-padding: 0 0 0 0;");
        cena.getStyleClass().add("tag");
        tagsHBox.getChildren().addAll(skladnik, ilosc, dostawca, cena);

        try {
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT s.*, d.nazwa as \"NAZWA_DOSTAWCY\" FROM hotel_skladniki s inner join hotel_dostawcy d ON (d.nip=s.dostawca) order by s.nazwa asc");
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
                String vNazwa = rs.getString("nazwa");
                Integer vIlosc = rs.getInt("stan_magazynu");
                String vDostawca = rs.getString("nazwa_dostawcy");
                Float vCena = rs.getFloat("cena");
                Button current = new Button();
                HBox aggregate = new HBox();
                Label nazwaL = new Label(vNazwa);
                nazwaL.setPrefWidth(330);
                Label iloscL = new Label(vIlosc.toString());
                iloscL.setPrefWidth(80);
                Label dostL = new Label(vDostawca.toString());
                dostL.setPrefWidth(330);
                Label cenaL = new Label(vCena.toString());
                cenaL.setPrefWidth(110);
                aggregate.setStyle("-fx-alignment: center-left;");
                aggregate.getChildren().addAll(nazwaL, iloscL, dostL, cenaL);
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
            String st = "SELECT s.*, d.nazwa as \"NAZWA_DOSTAWCY\" FROM hotel_skladniki s inner join hotel_dostawcy d ON (d.nip=s.dostawca) where upper(s.nazwa) like UPPER(?)||'%'\n" +
                    "UNION \n" +
                    "SELECT s.*, d.nazwa as \"NAZWA_DOSTAWCY\" FROM hotel_skladniki s inner join hotel_dostawcy d ON (d.nip=s.dostawca) where upper(d.nazwa) like UPPER(?)||'%'";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(st);
            pstmt.setString(1, searchField.getText());
            pstmt.setString(2, searchField.getText());
            rs = pstmt.executeQuery();
            populate(rs);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void moreInfo(String vNazwa) {
        controller.changeScene("addSkladnik.fxml", new AddSkladnik(controller, this, vNazwa));
    }
}
