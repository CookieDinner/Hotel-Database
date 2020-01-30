package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.java.Main;

import java.sql.*;
import java.util.ArrayList;

public class AddPracownicy {
    private Controller controller;
    private Pracownicy pracownicy;
    private boolean premia = false, look, edit;
    private String peselString;
    @FXML
    private Button saveButton, editButton, delButton;
    @FXML
    private TextField premiaTextField, imie, nazwisko, pesel, adres, etat, placa;
    @FXML
    private ComboBox<String> umowa;
    @FXML
    private DatePicker dataZa, dataUr;
    @FXML
    private CheckBox premiaBox;
    ObservableList<String> umowy = FXCollections.observableArrayList("o Pracę", "o Dzieło", "Zlecenie");

    public AddPracownicy(Controller controller, Pracownicy pracownicy){
        this.controller = controller;
        this.pracownicy = pracownicy;
        this.look = false;
        this.edit = true;
    }
    public AddPracownicy(Controller controller, Pracownicy pracownicy, String peselString){
        this.controller = controller;
        this.pracownicy = pracownicy;
        this.peselString = peselString;
        this.look = true;
        this.edit = false;
    }
    @FXML
    private void initialize(){
        umowa.setItems(umowy);
        if(look){
            saveButton.setVisible(false);
            try {
                saveButton.setVisible(false);
                String str = "SELECT * FROM hotel_pracownicy WHERE pesel=" + peselString;
                PreparedStatement stmt = pracownicy.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                imie.setText(rs.getString("imie"));
                imie.setEditable(false);
                nazwisko.setText(rs.getString("nazwisko"));
                nazwisko.setEditable(false);
                pesel.setText(rs.getString("pesel"));
                pesel.setEditable(false);
                adres.setText(rs.getString("adres"));
                adres.setEditable(false);
                etat.setText(rs.getString("etat"));
                etat.setEditable(false);
                placa.setText(rs.getString("placa"));
                placa.setEditable(false);
                umowa.setValue(rs.getString("umowa"));
                umowa.setDisable(true);
                premiaTextField.setText(rs.getString("premia"));
                premiaTextField.setEditable(false);
                dataUr.setValue(rs.getDate("data_urodzenia").toLocalDate());
                dataUr.setDisable(true);
                dataZa.setValue(rs.getDate("data_zatrudnienia").toLocalDate());
                dataZa.setDisable(true);
                if(rs.getFloat("premia") > 0)
                    premiaBox.setSelected(true);
                else
                    premiaBox.setSelected(false);
                premiaBox.setOnAction(e -> {
                    if (!edit) premiaBox.setSelected(!premiaBox.isSelected());
                });
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
        controller.changeScene("main_view.fxml", pracownicy);
    }
    @FXML
    private void addPracownicy(){
        if(look && checkCorrectness()){
            edit = false;
            saveButton.setVisible(false);
            imie.setEditable(false);
            nazwisko.setEditable(false);
            pesel.setEditable(false);
            adres.setEditable(false);
            etat.setEditable(false);
            placa.setEditable(false);
            umowa.setDisable(true);
            premiaTextField.setEditable(false);
            dataUr.setDisable(true);
            dataZa.setDisable(true);
            try {
                CallableStatement cstmt = pracownicy.dataBase.getCon().prepareCall("{call dodajPracownika(?,?,?,?,?,?,?,?,?,?,1)}");
                cstmt.setString(1, pesel.getText());
                cstmt.setString(2, imie.getText());
                cstmt.setString(3, nazwisko.getText());
                cstmt.setString(4, etat.getText());
                cstmt.setFloat(5, Float.parseFloat(placa.getText()));
                cstmt.setDate(6, Date.valueOf(dataUr.getValue()));
                cstmt.setDate(7, Date.valueOf(dataZa.getValue()));
                cstmt.setString(8, umowa.getValue());
                cstmt.setString(9, adres.getText());
                if(premiaBox.isSelected())
                    cstmt.setFloat(10, Float.parseFloat(premiaTextField.getText()));
                else
                    cstmt.setNull(10, Types.FLOAT);
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
        }else if(checkCorrectness()){
            pracownicy.dataBase.addPracownika(pesel.getText(), imie.getText(), nazwisko.getText(), etat.getText(),
                    Float.parseFloat(placa.getText()), Date.valueOf(dataUr.getValue()), Date.valueOf(dataZa.getValue()),
                    umowa.getValue(), adres.getText(), ((premiaBox.isSelected()) ? Float.parseFloat(premiaTextField.getText()) : 0));
            returnTo();
        }else{
        }
    }
    @FXML
    private void premiaCheck(){
        if(edit) {
            premia = !premia;
            if (premia) {
                premiaTextField.setEditable(true);
                while (premiaTextField.getStyleClass().remove("non-writableSmall")) ;
                premiaTextField.getStyleClass().add("addTextWithButtonSmall");
            } else {
                premiaTextField.setEditable(false);
                premiaTextField.setText("");
                while (premiaTextField.getStyleClass().remove("addTextWithButtonSmall")) ;
                premiaTextField.getStyleClass().add("non-writableSmall");
            }
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        edit = true;
        saveButton.setVisible(true);
        imie.setEditable(true);
        nazwisko.setEditable(true);
        pesel.setEditable(false);
        adres.setEditable(true);
        etat.setEditable(true);
        placa.setEditable(true);
        umowa.setDisable(false);
        premiaTextField.setEditable(true);
        dataUr.setDisable(false);
        dataZa.setDisable(false);
    }
    @FXML
    private void delete(){
        try {
            PreparedStatement stmt = pracownicy.dataBase.getCon().prepareStatement("SELECT id_rezerwacji FROM hotel_rezerwacje WHERE pracownik = ?");
            stmt.setString(1, pesel.getText());
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> rezerwacje = new ArrayList<>();
            while (rs.next()){
                rezerwacje.add(rs.getString("id_rezerwacji"));
            }
            rs.close();
            Alert alert = new Alert(Alert.AlertType.NONE, "Istnieją rezerwacje (" + rezerwacje.size() + "), one również zostaną usunięte.\n\nCzy nadal chcesz usunąć pracownika?", ButtonType.YES, ButtonType.NO);
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
                stmt = pracownicy.dataBase.getCon().prepareStatement("SELECT id_zamowienia FROM hotel_zamowienia WHERE pracownik = ?");
                stmt.setString(1, pesel.getText());
                rs = stmt.executeQuery();
                ArrayList<String> zamowienia = new ArrayList<>();
                while (rs.next()){
                    zamowienia.add(rs.getString("id_zamowienia"));
                }
                rs.close();
                alert = new Alert(Alert.AlertType.NONE, "Istnieją zamówienia (" + zamowienia.size() + "), one również zostaną usunięte.\n\nCzy nadal chcesz usunąć pracownika?", ButtonType.YES, ButtonType.NO);
                scrollPane = new ScrollPane();
                vBox = new VBox();
                scrollPane.setContent(vBox);
                scrollPane.setHmin(40);
                alert.getDialogPane().setExpandableContent(scrollPane);

                if(!zamowienia.isEmpty()){
                    for (String n : zamowienia){
                        vBox.getChildren().add(new Label("Zamówienie numer: "+n));
                    }
                    alert.showAndWait();
                }
                if(zamowienia.isEmpty() || alert.getResult() == ButtonType.YES) {
                    String str = "DELETE FROM hotel_pracownicy WHERE pesel = \'" + peselString + "\'";
                    stmt = pracownicy.dataBase.getCon().prepareStatement(str);
                    stmt.executeQuery();
                    stmt.close();
                    returnTo();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        boolean peselCorrect = true;
        boolean dataCorrect = true;
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
        if (adres.getText().isEmpty() || adres.getText().length() > 50){
            correct = false;
            adres.getStyleClass().add("wrong");
            adres.setTooltip(new Tooltip("Adres powinien się składać z 0-50 znaków"));
        }else{
            while (adres.getStyleClass().remove("wrong"));
            adres.setTooltip(null);
        }
        if (etat.getText().isEmpty() || etat.getText().length() > 30){
            correct = false;
            etat.getStyleClass().add("wrong");
            etat.setTooltip(new Tooltip("Adres powinien się składać z 0-50 znaków"));
        }else{
            while (etat.getStyleClass().remove("wrong"));
            etat.setTooltip(null);
        }
        if (!placa.getText().matches("^[0-9]{1,7}(\\.[0-9]{0,2}){0,1}$")){
            correct = false;
            placa.getStyleClass().add("wrong");
            placa.setTooltip(new Tooltip("Niepoprawna placa"));
        }else{
            while (placa.getStyleClass().remove("wrong"));
            placa.setTooltip(null);
        }
        if (umowa.getValue() == null){
            correct = false;
            umowa.getStyleClass().add("wrong");
            umowa.setTooltip(new Tooltip("Trzeba wybrać umowę"));
        }else{
            while (umowa.getStyleClass().remove("wrong"));
            umowa.setTooltip(null);
        }
        if (dataUr.getValue() == null || dataUr.getValue().toString().matches("^((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})$")){
            correct = false;
            dataCorrect = false;
            dataUr.getStyleClass().add("wrongDateSmall");
            while (dataUr.getStyleClass().remove("addDateSmall"));
            dataUr.setTooltip(new Tooltip("Niepoprawna data"));
        }else{
            while (dataUr.getStyleClass().remove("wrongDateSmall"));
            dataUr.getStyleClass().add("addDateSmall");
            dataUr.setTooltip(null);
        }
        if (dataZa.getValue() == null || dataZa.getValue().toString().matches("^((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})$")){
            correct = false;
            dataCorrect = false;
            dataZa.getStyleClass().add("wrongDateSmall");
            while (dataZa.getStyleClass().remove("addDateSmall"));
            dataZa.setTooltip(new Tooltip("Niepoprawna data"));
        }else{
            while (dataZa.getStyleClass().remove("wrongDateSmall"));
            dataZa.getStyleClass().add("addDateSmall");
            dataZa.setTooltip(null);
        }
        if (premiaBox.isSelected() && !premiaTextField.getText().matches("^[0-9]{1,7}(\\.[0-9]{0,2}){0,1}$")){
            correct = false;
            premiaTextField.getStyleClass().add("wrongSmall");
            while(premiaTextField.getStyleClass().remove("addTextWithButtonSmall"));
            premiaTextField.setTooltip(new Tooltip("Niepoprawna premia"));
        }else if (premiaBox.isSelected()){
            while(premiaTextField.getStyleClass().remove("wrongSmall"));
            premiaTextField.getStyleClass().add("addTextWithButtonSmall");
            premiaTextField.setTooltip(null);
        }
        if (peselCorrect)
            try {
                PreparedStatement stmt = pracownicy.dataBase.getCon().prepareStatement("SELECT pesel from hotel_pracownicy");
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
                    pesel.setTooltip(new Tooltip("Istnieje już taki pracownik"));
                }else{
                    while(pesel.getStyleClass().remove("wrong"));
                    pesel.setTooltip(null);
                }
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        if (dataCorrect && dataUr.getValue().compareTo(dataZa.getValue()) > 0){
            correct = false;
            dataZa.getStyleClass().add("wrongDateSmall");
            while (dataZa.getStyleClass().remove("addDateSmall"));
            dataZa.setTooltip(new Tooltip("Data zatrudnienia wcześniej niż urodzenia?"));
            dataUr.getStyleClass().add("wrongDateSmall");
            while (dataUr.getStyleClass().remove("addDateSmall"));
            dataUr.setTooltip(new Tooltip("Data zatrudnienia wcześniej niż urodzenia?"));
        }else if(dataCorrect){
            while (dataZa.getStyleClass().remove("wrongDateSmall"));
            dataZa.getStyleClass().add("addDateSmall");
            dataZa.setTooltip(null);
            while (dataUr.getStyleClass().remove("wrongDateSmall"));
            dataUr.getStyleClass().add("addDateSmall");
            dataUr.setTooltip(null);
        }
        return correct;
    }

}
