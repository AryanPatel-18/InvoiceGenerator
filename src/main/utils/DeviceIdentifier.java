package utils;

import javafx.scene.control.TextInputDialog;

import javax.swing.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Optional;

public class DeviceIdentifier {

    // Get MAC address of the first available network interface
    private static String getMACAddress() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN_MAC";
    }

    // Get the hostname of the machine
    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "UNKNOWN_HOST";
        }
    }

    // Generate SHA-256 hash from a string
    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // Main method to generate a unique device ID
    public static String getDeviceUniqueId() {
        String mac = getMACAddress();
        String host = getHostName();

        // Combine multiple identifiers into one string
        String combined = mac + "-" + host;

        // Return hash of combined string
        return sha256(combined);
    }

    public static boolean doesExist() throws Exception{
        Connection connection = DBConnect.getConnection();
        String uniqueId = getDeviceUniqueId();
        String query = "SELECT 1 FROM users WHERE unique_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, uniqueId);
            ResultSet set = statement.executeQuery();
            return set.next();
        }catch (SQLException e){
            System.out.println("There was a problem while checking the unique id");
        }

        return false;
    }

    public static void registerDevice() throws Exception{
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Prompt");
        dialog.setHeaderText("Enter your user name (Note : You wont be able to change it ) :");
        dialog.setContentText("Input:");
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            String name = result.get();
            Connection connection = DBConnect.getConnection();
            String query = "INSERT INTO users (user_name, unique_id ) VALUES ( ?,? )";
            try(PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(2, getDeviceUniqueId());
                statement.setString(1, name);
                int row = statement.executeUpdate();

                System.out.println(row>0?"Added the user":"Could not add the user");

            }catch (SQLException e){
                System.out.println("There was a problem while registering the user ");
            }
        }
    }
}
