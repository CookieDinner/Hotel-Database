package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class AddKlienci {
    private Controller controller;
    private Object toReturnTo;
    private String peselFilled;
    private String fxml;
    private boolean look;
    @FXML
    private Button saveButton, editButton, delButton;
    @FXML
    private TextField imie, nazwisko, pesel, numerTel, adresZa;

    public AddKlienci(Controller controller, Object toReturnTo, String peselFilled, String fxml, boolean look){
        this.controller = controller;
        this.toReturnTo = toReturnTo;
        this.peselFilled = peselFilled;
        this.fxml = fxml;
        this.look = look;
    }
    @FXML
    private void initialize(){
        pesel.setText(peselFilled);
        if(look){
            try {
                String str = "SELECT * FROM hotel_klienci WHERE pesel=\'" + peselFilled + "\'";
                PreparedStatement stmt = ((Klienci)toReturnTo).dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                saveButton.setVisible(false);
                imie.setText(rs.getString("imie"));
                imie.setEditable(false);
                nazwisko.setText(rs.getString("nazwisko"));
                nazwisko.setEditable(false);
                pesel.setEditable(false);
                numerTel.setText(rs.getString("numer_telefonu"));
                numerTel.setEditable(false);
                adresZa.setText(rs.getString("adres_zamieszkania"));    // TODO
                adresZa.setEditable(false);
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
    private void returnTo(){
        controller.changeScene(fxml, toReturnTo);
    }
    @FXML
    private void addKlienci(){
        if(look && checkCorrectness()) {
            saveButton.setVisible(false);
            imie.setEditable(false);
            nazwisko.setEditable(false);
            pesel.setEditable(false);
            numerTel.setEditable(false);
            adresZa.setEditable(false);
            try{
                CallableStatement cstmt = ((Klienci) toReturnTo).dataBase.getCon().prepareCall("{call dodajKlienta(?,?,?,?,?,1)}");
                cstmt.setString(1, pesel.getText());
                cstmt.setString(2, imie.getText());
                cstmt.setString(3, nazwisko.getText());
                cstmt.setString(4, numerTel.getText());
                cstmt.setString(5, adresZa.getText());
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
        }else if(checkCorrectness()) {
            if (toReturnTo.getClass() == Klienci.class) {
                ((Klienci) toReturnTo).dataBase.addKlienci(imie.getText(), nazwisko.getText(),
                        pesel.getText(), numerTel.getText(), adresZa.getText());
                returnTo();
            }
            else{
                ((AddRezerwacje) toReturnTo).dataBase.addKlienci(imie.getText(), nazwisko.getText(),
                        pesel.getText(), numerTel.getText(), adresZa.getText());
                returnTo();
            }
        }else{
        }
    }
    @FXML
    private void edit() {
        if (!look)
            return;
        saveButton.setVisible(true);
        imie.setEditable(true);
        nazwisko.setEditable(true);
        pesel.setEditable(false);
        numerTel.setEditable(true);
        adresZa.setEditable(true);
    }
    @FXML
    private void delete(){
        try {
            PreparedStatement stmt = controller.dataBase.getCon().prepareStatement("SELECT id_rezerwacji FROM hotel_rezerwacje WHERE klient = ?");
            stmt.setString(1, pesel.getText());
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> rezerwacje = new ArrayList<>();
            while (rs.next()){
                rezerwacje.add(rs.getString("id_rezerwacji"));
            }
            rs.close();
            Alert alert = new Alert(Alert.AlertType.NONE, "Istnieją rezerwacje (" + rezerwacje.size() + "), one również zostaną usunięte.\n\nCzy nadal chcesz usunąć klienta?", ButtonType.YES, ButtonType.NO);
            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            scrollPane.setContent(vBox);
            scrollPane.setHmin(40);
            alert.getDialogPane().setExpandableContent(scrollPane);

            if(!rezerwacje.isEmpty()){
                for (String n : rezerwacje){
                    vBox.getChildren().add(new Label("Rezerwacja numer: "+n));
                }
                alert.showAndWait();
            }

            if(rezerwacje.isEmpty() || alert.getResult() == ButtonType.YES) {
                String str = "DELETE FROM hotel_klienci WHERE pesel=\'" + peselFilled + "\'";
                stmt = ((Klienci) toReturnTo).dataBase.getCon().prepareStatement(str);
                stmt.executeQuery();
                stmt.close();
                returnTo();
            }
        }catch (Exception ex){
//            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        boolean peselCorrect = true;
        if (imie.getText().isEmpty() || imie.getText().length() > 30){
            correct = false;
            imie.getStyleClass().add("wrong");
            imie.setTooltip(new Tooltip("Imie powinno się składać z 0-30 znaków"));
        }else{
            while (imie.getStyleClass().remove("wrong"));
            imie.setTooltip(null);
        }
        if (nazwisko.getText().isEmpty() || nazwisko.getText().length() > 30){
            correct = false;
            nazwisko.getStyleClass().add("wrong");
            nazwisko.setTooltip(new Tooltip("Nazwisko powinno się składać z 0-30 znaków"));
        }else{
            while (nazwisko.getStyleClass().remove("wrong"));
            nazwisko.setTooltip(null);
        }
        if (!pesel.getText().matches("^[0-9]{11}$")){
            correct = false;
            peselCorrect = false;
            pesel.getStyleClass().add("wrong");
            pesel.setTooltip(new Tooltip("Niepoprawny pesel"));
        }else{
            while (pesel.getStyleClass().remove("wrong"));
            pesel.setTooltip(null);
        }
        if (!numerTel.getText().matches("^[0-9]{9}$")){
            correct = false;
            numerTel.getStyleClass().add("wrong");
            numerTel.setTooltip(new Tooltip("Niepoprawny numer telfonu"));
        }else{
            while (numerTel.getStyleClass().remove("wrong"));
            numerTel.setTooltip(null);
        }
        if (adresZa.getText().isEmpty() || adresZa.getText().length() > 50){
            correct = false;
            adresZa.getStyleClass().add("wrong");
            adresZa.setTooltip(new Tooltip("Adres powinien się składać z 0-50 znaków"));
        }else{
            while (adresZa.getStyleClass().remove("wrong"));
            adresZa.setTooltip(null);
        }
        if (peselCorrect && !look)
            try {
                PreparedStatement stmt = controller.dataBase.getCon().prepareStatement("SELECT pesel from hotel_klienci");
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while (rs.next()) {
                    if (pesel.getText().equals(rs.getString("pesel"))) {
                        nope = true;
                        break;
                    }
                }
                if (nope){
                    correct = false;
                    pesel.getStyleClass().add("wrong");
                    pesel.setTooltip(new Tooltip("Istnieje już taki klient"));
                }else{
                    while(pesel.getStyleClass().remove("wrong"));
                    pesel.setTooltip(null);
                }
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        return correct;
    }



}
