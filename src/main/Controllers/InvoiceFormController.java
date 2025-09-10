package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Company;
import models.Invoice;
import services.Utils;
import utils.DBConnect;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class InvoiceFormController {

    @FXML private ComboBox<String> companyField;
    @FXML private ComboBox<String> stateBox;
    @FXML private ComboBox<String> cityBox;
    @FXML private Button fetchButton;
    @FXML private Button updateButton;
    @FXML private DatePicker dateSelector;
    @FXML private TextField toInputField;
    @FXML private TextField projectInputField;
    @FXML private TextField companyNameField;
    @FXML private TextField emailField;
    @FXML private TextField numberField;

    // All the text fields
    @FXML private TextField description1;
    @FXML private TextField description2;
    @FXML private TextField description3;
    @FXML private TextField description4;
    @FXML private TextField description5;
    @FXML private TextField quantity1;
    @FXML private TextField quantity2;
    @FXML private TextField quantity3;
    @FXML private TextField quantity4;
    @FXML private TextField quantity5;
    @FXML private TextField price1;
    @FXML private TextField price2;
    @FXML private TextField price3;
    @FXML private TextField price4;
    @FXML private TextField price5;

    @FXML
    void initialize() throws Exception{
        setCompanyField();
        fetchButton.setDisable(true);
        setStates();

        companyField.valueProperty().addListener((obs, oldVal, newVal) -> {
            fetchButton.setDisable(newVal == null || newVal.trim().isEmpty());
        });

        stateBox.setValue("Gujarat");
        setCityValue("Gujarat");
        cityBox.setValue("Ahmedabad");

        stateBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                setCityValue(newVal);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        applyNumericOnly(quantity1);
        applyNumericOnly(quantity2);
        applyNumericOnly(quantity3);
        applyNumericOnly(quantity4);
        applyNumericOnly(quantity5);

        applyDecimalOnly(price1);
        applyDecimalOnly(price2);
        applyDecimalOnly(price3);
        applyDecimalOnly(price4);
        applyDecimalOnly(price5);
    }

    private void applyNumericOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null));
    }

    private void applyDecimalOnly(TextField field) {
        field.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null));
    }

    private void setCompanyField() throws Exception{
        String query = "SELECT company_name FROM companies";
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet set = statement.executeQuery()) {

            ArrayList<String> names = new ArrayList<>();
            while (set.next()) {
                names.add(set.getString("company_name"));
            }

            companyField.getItems().setAll(names);
        }
    }

    private void setStates() throws SQLException {
        String query = "SELECT name FROM states;";
        ArrayList<String> states = new ArrayList<>();

        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();

            while(set.next()){
                states.add(set.getString(1));
            }
            stateBox.getItems().addAll(states);
        }catch (Exception e){
            Utils.showError("Fetching Error","A problem occurred while getting the state values, please check database");
        }
    }

    private void setCityValue(String name) throws SQLException{
        String query = "SELECT name FROM cities WHERE state_id = (SELECT state_id FROM states WHERE name = ?)";
        try (Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,name);

            ResultSet set = statement.executeQuery();
            ArrayList<String> cities = new ArrayList<>();

            while(set.next()){
                cities.add(set.getString(1));
            }

            cityBox.getItems().clear();
            cityBox.getItems().addAll(cities);
        }catch (Exception e){
            Utils.showError("Fetching Error","A problem occurred while getting the city values, please check database");
        }
    }

    public void setValues(ActionEvent e) throws SQLException{
        String query = "SELECT * FROM companies WHERE company_name = ? LIMIT 1";

        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, companyField.getValue());
            ResultSet set = statement.executeQuery();

            if(set.next()){
                toInputField.setText(set.getString("contact_name"));
                companyNameField.setText(set.getString("company_name"));
                emailField.setText(set.getString("email"));
                numberField.setText(set.getString("phone_number"));
                stateBox.setValue(set.getString("state"));
                cityBox.setValue(set.getString("city"));
            }

        }catch (Exception exception) {
            Utils.showError("Fetching Error","A problem occurred while getting the company values, please check database");
        }

    }

    public boolean checkAllFields() {
        return isEmpty(toInputField) ||
                isEmpty(projectInputField) ||
                isEmpty(companyNameField) ||
                isEmpty(emailField) ||
                isEmpty(numberField) ||
                isEmpty(description1) ||
                isEmpty(quantity1) ||
                isEmpty(price1);
    }


    private boolean isEmpty(TextField field) {
        return field == null || field.getText() == null || field.getText().trim().isEmpty();
    }

    public Invoice getInvoiceObject() {
        Invoice invoice = new Invoice(
                projectInputField.getText() != null ? projectInputField.getText() : "",

                description1.getText() != null ? description1.getText() : "",
                description2.getText() != null ? description2.getText() : "",
                description3.getText() != null ? description3.getText() : "",
                description4.getText() != null ? description4.getText() : "",
                description5.getText() != null ? description5.getText() : "",

                parseDoubleSafe(price1.getText()),
                parseDoubleSafe(price2.getText()),
                parseDoubleSafe(price3.getText()),
                parseDoubleSafe(price4.getText()),
                parseDoubleSafe(price5.getText()),

                parseIntSafe(quantity1.getText()),
                parseIntSafe(quantity2.getText()),
                parseIntSafe(quantity3.getText()),
                parseIntSafe(quantity4.getText()),
                parseIntSafe(quantity5.getText())
        );

        return invoice;
    }

    private double parseDoubleSafe(String input) {
        try {
            return (input != null && !input.isBlank()) ? Double.parseDouble(input) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseIntSafe(String input) {
        try {
            return (input != null && !input.isBlank()) ? Integer.parseInt(input) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public Company getCompanyFromInput() {
        String client = (toInputField != null && !toInputField.getText().isEmpty()) ? toInputField.getText() : "";
        String companyName = (companyNameField != null && !companyNameField.getText().isEmpty()) ? companyNameField.getText() : "";
        String email = (emailField != null && !emailField.getText().isEmpty()) ? emailField.getText() : "";
        String phone = (numberField != null && !numberField.getText().isEmpty()) ? numberField.getText() : "";
        String state = (stateBox != null && stateBox.getValue() != null) ? stateBox.getValue() : "";
        String city = (cityBox != null && cityBox.getValue() != null) ? cityBox.getValue() : "";

        return new Company(client, companyName, email, phone, state, city);
    }

    public void clearValues() throws SQLException {
        description1.clear();
        description2.clear();
        description3.clear();
        description4.clear();
        description5.clear();

        price1.clear();
        price2.clear();
        price3.clear();
        price4.clear();
        price5.clear();

        quantity1.clear();
        quantity2.clear();
        quantity3.clear();
        quantity4.clear();
        quantity5.clear();

        toInputField.clear();
        numberField.clear();
        emailField.clear();
        projectInputField.clear();
        companyNameField.clear();
        companyField.setValue(null);

        stateBox.setValue("Gujarat");
        setCityValue("Gujarat");
        cityBox.setValue("Ahmedabad");
    }

}
