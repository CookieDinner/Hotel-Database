package main.java.base;

import main.java.controller.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    private Connection con = null;

    public void connect(){
        Properties connectionProps = new Properties();
        connectionProps.put("user", "inf136826");
        connectionProps.put("password", "blieberry420");
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@admlab2.cs.put.poznan.pl:1521/"+
                    "dblab02_students.cs.put.poznan.pl", connectionProps);
            System.out.println("Połączono z bazą danych");
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                    "nie udało się połączyć z bazą danych", ex);
            System.exit(-1);
        }
    }

    public Connection getCon(){ return con; }
}
