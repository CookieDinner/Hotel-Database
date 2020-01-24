package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowKonf {
    @FXML
    private Label titleLabel;
    private String title;
    @FXML
    private Label dateLabel;
    private String  date;
    @FXML
    private Label personCount;
    @FXML
    private Label whichHala;
    private Konferencje konferencje;
    private Controller controller;
    private String liczbaOsob, hala;

    public ShowKonf(String title, String date, String liczbaOsob, String hala, Konferencje konferencje, Controller controller){
        this.title = title;
        this.date = date;
        this.konferencje = konferencje;
        this.controller = controller;
        this.liczbaOsob = liczbaOsob;
        this.hala = hala;
    }

    @FXML
    public void initialize(){
        titleLabel.setText(title);
        dateLabel.setText(date);
        personCount.setText(liczbaOsob);
        whichHala.setText(hala);
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }

    @FXML
    private void delete(){
        System.out.println("Delete this!");  // TODO: Delete Event
        returnTo();
    }
}
