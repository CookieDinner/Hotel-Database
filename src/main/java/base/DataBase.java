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

    public void addPracownika(String pes, String im, String naz, String et, float pl,
                              Date datu, Date datz, String um, String ad, float prem){
        try {
            cstmt = con.prepareCall("{call dodajPracownika(?,?,?,?,?,?,?,?,?,?,0)}");
            cstmt.setString(1, pes);
            cstmt.setString(2, im);
            cstmt.setString(3, naz);
            cstmt.setString(4, et);
            cstmt.setFloat(5, pl);
            cstmt.setDate(6, datu);
            cstmt.setDate(7, datz);
            cstmt.setString(8, um);
            cstmt.setString(9, ad);
            if(prem > 0)
                cstmt.setFloat(10, prem);
            else
                cstmt.setNull(10, Types.FLOAT);
            cstmt.execute();
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    };
    public void addDanie(String nazwa, Float cena, ArrayList<String> skladniki){
        try {
            cstmt = con.prepareCall("{? = call dodajDanie(null,?,?,null,0)}");
            cstmt.setString(2, nazwa);
            cstmt.setFloat(3, cena);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            int danId = cstmt.getInt(1);
            cstmt.close();
            for(String i : skladniki){
                cstmt = con.prepareCall("{call dodajSklad(?,?)}");
                cstmt.setInt(1, danId);
                cstmt.setString(2, i);
                cstmt.execute();
            }
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addHale(int numer, int liczba){
        try {
            cstmt = con.prepareCall("{call dodajHale(?,?,0)}");
            cstmt.setInt(1, numer);
            cstmt.setInt(2, liczba);
            cstmt.execute();
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addPokoj(int numer, float cena, int lLoz, int tele, int laz){
        try {
            cstmt = con.prepareCall("{call dodajPokoj(?,?,?,?,?,0)}");
            cstmt.setInt(1, numer);
            cstmt.setFloat(2, cena);
            cstmt.setInt(3, lLoz);
            cstmt.setInt(4, tele);
            cstmt.setInt(5, laz);
            cstmt.execute();
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addDostawce(String nip, String nazwa, String adres, String numer){
        try {
            cstmt = con.prepareCall("{call dodajDostawce(?,?,?,?,0)}");
            cstmt.setString(1, nip);
            cstmt.setString(2, nazwa);
            cstmt.setString(3, adres);
            cstmt.setString(4, numer);
            cstmt.execute();
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addKlienci(String imie, String nazwisko, String pesel, String numerTel, String adresZa){
        try {
            cstmt = con.prepareCall("{call dodajKlienta(?,?,?,?,?,0)}");
            cstmt.setString(1, pesel);
            cstmt.setString(2, imie);
            cstmt.setString(3, nazwisko);
            cstmt.setString(4, numerTel);
            cstmt.setString(5, adresZa);
            cstmt.execute();
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addRezerwacje(Date data_zam, Date term_wym, float rabat, String pracownik, String klient, ArrayList<String> pokoje){
        try {
            cstmt = con.prepareCall("{? = call dodajRezerwacje(null,?,?,?,?,?,0)}");
            cstmt.setDate(2, data_zam);
            cstmt.setDate(3, term_wym);
            if(rabat == 0)
                cstmt.setNull(4,Types.INTEGER);
            else
                cstmt.setFloat(4, rabat);
            cstmt.setString(5, pracownik);
            cstmt.setString(6, klient);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            int rezId = cstmt.getInt(1);
            cstmt.close();
            for(String i : pokoje){
                cstmt = con.prepareCall("{call dodajRezerwacjePokoju(?,?)}");
                cstmt.setInt(1, rezId);
                cstmt.setString(2, i);
                cstmt.execute();
            }
            cstmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
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

    public void addZamowienie(Date data, String pesel, ArrayList<String> dania){
        try {
            cstmt = con.prepareCall("{? = call dodajZamowienie(null,?,?,0)}");
            cstmt.setDate(2, data);
            cstmt.setString(3, pesel);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            int zamId = cstmt.getInt(1);
            cstmt.close();
            for(String i : dania) {
                cstmt = con.prepareCall("{call dodajZamowienieDania(?,?)}");
                cstmt.setInt(1, zamId);
                cstmt.setInt(2, Integer.parseInt(i));
                cstmt.execute();
            }
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

    public ArrayList<String> getSomeSkladniki(){
        ArrayList<String> skladniki = new ArrayList<>();
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM hotel_skladniki order by nazwa asc");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                skladniki.add(rs.getString("nazwa"));
            }
            stmt.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return skladniki;
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
