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
    private TextField enazwa, stanM, cena, nip;

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
            enazwa.setText(""); // TODO
            enazwa.setEditable(false);
            stanM.setText("");  // TODO
            stanM.setEditable(false);
            cena.setText("");   // TODO
            cena.setEditable(false);
            nip.setText("");    // TODO
            nip.setEditable(false);
        }else{
            editButton.setVisible(false);
        }
    }
    @FXML
    private void addDostawce(){
        if(look) {
            saveButton.setVisible(false);
            enazwa.setEditable(false);
            stanM.setEditable(false);
            cena.setEditable(false);
            nip.setEditable(false);
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
        enazwa.setEditable(true);
        stanM.setEditable(true);
        cena.setEditable(true);
        nip.setEditable(true);
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }
}
