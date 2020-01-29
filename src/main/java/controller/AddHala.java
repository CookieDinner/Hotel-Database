package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

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
//                ex.printStackTrace();
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
//                ex.printStackTrace();
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
            Alert alert = new Alert(Alert.AlertType.NONE, "Delete ?", ButtonType.YES, ButtonType.NO);
            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            Button button = new Button("halo");
            vBox.getChildren().addAll(button);
            scrollPane.setContent(vBox);
            alert.getDialogPane().setExpandableContent(scrollPane);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES) {   // TODO: gdy isnieje konferencja
                String str = "DELETE FROM hotel_hale_konferencyjne WHERE numer_hali=" + numerHali;
                PreparedStatement stmt = hale.dataBase.getCon().prepareStatement(str);
                stmt.executeQuery();
                stmt.close();
                returnTo();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        boolean numerCorrect = true;
        if (!numer.getText().matches("^[1-9][0-9]?$")){
            correct = false;
            numerCorrect = false;
            numer.getStyleClass().add("wrong");
            numer.setTooltip(new Tooltip("Niepoprawny numer hali"));
        }else{
            while (numer.getStyleClass().remove("wrong"));
            numer.setTooltip(null);
        }
        if (!lMiejsc.getText().matches("^[1-9][0-9]{0,4}$")){
            correct = false;
            lMiejsc.getStyleClass().add("wrong");
            lMiejsc.setTooltip(new Tooltip("Niepoprawna liczba miejsc"));
        }else{
            while (lMiejsc.getStyleClass().remove("wrong"));
            lMiejsc.setTooltip(null);
        }
        if (numerCorrect && !look)
            try {
                PreparedStatement stmt = hale.dataBase.getCon().prepareStatement("SELECT numer_hali from hotel_hale_konferencyjne");
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while (rs.next()) {
                    if (numer.getText().equals(rs.getString("numer_hali"))) {
                        nope = true;
                        break;
                    }
                }
                if (nope){
                    correct = false;
                    numer.getStyleClass().add("wrong");
                    numer.setTooltip(new Tooltip("Istnieje już taka hala"));
                }else{
                    while(numer.getStyleClass().remove("wrong"));
                    numer.setTooltip(null);
                }
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        return correct;
    }

}
