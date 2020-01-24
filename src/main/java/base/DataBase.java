package main.java.base;

import main.java.controller.Controller;

import java.sql.*;
import java.util.ArrayList;
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

    public void deleteKonferencje(String id_konf){
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM hotel_konferencje WHERE ID_KONFERENCJI = "+id_konf);
            stmt.executeQuery();
            stmt.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getWholeKonferencja(String id_konf){
        ArrayList<String> result = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_konferencje WHERE ID_KONFERENCJI = "+id_konf);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            result.add(rs.getString("nazwa"));
            result.add(rs.getDate("data_konferencji").toString());
            result.add(rs.getString("liczba_osob"));
            result.add(rs.getString("hala_konferencyjna"));
            stmt.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getSomePracownicy(){
        ArrayList<String> pracownicy = new ArrayList<>();
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_pracownicy");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                pracownicy.add(rs.getString("nazwisko"));
            }
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return pracownicy;
    }

    public Connection getCon(){ return con; }
}
