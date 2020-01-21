package main.java.base;

import main.java.controller.Controller;

import java.sql.*;
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

    public void addKonferencje(String nazwa, Date data, int lOsob){
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO hotel_konferencje (nazwa, data_konferencji, liczba_osob, hala_konferencyjna) VALUES (?,?,?,1)");
            stmt.setString(1, nazwa);
            stmt.setDate(2, data);
            stmt.setInt(3, lOsob);
            stmt.executeUpdate();
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public Connection getCon(){ return con; }
}
