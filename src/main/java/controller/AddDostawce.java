package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddDostawce {
    private Controller controller;
    private Dostawcy dostawcy;
    private String id;
    private boolean look;
    @FXML
    private Button editButton, saveButton, delButton;
    @FXML
    private TextField nip, enazwa, adres, numer_telefonu;

    public AddDostawce(Controller controller, Dostawcy dostawcy){
        this.controller = controller;
        this.dostawcy = dostawcy;
        this.look = false;
    }
    public AddDostawce(Controller controller, Dostawcy dostawcy, String id){
        this.controller = controller;
        this.dostawcy = dostawcy;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if(look){
            try {
                saveButton.setVisible(false);
                String str = "SELECT * FROM hotel_dostawcy WHERE nip = \'" + id + "\'";
                PreparedStatement stmt = dostawcy.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                nip.setText(rs.getString("nip"));
                nip.setEditable(false);
                enazwa.setText(rs.getString("nazwa"));
                enazwa.setEditable(false);
                adres.setText(rs.getString("adres"));
                adres.setEditable(false);
                numer_telefonu.setText(rs.getString("numer_telefonu"));
                numer_telefonu.setEditable(false);
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
    private void addDostawce(){
        if(look && checkCorrectness()) {
            saveButton.setVisible(false);
            nip.setEditable(false);
            enazwa.setEditable(false);
            adres.setEditable(false);
            numer_telefonu.setEditable(false);
            try{
                CallableStatement cstmt = dostawcy.dataBase.getCon().prepareCall("{call dodajDostawce(?,?,?,?,1)}");
                cstmt.setString(1, nip.getText());
                cstmt.setString(2, enazwa.getText());
                cstmt.setString(3, adres.getText());
                cstmt.setString(4, numer_telefonu.getText());
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(checkCorrectness()) {
            dostawcy.dataBase.addDostawce(nip.getText(), enazwa.getText(), adres.getText(), numer_telefonu.getText());
            returnTo();
        }else{
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        saveButton.setVisible(true);
        saveButton.setVisible(true);
        nip.setEditable(false);
        enazwa.setEditable(true);
        adres.setEditable(true);
        numer_telefonu.setEditable(true);
    }
    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_dostawcy WHERE nip = \'" + id + "\'";
            PreparedStatement stmt = dostawcy.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!nip.getText().matches("^[0-9]{10}$")){
            correct = false;
            nip.getStyleClass().add("wrong");
            nip.setTooltip(new Tooltip("Nip nie jest poprawny"));
        }else{
            while(nip.getStyleClass().remove("wrong"));
            nip.setTooltip(null);
        }
        if (enazwa.getText().isEmpty() || enazwa.getText().length() > 30){
            correct = false;
            enazwa.getStyleClass().add("wrong");
            enazwa.setTooltip(new Tooltip("Nazwa musi się składać z 0-30 znaków"));
        }else{
            while (enazwa.getStyleClass().remove("wrong"));
            enazwa.setTooltip(null);
        }
        if (adres.getText().isEmpty() || adres.getText().length() > 50){
            correct = false;
            adres.getStyleClass().add("wrong");
            adres.setTooltip(new Tooltip("Adres musi się składać z 0-50 znaków"));
        }else{
            while (adres.getStyleClass().remove("wrong"));
            adres.setTooltip(null);
        }
        if (!numer_telefonu.getText().matches("^[0-9]{9}$")){
            correct = false;
            numer_telefonu.getStyleClass().add("wrong");
            numer_telefonu.setTooltip(new Tooltip("Niepoprawny numer telefonu"));
        }else{
            while (numer_telefonu.getStyleClass().remove("wrong"));
            numer_telefonu.setTooltip(null);
        }
        return correct;
    }

}
