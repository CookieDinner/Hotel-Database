package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/sample.fxml"));
        primaryStage.setTitle("Hotel Database");
        primaryStage.setScene(new Scene(root, 1250, 750));
        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Ending app");   // TODO: dissconnect from database
    }


    public static void main(String[] args) {
        launch(args);
    }
}