import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    public static int userId;

    @Override
    public void start(Stage primaryStage) throws Exception {

        if(!DeviceIdentifier.doesExist()){
            DeviceIdentifier.registerDevice();
        }
        setUserId();
        System.out.println(userId);

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void setUserId() throws Exception{
        String id = DeviceIdentifier.getDeviceUniqueId();
        String query = "SELECT id FROM users WHERE unique_id = ?";
        Connection connection = DBConnect.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();
            if(set.next())
                userId = set.getInt(1);
        }catch (SQLException e){
            System.out.println("There was a problem while setting the user id");
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
