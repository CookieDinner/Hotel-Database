package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ShowRezerwacje {
    private Controller controller;
    private Rezerwacje rezerwacje;
    @FXML
    private Label imieLabel, nazwiskoLabel, dataZLabel, dataWLabel;
    @FXML
    private Button pokojButton;

    public ShowRezerwacje(Controller controller, Rezerwacje rezerwacje){
        this.controller = controller;
        this.rezerwacje = rezerwacje;
    }

    @FXML
    private void initialize(){
        imieLabel.setText("Krzystof");
        nazwiskoLabel.setText("Sychla");
        dataZLabel.setText("12.01.2010");
        dataWLabel.setText("18.10.2019");
        pokojButton.setGraphic(new Label("1"));
        pokojButton.setOnAction(e->gotoPokoj("1"));
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    @FXML
    private void delete(){
//        TODO: delete this
        controller.changeScene("main_view.fxml", rezerwacje);
    }
    private void gotoPokoj(String id){

    }
}
