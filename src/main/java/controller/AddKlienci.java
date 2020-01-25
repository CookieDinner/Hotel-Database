package main.java.controller;

import javafx.fxml.FXML;

public class AddKlienci {
    private Controller controller;
    private Object toReturnTo;

    public AddKlienci(Controller controller, Object toReturnTo){
        this.controller = controller;
        this.toReturnTo = toReturnTo;
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", toReturnTo);
    }
}
