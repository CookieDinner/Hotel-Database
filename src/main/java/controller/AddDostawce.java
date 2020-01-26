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
        }else if(checkCorrectness()){
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

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!nip.getText().matches("^[0-9]{10}$")){
            correct = false;
            nip.getStyleClass().add("wrong");
        }else{
            while(nip.getStyleClass().remove("wrong"));
        }
        if (enazwa.getText().isEmpty() || enazwa.getText().length() > 30){
            correct = false;
            enazwa.getStyleClass().add("wrong");
        }else{
            while (enazwa.getStyleClass().remove("wrong"));
        }
        if (adres.getText().isEmpty() || adres.getText().length() > 50){
            correct = false;
            adres.getStyleClass().add("wrong");
        }else{
            while (adres.getStyleClass().remove("wrong"));
        }
        if (!numer_telefonu.getText().matches("[0-9]{9}")){
            correct = false;
            numer_telefonu.getStyleClass().add("wrong");
        }else{
            while (numer_telefonu.getStyleClass().remove("wrong"));
        }
        return correct;
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }
}
