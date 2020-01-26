package main.java.base;

import main.java.controller.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    private Connection con = null;
    private CallableStatement cstmt = null;

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

    public void addKonferencje(String nazwa, Date data, int lOsob, String pesel, int idHali, ArrayList<String> pracownicy){
        try {
            cstmt = con.prepareCall("{? = call dodajKonferencje(null,?,?,?,?,0)}");
            cstmt.setString(2, nazwa);
            cstmt.setDate(3, data);
            cstmt.setInt(4, lOsob);
            cstmt.setInt(5,idHali);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            int konfId = cstmt.getInt(1);
            cstmt.close();
            for(String i : pracownicy){
                cstmt = con.prepareCall("{call dodajNadzorKonferencji(?,?)}");
                cstmt.setInt(1, konfId);
                cstmt.setString(2, i);
                cstmt.execute();
            }
            cstmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void addZamowienie(Date data, String pesel, int idDania){
        try {
            cstmt = con.prepareCall("{? = call dodajZamowienie(null,?,?,0)}");
            cstmt.setDate(2, data);
            cstmt.setString(3, pesel);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            int zamId = cstmt.getInt(1);
            cstmt.close();

            cstmt = con.prepareCall("{call dodajZamowienieDania(?,?)}");
            cstmt.setInt(1, zamId);
            cstmt.setInt(2, idDania);
            cstmt.execute();
            cstmt.close();
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
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_pracownicy order by nazwisko asc");
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

    public ArrayList<String> getSomeDania(){
        ArrayList<String> dania = new ArrayList<>();
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_dania order by nazwa asc");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dania.add(rs.getString("nazwa"));
            }
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return dania;
    }

    public ArrayList<String> getSomeHale(){
        ArrayList<String> hale = new ArrayList<>();
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_hale_konferencyjne order by numer_hali asc");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                hale.add(rs.getInt("numer_hali")+", Liczba miejsc: "+rs.getInt("liczba_miejsc"));
            }
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return hale;
    }

    public Connection getCon(){ return con; }
}
