package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        controller.changeScene("addKonferencje.fxml", new AddController(dataBase));
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

            int i = 0;
            while(rs.next()){
                String vNazwa = rs.getString("nazwa");
                Node current = new Button(vNazwa);
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
        System.out.println(searchField.getText());
    }

    @Override
    public void moreInfo() {

    }
}
