package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddHala {
    private Controller controller;
    private Hale hale;
    private String numerHali;
    private boolean look;
    @FXML
    private TextField numer, lMiejsc;
    @FXML
    private Button editButton, saveButton;

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
            saveButton.setVisible(false);
            numer.setText("");  // TODO
            numer.setEditable(false);
            lMiejsc.setText("");    // TODO
            lMiejsc.setEditable(false);
        }else{
            editButton.setVisible(false);
        }

    }
    @FXML
    private void addHale(){
        if(look){
            saveButton.setVisible(false);
            numer.setEditable(false);
            lMiejsc.setEditable(false);
        }else if(checkCorrectness()){
        }else{
            // TODO
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
        numer.setEditable(true);
        lMiejsc.setEditable(true);
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
