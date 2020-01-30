package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AddPokoj {
    private Controller controller;
    private Pokoje pokoje;
    private boolean look;
    private boolean edit;
    private String numerString;
    @FXML
    private Button saveButton, editButton, delButton;
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
                rs.close();
                stmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
        }else{
            editButton.setVisible(false);
            delButton.setVisible(false);
        }
    }
    @FXML
    private void addPokoj(){
        if(look && checkCorrectness()){
            edit = false;
            saveButton.setVisible(false);
            numer.setEditable(false);
            cena.setEditable(false);
            liczbaL.setEditable(false);
            try{
                CallableStatement cstmt = pokoje.dataBase.getCon().prepareCall("{call dodajPokoj(?,?,?,?,?,1)}");
                cstmt.setInt(1, Integer.parseInt(numer.getText()));
                cstmt.setFloat(2, Float.parseFloat(cena.getText()));
                cstmt.setInt(3, Integer.parseInt(liczbaL.getText()));
                cstmt.setInt(4, ((telewizorCheck.isSelected()) ? 1 : 0));
                cstmt.setInt(5, ((lazienkaCheck.isSelected()) ? 1 : 0));
                cstmt.execute();
                cstmt.close();
            }catch (Exception ex){
//                ex.printStackTrace();
            }
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
        numer.setEditable(false);
        cena.setEditable(true);
        liczbaL.setEditable(true);
    }

    @FXML
    private void delete(){
        try {
            PreparedStatement stmt = pokoje.dataBase.getCon().prepareStatement("SELECT rezerwacja FROM HOTEL_REZERWACJA_POKOJU WHERE pokoj = ?");
            stmt.setString(1, numer.getText());
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> rezerwacje = new ArrayList<>();
            while (rs.next()){
                rezerwacje.add(rs.getString("rezerwacja"));
            }
            rs.close();
            Alert alert = new Alert(Alert.AlertType.NONE, "Istnieją rezerwacje (" + rezerwacje.size() + "), one również zostaną usunięte.\n\nCzy nadal chcesz usunąć pokój?", ButtonType.YES, ButtonType.NO);
            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            scrollPane.setContent(vBox);
            scrollPane.setHmin(40);
            alert.getDialogPane().setExpandableContent(scrollPane);

            if(!rezerwacje.isEmpty()){
                for (String n : rezerwacje){
                    vBox.getChildren().add(new Label("Rezerwacja numer: "+n));
                }
                alert.showAndWait();
            }

            if(rezerwacje.isEmpty() || alert.getResult() == ButtonType.YES) {
                String str = "DELETE FROM hotel_pokoje WHERE numer=" + numerString;
                stmt = pokoje.dataBase.getCon().prepareStatement(str);
                stmt.executeQuery();
                stmt.close();
                returnTo();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkCorrectness(){
        boolean correct = true;
        boolean numerCorrect = true;
        if (!numer.getText().matches("^[1-9][0-9]{0,2}$")){
            correct = false;
            numerCorrect = false;
            numer.getStyleClass().add("wrong");
            numer.setTooltip(new Tooltip("Niepoprawny numer pokoju"));
        }else{
            while (numer.getStyleClass().remove("wrong"));
            numer.setTooltip(null);
        }
        if (!cena.getText().matches("^[0-9]{1,7}(\\.[0-9]{0,2}){0,1}$")){
            correct = false;
            cena.getStyleClass().add("wrong");
            cena.setTooltip(new Tooltip("Niepoprawna cena"));
        }else{
            while (cena.getStyleClass().remove("wrong"));
            cena.setTooltip(null);
        }
        if (!liczbaL.getText().matches("^[1-9][0-9]?$")){
            correct = false;
            liczbaL.getStyleClass().add("wrong");
            liczbaL.setTooltip(new Tooltip("Niepoprawna liczba łóżek"));
        }else{
            while (liczbaL.getStyleClass().remove("wrong"));
            liczbaL.setTooltip(null);
        }
        if (numerCorrect && !look)
            try {
                PreparedStatement stmt = pokoje.dataBase.getCon().prepareStatement("SELECT numer from hotel_pokoje");
                ResultSet rs = stmt.executeQuery();
                boolean nope = false;
                while (rs.next()) {
                    if (numer.getText().equals(rs.getString("numer"))) {
                        nope = true;
                        break;
                    }
                }
                if (nope){
                    correct = false;
                    numer.getStyleClass().add("wrong");
                    numer.setTooltip(new Tooltip("Istnieje już taki pokój"));
                }else{
                    while(numer.getStyleClass().remove("wrong"));
                    numer.setTooltip(null);
                }
                rs.close();
                stmt.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        return correct;
    }

}
