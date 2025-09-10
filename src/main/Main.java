
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.Utils;
import utils.DBConnect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    public static int userId;

    @Override
    public void start(Stage primaryStage) throws Exception {

        if(!utils.DeviceIdentifier.doesExist()){
            utils.DeviceIdentifier.registerDevice();
        }
        setUserId();
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/logo.png"))));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/MainPage.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Invoice Generator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    private static void setUserId() throws Exception{
        String id = utils.DeviceIdentifier.getDeviceUniqueId();
        String query = "SELECT id FROM users WHERE unique_id = ?";
        Connection connection = utils.DBConnect.getConnection();
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
