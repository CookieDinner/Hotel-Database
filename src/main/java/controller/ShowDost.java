package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class ShowDost {
    @FXML
    private Label titleLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label personCount;
    @FXML
    private Label whichHala;
    private Dostawcy dostawcy;
    private Controller controller;
    private String nip;

    public ShowDost(String nip, Dostawcy dostawcy, Controller controller){
        this.nip = nip;
        this.dostawcy = dostawcy;
        this.controller = controller;

    }

    @FXML
    public void initialize(){
        ArrayList<String> arr = dostawcy.dataBase.getWholeKonferencja(nip);
        titleLabel.setText(arr.get(0));
        dateLabel.setText(arr.get(1));
        personCount.setText(arr.get(2));
        whichHala.setText(arr.get(3));
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }

    @FXML
    private void delete(){
        dostawcy.dataBase.deleteKonferencje(nip);
        returnTo();
    }
}
