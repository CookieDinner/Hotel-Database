package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.Main;
import main.java.base.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddRezerwacje {
    private Controller controller;
    private Rezerwacje rezerwacje;
    private boolean rabat = false;
    private int which = 0;
    private String peselString, pracownikString, rabatString;
    private LocalDate dataZamString, dataWymString;
    @FXML
    private TextField rabatTextField;
    @FXML
    private TextField peselTextField;
    @FXML
    private ComboBox<String> epracownicy, epokoje;
    @FXML
    private DatePicker zdata, wdata;
    @FXML
    private CheckBox rabatCheckBox;
    @FXML
    private Button saveButton, editButton, delButton;
    @FXML
    private VBox pokojeScroll;
    private boolean look, edit = false;
    public DataBase dataBase;
    private String id;
    private ArrayList<String> pokoje = new ArrayList<>();

    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.dataBase = rezerwacje.dataBase;
        this.look = false;
        this.edit = true;
    }
    public AddRezerwacje(Controller controller, Rezerwacje rezerwacje, String id){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
        peselString = pracownikString = rabatString = "";
        dataWymString = dataZamString = null;
        this.dataBase = rezerwacje.dataBase;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        peselTextField.setTooltip(new Tooltip("Pesel klienta"));
        if (look) {
            try {
                String str = "SELECT * FROM hotel_rezerwacje r inner join hotel_pracownicy p on(r.pracownik=p.pesel) WHERE r.id_rezerwacji=" + id;
                PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                peselTextField.setText(rs.getString("klient"));
                peselTextField.setEditable(false);
                epracownicy.setValue(rs.getString("nazwisko"));
                epracownicy.setDisable(true);
                zdata.setValue(rs.getDate("data_zameldowania").toLocalDate());
                zdata.setDisable(true);
                wdata.setValue(rs.getDate("termin_wymeldowania").toLocalDate());
                wdata.setDisable(true);
                epokoje.setDisable(true);
                float temp_rabat = rs.getFloat("rabat");
                if(rabat)
                    rabatCheckBox.setSelected(true);
                else
                    rabatCheckBox.setSelected(false);
                rabatTextField.setText(Float.toString(temp_rabat));
                rabatTextField.setEditable(false);
                getPokoje();
                for (String i : pokoje)
                    pokojeScroll.getChildren().add(createSkladnikButton(i));
                saveButton.setVisible(false);
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            editButton.setVisible(false);
            delButton.setVisible(false);
            rabatCheckBox.setSelected(rabat);
            rabatCheck();
            peselTextField.setText(peselString);
            rabatTextField.setText(rabatString);
            zdata.setValue(dataZamString);
            wdata.setValue(dataWymString);
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(rezerwacje.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);

        ObservableList<String> observPokoje = FXCollections.observableArrayList();
        observPokoje.addAll(rezerwacje.dataBase.getSomePokoje());
        epokoje.setItems(observPokoje);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    @FXML
    private void rabatCheck(){
        if(edit) {
            rabat = rabatCheckBox.isSelected();
            if (rabat) {
                rabatTextField.setEditable(true);
                while (rabatTextField.getStyleClass().remove("non-writable")) ;
                rabatTextField.getStyleClass().add("addTextWithButton");
            } else {
                rabatTextField.setEditable(false);
                rabatTextField.setText("");
                while (rabatTextField.getStyleClass().remove("addTextWithButton")) ;
                rabatTextField.getStyleClass().add("non-writable");
            }
        }else{
            rabatCheckBox.setSelected(!rabatCheckBox.isSelected());
        }
    }
    @FXML
    private void addRezerwacje(){
        if(look && checkCorrectness()){
            peselTextField.setEditable(false);
            epracownicy.setDisable(true);
            zdata.setDisable(true);
            wdata.setDisable(true);
            rabatTextField.setEditable(false);
            epokoje.setDisable(false);
            edit = false;
            saveButton.setVisible(false);
            try{
                CallableStatement cstmt = rezerwacje.dataBase.getCon().prepareCall("{? = call dodajRezerwacje(?,?,?,?,?,?,1)}");
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.setInt(2, Integer.parseInt(id));
                cstmt.setDate(3, Date.valueOf(zdata.getValue()));
                cstmt.setDate(4, Date.valueOf(wdata.getValue()));
                if(rabatCheckBox.isSelected())
                    cstmt.setFloat(5, Float.parseFloat(rabatTextField.getText()));
                else
                    cstmt.setNull(5, Types.FLOAT);
                PreparedStatement pstmt = rezerwacje.dataBase.getCon().prepareStatement("SELECT pesel from hotel_pracownicy where nazwisko=?");
                pstmt.setString(1,epracownicy.getValue());
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                cstmt.setString(6, rs.getString("pesel"));
                cstmt.setString(7,peselTextField.getText());
                cstmt.execute();
                cstmt.close();
                rs.close();
                pstmt.close();
                String str = "DELETE FROM hotel_rezerwacja_pokoju where rezerwacja=" + id;
                PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
                stmt.executeQuery();
                stmt.close();
                for(String i : pokoje){
                    cstmt = rezerwacje.dataBase.getCon().prepareCall("{call dodajRezerwacjePokoju(?,?)}");
                    cstmt.setInt(1, Integer.parseInt(id));
                    cstmt.setInt(2, Integer.parseInt(i));
                    cstmt.execute();
                }
                cstmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else {
            if (checkCorrectness()) {
                try {
                    String str = "SELECT pesel FROM hotel_pracownicy where upper(nazwisko)=upper(\'" + epracownicy.getValue().toString() + "\')";
                    PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    String temp_rabat;
                    if (rabatCheckBox.isSelected())
                        rezerwacje.dataBase.addRezerwacje(Date.valueOf(zdata.getValue()), Date.valueOf(wdata.getValue()),
                                Float.parseFloat(rabatTextField.getText()), rs.getString("pesel"),
                                peselTextField.getText().toString(), pokoje);
                    else
                        rezerwacje.dataBase.addRezerwacje(Date.valueOf(zdata.getValue()), Date.valueOf(wdata.getValue()),
                                0, rs.getString("pesel"),
                                peselTextField.getText().toString(), pokoje);
                    rs.close();
                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                returnTo();
            }
        }
    }
    @FXML
    private void addKlient(){
        peselString = peselTextField.getText();
        rabatString = rabatTextField.getText();
        dataZamString = zdata.getValue();
        dataWymString = wdata.getValue();
        controller.changeScene("addKlienci.fxml", new AddKlienci(controller, this, peselTextField.getText(), "addRezerwacje.fxml",false));
    }
    @FXML
    private void edit() {
        if (!look)
            return;
        saveButton.setVisible(true);
        peselTextField.setEditable(true);
        epracownicy.setDisable(false);
        zdata.setDisable(false);
        wdata.setDisable(false);
        rabatTextField.setEditable(true);
        epokoje.setDisable(false);
        edit = true;
    }
    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_rezerwacje WHERE id_rezerwacji = " + id;
            PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Button createSkladnikButton(String nazwa){
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e->deleteButton(button));
        return button;
    }

    @FXML
    private void chooseSkladnik(){
        String temp_nazwa = epokoje.getValue().toString();
        pokoje.add(temp_nazwa);
        pokojeScroll.getChildren().add(createSkladnikButton(temp_nazwa));
    }

    private void deleteButton(Button toDelete){
        if(!edit)
            return;
        pokojeScroll.getChildren().remove(toDelete);
        pokoje.clear();
        for (Node i : pokojeScroll.getChildren()) {
            pokoje.add(((Button) i).getText());
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!peselTextField.getText().matches("^[0-9]{11}$")){
            correct = false;
            peselTextField.getStyleClass().add("wrong");
            peselTextField.setTooltip(new Tooltip("Niepoprawny pesel"));
        }else if(peselTextField.getText().matches("^[0-9]{11}$")){
            try {
                PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement("SELECT pesel from hotel_klienci");
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while(rs.next()){
                    if(peselTextField.getText().equals(rs.getString("pesel"))){
                        nope = true;
                        break;
                    }
                }
                if (!nope){
                    correct = false;
                    peselTextField.getStyleClass().add("wrong");
                    peselTextField.setTooltip(new Tooltip("Nie istnieje taki klient"));
                }else{
                    while(peselTextField.getStyleClass().remove("wrong"));
                    peselTextField.setTooltip(null);
                }
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        } else{
            while(peselTextField.getStyleClass().remove("wrong"));
            peselTextField.setTooltip(new Tooltip("Pesel klienta"));
        }
        if (epracownicy.getValue() == null){
            correct = false;
            epracownicy.getStyleClass().add("wrong");
            epracownicy.setTooltip(new Tooltip("Trzeba wybrać pracownika"));
        }else{
            while(epracownicy.getStyleClass().remove("wrong"));
            epracownicy.setTooltip(null);
        }
        if (zdata.getValue() == null || zdata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            zdata.getStyleClass().add("wrongDate");
            while(zdata.getStyleClass().remove("addDate"));
            zdata.setTooltip(new Tooltip("Niepoprawna data"));
        }else{
            while(zdata.getStyleClass().remove("wrongDate"));
            zdata.getStyleClass().add("addDate");
            zdata.setTooltip(null);
        }
        if (wdata.getValue() == null || wdata.getValue().toString().matches("((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})")){
            correct = false;
            wdata.getStyleClass().add("wrongDate");
            while(wdata.getStyleClass().remove("addDate"));
            wdata.setTooltip(new Tooltip("Niepoprawna data"));
        }else{
            while(wdata.getStyleClass().remove("wrongDate"));
            wdata.getStyleClass().add("addDate");
            wdata.setTooltip(null);
        }
        if(pokojeScroll.getChildren().isEmpty()){
            correct = false;
            epokoje.getStyleClass().add("wrong");
            epokoje.setTooltip(new Tooltip("Trzeba wybrać pokój"));
        }else{
            while(epokoje.getStyleClass().remove("wrong"));
            epokoje.setTooltip(null);
        }
        if(rabatCheckBox.isSelected() && !rabatTextField.getText().matches("^[0-9]{1,4}(\\.[0-9]{0,2}){0,1}$")){
            correct = false;
            rabatTextField.getStyleClass().add("wrong");
            rabatTextField.setTooltip(new Tooltip("Niepoprawny rabat"));
        }else if(rabatCheckBox.isSelected()){
            while (rabatTextField.getStyleClass().remove("wrong"));
            rabatTextField.setTooltip(null);
        }
        try{
            if (zdata.getValue() != null && wdata.getValue() != null) {
                int p = 1;
                for(String i : pokoje) {
                    CallableStatement cstmt = rezerwacje.dataBase.getCon().prepareCall("{? = call sprawdzPokoj(?,?,?)}");
                    cstmt.registerOutParameter(1, Types.INTEGER);
                    cstmt.setInt(2, Integer.parseInt(i));
                    cstmt.setDate(3, Date.valueOf(zdata.getValue()));
                    cstmt.setDate(4, Date.valueOf(wdata.getValue()));
                    cstmt.execute();
                    p = p & cstmt.getInt(1);
                }
                if(p == 0){
                    epokoje.getStyleClass().add("wrong");
                    epokoje.setTooltip(new Tooltip("Pokój zajęty"));
                    correct = false;
                }else{
                    while(epokoje.getStyleClass().remove("wrong"));
                    epokoje.setTooltip(null);
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return correct;
    }

    @FXML
    private void peselTyped() {

    }

    private void getPokoje(){
        try {
            String str = "select r.id_rezerwacji, rp.pokoj from hotel_rezerwacja_pokoju rp inner join hotel_rezerwacje r on (rp.rezerwacja=r.id_rezerwacji) where r.id_rezerwacji=\'" + id + "\'";
            PreparedStatement stmt = rezerwacje.dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                pokoje.add(rs.getString("pokoj"));
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}
