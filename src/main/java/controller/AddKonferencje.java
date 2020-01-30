package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.base.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddKonferencje {
    @FXML
    private TextField enazwa;
    @FXML
    private DatePicker edata;
    @FXML
    private TextField eliczba_osob;
    @FXML
    private ComboBox<String> ehala;
    @FXML
    private ComboBox<String> epracownicy;
    @FXML
    private ImageView editImageView;
    @FXML
    private Button editButton, saveButton, delButton;
    @FXML
    private VBox pracownicyScroll;

    private DataBase dataBase;
    private Konferencje konferencje;
    private Controller controller;
    private boolean look, edit;
    private String id_konf = "";
    private ArrayList<String> pracownicy = new ArrayList<>();

    public AddKonferencje(DataBase dataBase, Konferencje konferencje, Controller controller){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.look = false;
        this.edit = true;
    }
    public AddKonferencje(DataBase dataBase, Konferencje konferencje, Controller controller, String id_konf){
        this.dataBase = dataBase;
        this.konferencje = konferencje;
        this.controller = controller;
        this.id_konf = id_konf;
        this.look = true;
        this.edit = false;
    }
    @FXML
    public void addKonferencje(){
        if(look && checkCorrectness()){
            edit = false;
            String pesel = null;
            enazwa.setEditable(false);
            edata.setDisable(true);
            ehala.setDisable(true);
            epracownicy.setDisable(true);
            saveButton.setVisible(false);
            try{
                CallableStatement cstmt = konferencje.dataBase.getCon().prepareCall("{? = call dodajKonferencje(?,?,?,?,?,1)}");
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.setInt(2, Integer.parseInt(id_konf));
                cstmt.setString(3, enazwa.getText());
                cstmt.setDate(4, Date.valueOf(edata.getValue()));
                cstmt.setInt(5, Integer.parseInt(eliczba_osob.getText()));
                if(ehala.getValue().contains(","))
                    cstmt.setInt(6, Integer.parseInt(ehala.getValue().substring(0,ehala.getValue().indexOf(","))));
                else
                    cstmt.setInt(6, Integer.parseInt(ehala.getValue()));
                cstmt.execute();
                cstmt.close();
                String str = "DELETE FROM hotel_nadzor_konferencji where id_konferencji=" + id_konf;
                PreparedStatement stmt = konferencje.dataBase.getCon().prepareStatement(str);
                stmt.executeQuery();
                stmt.close();
                for(String i : pracownicy){
                    str = "SELECT * FROM hotel_pracownicy WHERE nazwisko=\'" + i + "\'";
                    stmt = dataBase.getCon().prepareStatement(str);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    pesel = rs.getString("pesel");
                    cstmt = konferencje.dataBase.getCon().prepareCall("{call dodajNadzorKonferencji(?,?)}");
                    cstmt.setInt(1, Integer.parseInt(id_konf));
                    cstmt.setString(2, pesel);
                    cstmt.execute();
                    rs.close();
                }
                cstmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
        }else if(checkCorrectness()){
            String pesel = null;
            try {
                String str = "SELECT * FROM hotel_pracownicy WHERE nazwisko=\'" + epracownicy.getSelectionModel().getSelectedItem() + "\'";
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                pesel = rs.getString("pesel");
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
//                ex.printStackTrace();
            }
            String hala = ehala.getSelectionModel().getSelectedItem().toString();
            dataBase.addKonferencje(enazwa.getText(), Date.valueOf(edata.getValue()),
                    Integer.parseInt(eliczba_osob.getText()), pesel,
                    Integer.parseInt(hala.substring(0, hala.indexOf(","))),pracownicy);
            returnTo();
        }
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void initialize(){
        if(look){
            try {
                String str = "SELECT * FROM hotel_konferencje WHERE id_konferencji=" + id_konf;
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                enazwa.setText(rs.getString("nazwa"));
                enazwa.setEditable(false);
                edata.setValue(rs.getDate("data_konferencji").toLocalDate());
                edata.setDisable(true);
                eliczba_osob.setText(Integer.toString(rs.getInt("liczba_osob")));
                eliczba_osob.setEditable(false);
                ehala.setValue(Integer.toString(rs.getInt("hala_konferencyjna")));
                ehala.setDisable(true);
                getPracownicy();
                for (String i : pracownicy)
                    pracownicyScroll.getChildren().add(createPracownikButton(i));
                epracownicy.setDisable(true);
                saveButton.setVisible(false);
                rs.close();
                stmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
        }else {
            editButton.setVisible(false);
            delButton.setVisible(false);
        }
        ObservableList<String> observPracownicy = FXCollections.observableArrayList();
        observPracownicy.addAll(konferencje.dataBase.getSomePracownicy());
        epracownicy.setItems(observPracownicy);
        ObservableList<String> observHale = FXCollections.observableArrayList();
        observHale.addAll(konferencje.dataBase.getSomeHale());
        ehala.setItems(observHale);
    }

    @FXML
    private void edit(){
        if(!look)
            return;
        edit = true;
        enazwa.setEditable(true);
        edata.setDisable(false);
        eliczba_osob.setEditable(true);
        ehala.setDisable(false);
        epracownicy.setDisable(false);
        saveButton.setVisible(true);
    }
    @FXML
    private void delete(){
        try {
            String str = "DELETE FROM hotel_konferencje WHERE id_konferencji=" + id_konf;
            PreparedStatement stmt = konferencje.dataBase.getCon().prepareStatement(str);
            stmt.executeQuery();
            stmt.close();
            returnTo();
        }catch (Exception ex){
//            ex.printStackTrace();
        }
    }


    @FXML
    private void choosePracownik(){
        if(epracownicy.getValue() == null)
            return;
        String temp_nazwisko = epracownicy.getValue();
        try {
            String str = "SELECT * FROM hotel_pracownicy WHERE upper(nazwisko)=upper(\'" + temp_nazwisko + "\')";
            PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (!pracownicy.contains(rs.getString("nazwisko"))){
                pracownicy.add(rs.getString("nazwisko"));
                pracownicyScroll.getChildren().add(createPracownikButton(temp_nazwisko));
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex) {
//            ex.printStackTrace();
        }

    }

    private boolean checkCorrectness(){
        boolean correct = true;
        boolean dataCorrect = true;
        boolean nazwaCorrect = true;
        if (enazwa.getText().isEmpty() || enazwa.getText().length() > 100){
            correct = false;
            nazwaCorrect = false;
            enazwa.getStyleClass().add("wrong");
            enazwa.setTooltip(new Tooltip("Nazwa powininna się składać z 0-100 znaków"));
        }else{
            while (enazwa.getStyleClass().remove("wrong"));
            enazwa.setTooltip(null);
        }
        if (edata.getValue() == null || edata.getValue().toString().matches("^((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})$")){
            correct = false;
            dataCorrect = false;
            edata.getStyleClass().add("wrongDate");
            while(edata.getStyleClass().remove("addDate"));
            edata.setTooltip(new Tooltip("Niepoprawna data"));
        }else{
            while (edata.getStyleClass().remove("wrongDate")) ;
            edata.getStyleClass().add("addDate");
            edata.setTooltip(null);
        }
        if (!eliczba_osob.getText().matches("^[1-9][0-9]{0,4}$")){
            correct = false;
            eliczba_osob.getStyleClass().add("wrong");
            eliczba_osob.setTooltip(new Tooltip("Niepoprawna liczba osób"));
        }else{
            while (eliczba_osob.getStyleClass().remove("wrong"));
            eliczba_osob.setTooltip(null);
        }
        if (ehala.getValue() == null){
            correct = false;
            ehala.getStyleClass().add("wrong");
            ehala.setTooltip(new Tooltip("Potrzeba wybrać halę"));
        }else if(!eliczba_osob.getText().isEmpty()){
            int l = 0;
            try{
                PreparedStatement stmt = konferencje.dataBase.getCon().prepareStatement("SELECT liczba_miejsc from hotel_hale_konferencyjne where numer_hali = ?");
                if(ehala.getValue().contains(","))
                    stmt.setInt(1, Integer.parseInt(ehala.getValue().substring(0,ehala.getValue().indexOf(","))));
                else
                    stmt.setInt(1, Integer.parseInt(ehala.getValue()));
                ResultSet rs = stmt.executeQuery();
                rs.next();
                l = rs.getInt("liczba_miejsc");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if(l < Integer.parseInt(eliczba_osob.getText())){
                correct = false;
                dataCorrect = false;
                ehala.getStyleClass().add("wrong");
                ehala.setTooltip(new Tooltip("Hala jest za mała"));
            }else{
                while (ehala.getStyleClass().remove("wrong"));
                ehala.setTooltip(null);
            }
        }
        if(dataCorrect){
            try {
                PreparedStatement stmt = konferencje.dataBase.getCon().prepareStatement("SELECT id_konferencji, data_konferencji from hotel_konferencje where hala_konferencyjna = ?");
                if(ehala.getValue().contains(","))
                    stmt.setInt(1, Integer.parseInt(ehala.getValue().substring(0,ehala.getValue().indexOf(","))));
                else
                    stmt.setInt(1, Integer.parseInt(ehala.getValue()));
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while (rs.next()){
                    if(!rs.getString("id_konferencji").equals(id_konf) && rs.getDate("data_konferencji").compareTo(Date.valueOf(edata.getValue())) == 0){
                        nope = true;
                        break;
                    }
                }
                if(nope){
                    correct = false;
                    ehala.getStyleClass().add("wrong");
                    ehala.setTooltip(new Tooltip("Hala jest niedostępna tego dnia"));
                }else{
                    while (ehala.getStyleClass().remove("wrong"));
                    ehala.setTooltip(null);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        if (pracownicyScroll.getChildren().isEmpty()){
            correct = false;
            epracownicy.getStyleClass().add("wrong");
            epracownicy.setTooltip(new Tooltip("Przynajmniej jeden pracownik musi nadzorować konferencję"));
        }else{
            while (epracownicy.getStyleClass().remove("wrong"));
            epracownicy.setTooltip(null);
        }
        if(nazwaCorrect){
            try{
                PreparedStatement stmt = konferencje.dataBase.getCon().prepareStatement("SELECT nazwa, id_konferencji from hotel_konferencje");
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while (rs.next()){
                    if(rs.getString("nazwa").toUpperCase().equals(enazwa.getText().toUpperCase()) && !rs.getString("id_konferencji").equals(id_konf)){
                        nope = true;
                        break;
                    }
                }
                if(nope){
                    correct = false;
                    enazwa.getStyleClass().add("wrong");
                    enazwa.setTooltip(new Tooltip("Konferencja o takiej nazwie już istnieje"));
                }else{
                    while (enazwa.getStyleClass().remove("wrong"));
                    enazwa.setTooltip(null);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return correct;
    }

    private Button createPracownikButton(String nazwa) {
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e -> deleteButton(button));
        return button;
    }

    private void deleteButton(Button toDelete) {
        if(!edit)
            return;
        pracownicyScroll.getChildren().remove(toDelete);
        pracownicy.clear();
        try {
            for (Node i : pracownicyScroll.getChildren()) {
                String temp_nazwisko = ((Button) i).getText();
                String str = "SELECT * FROM hotel_pracownicy WHERE upper(nazwisko)=upper(\'" + temp_nazwisko + "\')";
                PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                pracownicy.add(rs.getString("nazwisko"));
                rs.close();
                stmt.close();
            }
        }catch (Exception ex){
//            ex.printStackTrace();
        }
    }

    private void getPracownicy(){
        try {
            String str = "select n.*, p.nazwisko from hotel_pracownicy p inner join hotel_nadzor_konferencji n on (p.pesel=n.pracownik) where n.id_konferencji=" + id_konf;
            PreparedStatement stmt = dataBase.getCon().prepareStatement(str);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                pracownicy.add(rs.getString("nazwisko"));
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex) {
//            ex.printStackTrace();
        }
    }

}
