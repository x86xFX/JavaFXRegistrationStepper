package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Splash extends Application {

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/interface/SplashInterface.fxml")));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/resources/icons/jManagerTray.png"));
            stage.show();

            stage.setOnCloseRequest(windowEvent -> Platform.exit());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}

/*
   * Rid The name org.a11y.Bus was not provided by any .service files
   *     run: sudo apt install at-spi2-core

 */