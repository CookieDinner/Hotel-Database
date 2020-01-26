package main.java.controller;

import javafx.fxml.FXML;

public class AddDostawce {
    private Controller controller;
    private Dostawcy dostawcy;

    public AddDostawce(Controller controller, Dostawcy dostawcy){
        this.controller = controller;
        this.dostawcy = dostawcy;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void addDostawce(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }
}
