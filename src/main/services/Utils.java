package services;

import javafx.scene.control.Alert;
import org.postgresql.copy.CopyIn;
import utils.DBConnect;
import utils.DeviceIdentifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isValidIndianNumber(String number) {
        if (number == null) return false;
        return number.matches("^[6-9]\\d{9}$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static int getUserId() throws SQLException {
        String query = "SELECT id FROM users WHERE unique_id = ?";
        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, DeviceIdentifier.getDeviceUniqueId());
            ResultSet set = statement.executeQuery();

            if(set.next())
                return set.getInt(1);
        }
        return -1;
    }

    public static int getCompanyId(String name) throws SQLException{
        String query = "SELECT company_id FROM companies WHERE company_name = ?";
        try(Connection connection = DBConnect.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();

            if(set.next())
                return set.getInt(1);
        }
        return -1;
    }

}
