package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import main.java.Main;

public class AddZamowienia {
    private Controller controller;
    private Zamowienia zamowienia;
    private boolean look;
    private String id;
    @FXML
    private ComboBox<String> epracownicy;
    @FXML
    private DatePicker zdata;
    @FXML
    private TextField dania;
    @FXML
    private VBox daniaScroll;
    @FXML
    private Button saveButton, editButton;

    public AddZamowienia(Controller controller, Zamowienia zamowienia){
        this.controller = controller;
        this.zamowienia = zamowienia;
        this.look = false;
    }

    public AddZamowienia(Controller controller, Zamowienia zamowienia, String id){
        this.controller = controller;
        this.zamowienia = zamowienia;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if(look){
            epracownicy.setValue("");       // TODO
            epracownicy.setDisable(true);
            zdata.setValue(Main.dateCreate("2010-12-15"));  // TODO
            zdata.setDisable(true);
            dania.setEditable(false);
//            for(String i : allDania)  // TODO
//                daniaScroll.getChildren().add(createDanieButton(i));
            saveButton.setVisible(false);
        }else{
            editButton.setVisible(false);
        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", zamowienia);
    }
    @FXML
    private void addRezerwacje(){
        if(look){
            epracownicy.setDisable(true);
            zdata.setDisable(true);
            dania.setEditable(false);
            saveButton.setVisible(false);
        }else{
            // TODO
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        epracownicy.setDisable(false);
        zdata.setDisable(false);
        dania.setEditable(true);
        saveButton.setVisible(true);
    }

    private boolean isDanieCorrect(String danie){
        return true;
    }

    @FXML
    private void danieReady(KeyEvent ke){
        if(ke.getCode().equals(KeyCode.ENTER)){
            if(isDanieCorrect(dania.getText())) {
                daniaScroll.getChildren().add(createDanieButton(dania.getText()));
                dania.setText("");
            }
        }
    }

    private Button createDanieButton(String nazwa){
        Button button = new Button(nazwa);
        button.getStyleClass().add("danieButton");
        button.setOnAction(e->deleteButton(button));
        return button;
    }

    private void deleteButton(Button toDelete){
        daniaScroll.getChildren().remove(toDelete);
    }
}
