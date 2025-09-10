package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Company;
import models.Invoice;
import services.Utils;
import utils.DBConnect;
import utils.DeviceIdentifier;
import utils.ExcelPlaceholderReplacer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class MainPageController {

    @FXML private AnchorPane contentPane;
    @FXML private ImageView imageContainer;
    @FXML private Button saveButton;
    @FXML private Button sendButton;

    // The controller instance of InvoiceForm loaded via FXMLLoader
    public static InvoiceFormController invoiceController;

    @FXML
    public void initialize() {
        loadLogo();
        loadInvoiceForm();
    }

    /**
     * Load logo image safely
     */
    private void loadLogo() {
        try {
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/logo.png")));
            imageContainer.setImage(logo);
        } catch (Exception e) {
            Utils.showError("Fetching Error","Problem while getting the company logo");
        }
    }

    /**
     * Load the InvoiceForm FXML and get its controller
     */
    private void loadInvoiceForm() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/FXML/InvoiceForm.fxml")));
            Parent invoiceRoot = loader.load();

            // Get the controller created by FXMLLoader
            invoiceController = loader.getController();

            // Add content to the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(invoiceRoot);

            // Anchor to edges
            AnchorPane.setTopAnchor(invoiceRoot, 0.0);
            AnchorPane.setBottomAnchor(invoiceRoot, 0.0);
            AnchorPane.setLeftAnchor(invoiceRoot, 0.0);
            AnchorPane.setRightAnchor(invoiceRoot, 0.0);

        } catch (IOException e) {
            Utils.showError("Fetching Error","Failed to load the Content page");
        }
    }

    /**
     * Save button handler
     */
    @FXML
    private void saveInvoice(ActionEvent event) throws Exception {
        if (invoiceController == null) return;
        if (checkFields()) return;
        Company obj = invoiceController.getCompanyFromInput();

        if(Utils.isValidEmail(obj.getEmail()) && Utils.isValidIndianNumber(obj.getPhoneNumber())){
            updateDatabase(obj);
            chooseSavePath((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow());
        } else{
            Utils.showError("Invalid Values","Please enter valid details");
        }
    }

    /**
     * Send button handler
     */
    @FXML
    private void sendInvoice(ActionEvent event) {
        if (invoiceController == null) return;

        if (checkFields()) return;


    }

    /**
     * Validate invoice fields using the controller's checkAllFields()
     */
    private boolean checkFields() {
        if (invoiceController.checkAllFields()) {
            Utils.showError("Empty Fields", "Please enter all the values");
            return true;
        }
        return false;
    }

    /**
     * Public method to reload invoice form dynamically if needed
     */
    public void reloadInvoiceForm() {
        loadInvoiceForm();
    }

    public void chooseSavePath(Stage stage) throws Exception {
        FileChooser fileChooser = new FileChooser();

        // Set a default file name (optional)
        fileChooser.setInitialFileName("new_invoice.xlsx");

        // Set extension filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show save dialog
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            ExcelPlaceholderReplacer.outputFilePath = selectedFile.getAbsolutePath();
            ExcelPlaceholderReplacer.replace(invoiceController.getInvoiceObject(), invoiceController.getCompanyFromInput());
        }
    }

    private void updateDatabase(Company obj) throws SQLException {
        String query = "SELECT 1 FROM companies WHERE company_name = ?";
        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, obj.getCompanyName());
            ResultSet set = statement.executeQuery();

            if(!set.next()){
                String insertQuery = "INSERT INTO companies (company_name, contact_name, phone_number, email, created_by, state, city) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement statement1 = connection.prepareStatement(insertQuery);
                statement1.setString(1, obj.getCompanyName());
                statement1.setString(2, obj.getClient());
                statement1.setString(3, obj.getPhoneNumber());
                statement1.setString(4, obj.getEmail());
                statement1.setInt(5, getUserId());
                statement1.setString(6, obj.getState());
                statement1.setString(7, obj.getCity());

                statement1.executeUpdate();
            }
        }
    }

    int getUserId() throws SQLException{
        String query = "SELECT id FROM users WHERE unique_id = ?";
        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, DeviceIdentifier.getDeviceUniqueId());
            ResultSet set = statement.executeQuery();

            if(set.next())
                return set.getInt(1);
        }
        return 0;
    }
}
