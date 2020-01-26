package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.Main;


public class AddSkladnik {
    private Controller controller;
    private Magazyn magazyn;
    private boolean look, edit = false;
    String nazwa;
    @FXML
    private TextField enazwa, stanM, cena, dostawca;
    @FXML
    private Button saveButton, editButton;

    public AddSkladnik(Controller controller, Magazyn magazyn){
        this.controller = controller;
        this.magazyn = magazyn;
        this.look=false;
    }
    public AddSkladnik(Controller controller, Magazyn magazyn, String nazwa){
        this.controller = controller;
        this.magazyn = magazyn;
        this.nazwa=nazwa;
        this.look=true;
    }
    @FXML
    private void initialize(){
        if (look) {
            enazwa.setText("");     // TODO
            enazwa.setEditable(false);
            stanM.setText("");       // TODO
            stanM.setEditable(false);
            cena.setText("");     // TODO
            cena.setEditable(false);
            dostawca.setText("");       // TODO
            dostawca.setEditable(false);
            saveButton.setVisible(false);
        }else {
            editButton.setVisible(false);

        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", magazyn);
    }
    @FXML
    private void addSkladnik(){
        if(look){
            enazwa.setEditable(false);
            cena.setEditable(false);
            stanM.setEditable(false);
            dostawca.setEditable(false);
            edit = false;
            saveButton.setVisible(false);
        }else{
            // TODO
        }
    }
    @FXML
    private void edit() {
        if (!look)
            return;
        saveButton.setVisible(true);
        enazwa.setEditable(true);
        stanM.setEditable(true);
        cena.setEditable(true);
        dostawca.setEditable(true);
        edit = true;
    }
}
