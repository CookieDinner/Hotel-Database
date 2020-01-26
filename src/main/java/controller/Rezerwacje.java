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
            stmt = dataBase.getCon().createStatement();
            rs = stmt.executeQuery("SELECT * FROM hotel_rezerwacje");

            int i = 0;
            while(rs.next()){
                String vId = rs.getString("id_rezerwacji");
                Node current = new Button(vId);
                fillableRows.getChildren().add(current);
                current.getStyleClass().add("field");
                current.getStyleClass().add("tag");
                i++;
            }
            populate(rs);
            rs.close();
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void populate(ResultSet rs){
        Button button = new Button("Temp");
        button.getStyleClass().add("field");
        button.getStyleClass().add("tag");
        button.setOnAction(e->moreInfo("1"));
        fillableRows.getChildren().add(button);
    }

    @Override
    public void search() {

    }

    public void moreInfo(String id) {
        controller.changeScene("addRezerwacje.fxml", new AddRezerwacje(controller, this, id));
    }
}
