package main.java.controller;

import javafx.fxml.FXML;

public class AddDostawy {
    private Controller controller;
    private Dostawcy dostawcy;

    public AddDostawy(Controller controller, Dostawcy dostawcy){
        this.controller = controller;
        this.dostawcy = dostawcy;
    }
    @FXML
    private void initialize(){

    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", dostawcy);
    }
    @FXML
    private void addDostawy(){

    }
}
