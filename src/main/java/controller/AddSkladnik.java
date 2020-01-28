package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import main.java.Main;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class AddSkladnik {
    private Controller controller;
    private Magazyn magazyn;
    private boolean look, edit = false;
    String nazwa;
    @FXML
    private TextField enazwa, stanM, cena;
    @FXML
    private ComboBox<String> dostawca;
    @FXML
    private Button saveButton, editButton, delButton;
    private ArrayList<String> arrDostawcy = new ArrayList<>();

    public AddSkladnik(Controller controller, Magazyn magazyn){
        this.controller = controller;
        this.magazyn = magazyn;
        this.look=false;
    }
    public AddSkladnik(Controller controller, Magazyn magazyn, String nazwa){
        this.controller = controller;
        this.magazyn = magazyn;
        this.nazwa=nazwa;
        this.look=true;
    }
    @FXML
    private void initialize(){
        if (look) {
            try {
                String str = "SELECT s.*, d.nazwa as \"dosNazwa\" FROM hotel_skladniki s INNER JOIN hotel_dostawcy d on (s.dostawca = d.nip) WHERE s.nazwa=\'" + nazwa + "\'";
                PreparedStatement stmt = magazyn.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                enazwa.setText(rs.getString("nazwa"));
                enazwa.setEditable(false);
                stanM.setText(rs.getString("stan_magazynu"));
                stanM.setEditable(false);
                cena.setText(rs.getString("cena"));
                cena.setEditable(false);
                dostawca.setValue(rs.getString("dosNazwa"));
                dostawca.setDisable(true);
                saveButton.setVisible(false);
                stmt.close();
                rs.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }else {
            editButton.setVisible(false);
            delButton.setVisible(false);

        }
        ObservableList<String> observDostawcy = FXCollections.observableArrayList();
        observDostawcy.addAll(magazyn.dataBase.getSomeDostawcy());
        dostawca.setItems(observDostawcy);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", magazyn);
    }
    @FXML
    private void addSkladnik(){
        if(look && checkCorrectness()){
            enazwa.setEditable(false);
            cena.setEditable(false);
            stanM.setEditable(false);
            dostawca.setDisable(true);
            edit = false;
            saveButton.setVisible(false);
            try{
                String str = "SELECT nip FROM hotel_dostawcy WHERE upper(nazwa) = upper(\'" + dostawca.getValue() + "\')";
                PreparedStatement stmt = magazyn.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String temp_nip = rs.getString("nip");
                rs.close();
                stmt.close();
                CallableStatement cstmt = magazyn.dataBase.getCon().prepareCall("{call dodajSkladnik(?,?,?,?,1)}");
                cstmt.setString(1, enazwa.getText());
                cstmt.setInt(2, Integer.parseInt(stanM.getText()));
                cstmt.setFloat(3, Float.parseFloat(cena.getText()));
                cstmt.setString(4, temp_nip);
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(checkCorrectness()){
            try{
                String str = "SELECT nip FROM hotel_dostawcy WHERE upper(nazwa) = upper(\'" + dostawca.getValue() + "\')";
                PreparedStatement stmt = magazyn.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String temp_nip = rs.getString("nip");
                rs.close();
                stmt.close();
                CallableStatement cstmt = magazyn.dataBase.getCon().prepareCall("{call dodajSkladnik(?,?,?,?,0)}");
                cstmt.setString(1, enazwa.getText());
                cstmt.setInt(2, Integer.parseInt(stanM.getText()));
                cstmt.setFloat(3, Float.parseFloat(cena.getText()));
                cstmt.setString(4, temp_nip);
                cstmt.execute();
                cstmt.close();
                returnTo();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    @FXML
    private void edit() {
        if (!look)
            return;
        saveButton.setVisible(true);
        enazwa.setEditable(false);
        stanM.setEditable(true);
        cena.setEditable(true);
        dostawca.setDisable(false);
        edit = true;
    }
    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_skladniki WHERE upper(nazwa) = upper(\'"+ nazwa + "\')";
            PreparedStatement stmt = magazyn.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (enazwa.getText().isEmpty() || enazwa.getText().length() > 30){
            correct = false;
            enazwa.getStyleClass().add("wrong");
            enazwa.setTooltip(new Tooltip("Nazwa musi się składać z 0-30 znaków"));
        }else{
            while(enazwa.getStyleClass().remove("wrong"));
            enazwa.setTooltip(null);
        }
        if (!stanM.getText().matches("[0-9]{1,5}")){
            correct = false;
            stanM.getStyleClass().add("wrong");
            stanM.setTooltip(new Tooltip("Niepoprawny stan magazynu"));
        }else{
            while (stanM.getStyleClass().remove("wrong"));
            stanM.setTooltip(null);
        }
        if (!cena.getText().matches("[0-9]{1,7}(\\.[0-9]{0,2}){0,1}")){
            correct = false;
            cena.getStyleClass().add("wrong");
            cena.setTooltip(new Tooltip("Cena powinna byc liczbą"));
        }else{
            while (cena.getStyleClass().remove("wrong"));
            cena.setTooltip(null);
        }
        if (dostawca.getValue() == null){
            correct = false;
            dostawca.getStyleClass().add("wrong");
            dostawca.setTooltip(new Tooltip("Dostawca musi być wybrany"));
        }else{
            while (dostawca.getStyleClass().remove("wrong"));
            dostawca.setTooltip(null);
        }
        return correct;
    }

    @FXML
    private void chooseDostawce(){
        System.out.println("test");
    }

}
