package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddPokoj {
    private Controller controller;
    private Pokoje pokoje;
    private boolean look;
    private boolean edit;
    private String numerString;
    @FXML
    private Button saveButton, editButton;
    @FXML
    private TextField numer, cena, liczbaL;
    @FXML
    private CheckBox telewizorCheck, lazienkaCheck;

    public AddPokoj(Controller controller, Pokoje pokoje){
        this.controller = controller;
        this.pokoje = pokoje;
        this.look = false;
        this.edit = true;
    }

    public AddPokoj(Controller controller, Pokoje pokoje, String numerString){
        this.controller = controller;
        this.pokoje = pokoje;
        this.numerString = numerString;
        this.look = true;
        this.edit = false;
    }

    @FXML
    private void initialize(){
        if(look){
            try {
                saveButton.setVisible(false);
                String str = "SELECT * FROM hotel_pokoje WHERE numer=" + numerString;
                PreparedStatement stmt = pokoje.dataBase.getCon().prepareStatement(str);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                saveButton.setVisible(false);
                numer.setText(rs.getString("numer"));
                numer.setEditable(false);
                cena.setText(rs.getString("cena_za_dobe"));
                cena.setEditable(false);
                liczbaL.setText(rs.getString("liczba_lozek"));
                liczbaL.setEditable(false);
                telewizorCheck.setSelected(rs.getInt("czy_telewizor") == 1);
                telewizorCheck.setOnAction(e -> {
                    if (!edit) telewizorCheck.setSelected(!telewizorCheck.isSelected());
                });
                lazienkaCheck.setSelected(rs.getInt("czy_lazienka") == 1);
                lazienkaCheck.setOnAction(e -> {
                    if (!edit) lazienkaCheck.setSelected(!lazienkaCheck.isSelected());
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            editButton.setVisible(false);
        }
    }
    @FXML
    private void addPokoj(){
        if(look){
            edit = false;
            saveButton.setVisible(false);
            numer.setEditable(false);
            cena.setEditable(false);
            liczbaL.setEditable(false);
        }else if(checkCorrectness()) {
            pokoje.dataBase.addPokoj(Integer.parseInt(numer.getText()), Float.parseFloat(cena.getText()), Integer.parseInt(liczbaL.getText()),
                    ((telewizorCheck.isSelected()) ? 1 : 0),((lazienkaCheck.isSelected()) ? 1 : 0));
            returnTo();
        }else{
        }
    }
    @FXML
    private void returnTo(){
        controller.changeScene("main_view.fxml", pokoje);
    }
    @FXML
    private void edit(){
        if(!look)
            return;
        saveButton.setVisible(true);
        edit = true;
        saveButton.setVisible(true);
        numer.setEditable(true);
        cena.setEditable(true);
        liczbaL.setEditable(true);
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        if (!numer.getText().matches("^[1-9][0-9]{0,2}$")){
            correct = false;
            numer.getStyleClass().add("wrong");
        }else{
            while (numer.getStyleClass().remove("wrong"));
        }
        if (!cena.getText().matches("[0-9]{1,7}(\\.[0-9]{0,2}){0,1}")){
            correct = false;
            cena.getStyleClass().add("wrong");
        }else{
            while (cena.getStyleClass().remove("wrong"));
        }
        if (!liczbaL.getText().matches("^[1-9][0-9]?$")){
            correct = false;
            liczbaL.getStyleClass().add("wrong");
        }else{
            while (liczbaL.getStyleClass().remove("wrong"));
        }
        return correct;
    }

}
