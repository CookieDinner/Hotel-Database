package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddDostawce {
    private Controller controller;
    private Dostawcy dostawcy;
    private String id;
    private boolean look;
    @FXML
    private Button editButton, saveButton;
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
            saveButton.setVisible(false);
            nip.setText(""); // TODO
            nip.setEditable(false);
            enazwa.setText(""); // TODO
            enazwa.setEditable(false);
            adres.setText(""); // TODO
            adres.setEditable(false);
            numer_telefonu.setText(""); // TODO
            numer_telefonu.setEditable(false);
        }else{
            editButton.setVisible(false);
        }
    }
    @FXML
    private void addDostawce(){
        if(look) {
            saveButton.setVisible(false);
            nip.setEditable(false);
            enazwa.setEditable(false);
            adres.setEditable(false);
            numer_telefonu.setEditable(false);
        }else{
            // TODO
        }
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        saveButton.setVisible(true);
        saveButton.setVisible(true);
        nip.setEditable(true);
        enazwa.setEditable(true);
        adres.setEditable(true);
        numer_telefonu.setEditable(true);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }
}
