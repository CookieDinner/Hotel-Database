package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class AddDania {
    private Controller controller;
    private Dania dania;
    private String id;
    private boolean look;
    @FXML
    private Button saveButton, editButton;
    @FXML
    private TextField nazwa, cena, skladniki;
    @FXML
    private VBox skladnikiScroll;

    public AddDania(Controller controller, Dania dania){
        this.controller = controller;
        this.dania = dania;
        this.look = false;
    }

    public AddDania(Controller controller, Dania dania, String id) {
        this.controller = controller;
        this.dania = dania;
        this.id = id;
        this.look = true;
    }
    @FXML
    private void initialize(){
        if(look){
            saveButton.setVisible(false);
            nazwa.setText("Danie"); // TODO
            nazwa.setEditable(false);
            cena.setText("12");     // TODO
            cena.setEditable(false);
            skladniki.setEditable(false);
//            for(String i : Skladniki)  // TODO
//                skladnikiScroll.getChildren().add(createDanieButton(i));
        }else{
            editButton.setVisible(false);
        }

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dania);
    }
    @FXML
    private void addDania(){
        if(look){
            saveButton.setVisible(false);
            nazwa.setEditable(false);
            cena.setEditable(false);
            skladniki.setEditable(false);
        }else if(checkCorrectness()){
            // TODO
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        saveButton.setVisible(true);
        nazwa.setEditable(true);
        cena.setEditable(true);
        skladniki.setEditable(true);
    }

    private boolean isDanieCorrect(String danie){
        return true;
    }

    @FXML
    private void danieReady(KeyEvent ke){
        if(ke.getCode().equals(KeyCode.ENTER)){
            if(isDanieCorrect(skladniki.getText())) {
                skladnikiScroll.getChildren().add(createDanieButton(skladniki.getText()));
                skladniki.setText("");
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
        skladnikiScroll.getChildren().remove(toDelete);
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

}
