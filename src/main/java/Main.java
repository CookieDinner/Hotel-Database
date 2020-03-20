package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/sample.fxml"));
        primaryStage.setTitle("Hotel Database");
        primaryStage.setScene(new Scene(root, 1300, 750));
        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Ending app");
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static LocalDate dateCreate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}