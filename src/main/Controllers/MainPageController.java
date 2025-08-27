package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class MainPageController {

    @FXML AnchorPane contentPane;

    @FXML
    public void initialize(){

    }

    private void loadContent(String fxmlPath) {
        try {
            Parent newContent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newContent);

            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);

        } catch (IOException e) {
            System.out.println("There was a problem in loading the content for the main page");
        }
    }

    public void callLoadContent(String path){
        loadContent(path);
    }

}
