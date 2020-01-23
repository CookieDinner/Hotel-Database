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
    private Konferencje konferencje;
    private Controller controller;

    public ShowKonf(String title, String date, Konferencje konferencje, Controller controller){
        this.title = title;
        this.date = date;
        this.konferencje = konferencje;
        this.controller = controller;
    }

    @FXML
    public void initialize(){
        titleLabel.setText(title);
        System.out.println(title);
        dateLabel.setText(date);
        System.out.println(date);
    }

    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", konferencje);
    }
}
