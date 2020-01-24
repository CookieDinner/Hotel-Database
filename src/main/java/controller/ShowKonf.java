package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class ShowKonf {
    @FXML
    private Label titleLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label personCount;
    @FXML
    private Label whichHala;
    private Konferencje konferencje;
    private Controller controller;
    private String id_konf;

    public ShowKonf(String id_konf, Konferencje konferencje, Controller controller){
        this.id_konf = id_konf;
        this.konferencje = konferencje;
        this.controller = controller;

    }

    @FXML
    public void initialize(){
        ArrayList<String> arr = konferencje.dataBase.getWholeKonferencja(id_konf);
        titleLabel.setText(arr.get(0));
        dateLabel.setText(arr.get(1));
        personCount.setText(arr.get(2));
        whichHala.setText(arr.get(3));
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void delete(){
        konferencje.dataBase.deleteKonferencje(id_konf);
        returnTo();
    }
}
