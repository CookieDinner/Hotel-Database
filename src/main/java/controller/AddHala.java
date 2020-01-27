package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddHala {
    private Controller controller;
    private Hale hale;
    private String numerHali;
    private boolean look;
    @FXML
    private TextField numer, lMiejsc;
    @FXML
    private Button editButton, saveButton, delButton;

    public AddHala(Controller controller, Hale hale){
        this.controller = controller;
        this.hale = hale;
        this.look = false;
    }

    public AddHala(Controller controller, Hale hale, String numerHali){
        this.controller = controller;
        this.hale = hale;
        this.numerHali = numerHali;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if(look){
            try {
                saveButton.setVisible(false);
                String str = "SELECT * FROM hotel_hale_konferencyjne WHERE numer_hali=\'" + numerHali + "\'";
                PreparedStatement stmt = hale.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                numer.setText(rs.getString("numer_hali"));
                numer.setEditable(false);
                lMiejsc.setText(rs.getString("liczba_miejsc"));
                lMiejsc.setEditable(false);
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            editButton.setVisible(false);
            delButton.setVisible(false);
        }

    }
    @FXML
    private void addHale(){
        if(look && checkCorrectness()){
            saveButton.setVisible(false);
            numer.setEditable(false);
            lMiejsc.setEditable(false);
            try{
                CallableStatement cstmt = hale.dataBase.getCon().prepareCall("{call dodajHale(?,?,1)}");
                cstmt.setString(1, numer.getText());
                cstmt.setString(2, lMiejsc.getText());
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(checkCorrectness()){
            hale.dataBase.addHale(Integer.parseInt(numer.getText()), Integer.parseInt(lMiejsc.getText()));
            returnTo();
        }else{
        }

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", hale);
    }

    @FXML
    private void edit(){
        if(!look)
            return;
        saveButton.setVisible(true);
        numer.setEditable(false);
        lMiejsc.setEditable(true);
    }
    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_hale_konferencyjne WHERE numer_hali=" + numerHali;
            PreparedStatement stmt = hale.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!numer.getText().matches("^[1-9][0-9]?$")){
            correct = false;
            numer.getStyleClass().add("wrong");
        }else{
            while (numer.getStyleClass().remove("wrong"));
        }
        if (!lMiejsc.getText().matches("^[1-9][0-9]{0,4}$")){
            correct = false;
            lMiejsc.getStyleClass().add("wrong");
        }else{
            while (lMiejsc.getStyleClass().remove("wrong"));
        }
        return correct;
    }

}
