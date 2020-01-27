package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddDania {
    private Controller controller;
    private Dania dania;
    private String id;
    private boolean look, edit;
    @FXML
    private Button saveButton, editButton, delButton;
    @FXML
    private TextField nazwa, cena;
    @FXML
    private ComboBox<String> skladniki;
    @FXML
    private VBox skladnikiScroll;
    private ArrayList<String> arrSkladniki = new ArrayList<>();

    public AddDania(Controller controller, Dania dania){
        this.controller = controller;
        this.dania = dania;
        this.look = false;
        this.edit = true;
    }

    public AddDania(Controller controller, Dania dania, String id) {
        this.controller = controller;
        this.dania = dania;
        this.id = id;
        this.look = true;
        this.edit = false;
    }
    @FXML
    private void initialize(){
        if(look){
            try {
                String str = "SELECT * FROM hotel_dania WHERE nazwa=\'" + id + "\'";
                PreparedStatement stmt = dania.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                saveButton.setVisible(false);
                nazwa.setText(rs.getString("nazwa"));
                nazwa.setEditable(false);
                cena.setText(Float.toString(rs.getFloat("cena")));
                cena.setEditable(false);
                skladniki.setEditable(false);
                getSkladniki();
                for (String i : arrSkladniki)
                    skladnikiScroll.getChildren().add(createSkladnikButton(i));
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            editButton.setVisible(false);
            delButton.setVisible(false);
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(dania.dataBase.getSomeSkladniki());
        skladniki.setItems(observPracownicy);

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dania);
    }
    @FXML
    private void addDania(){
        if(look && checkCorrectness()){
            edit = false;
            saveButton.setVisible(false);
            nazwa.setEditable(false);
            cena.setEditable(false);
            skladniki.setEditable(false);
        }else if(checkCorrectness()) {
            dania.dataBase.addDanie(nazwa.getText(), Float.parseFloat(cena.getText()), arrSkladniki);
            returnTo();
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        edit = true;
        saveButton.setVisible(true);
        nazwa.setEditable(true);
        cena.setEditable(true);
        skladniki.setEditable(true);
    }

    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_dania WHERE nazwa=\'" + id  + "\'";
            PreparedStatement stmt = dania.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean isDanieCorrect(String danie){
        return true;
    }


    private Button createSkladnikButton(String nazwa){
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e->deleteButton(button));
        return button;
    }

    @FXML
    private void chooseSkladnik(){
        String temp_nazwa = skladniki.getValue().toString();
        arrSkladniki.add(temp_nazwa);
        skladnikiScroll.getChildren().add(createSkladnikButton(temp_nazwa));
    }

    private void deleteButton(Button toDelete){
        if(!edit)
            return;
        skladnikiScroll.getChildren().remove(toDelete);
        arrSkladniki.clear();
        for (Node i : skladnikiScroll.getChildren()) {
            arrSkladniki.add(((Button) i).getText());
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (nazwa.getText().isEmpty() || nazwa.getText().length() > 50){
            correct = false;
            nazwa.getStyleClass().add("wrong");
        }else{
            while (nazwa.getStyleClass().remove("wrong"));
        }
        if (!cena.getText().matches("[0-9]{1,7}(\\.[0-9]{0,2}){0,1}")){
            correct = false;
            cena.getStyleClass().add("wrong");
        }else{
            while (cena.getStyleClass().remove("wrong"));
        }
        if (skladnikiScroll.getChildren().isEmpty()){
            correct = false;
            skladniki.getStyleClass().add("wrong");
        }else{
            while (skladniki.getStyleClass().remove("wrong"));
        }
        return correct;
    }

    private void getSkladniki(){
        try {
            String str = "select d.nazwa, s.skladnik from hotel_dania d inner join hotel_sklad_dania s on (d.id_dania=s.id_dania) where d.nazwa=\'" + id + "\'";
            PreparedStatement stmt = dania.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                arrSkladniki.add(rs.getString("skladnik"));
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
