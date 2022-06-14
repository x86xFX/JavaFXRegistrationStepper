package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Registration {
    public void openRegistration() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/interface/RegistrationInterface.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registration");
            stage.getIcons().add(new Image("/resources/icons/jManagerTray.png"));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
