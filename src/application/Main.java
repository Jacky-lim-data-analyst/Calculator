package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }    

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homeUI.fxml"));
            Scene scene = new Scene(root);

            try {
                Image icon = new Image(getClass().getResourceAsStream("/application/resources/calculator.png"));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("Could not load icon: " + e.getMessage());
            }

            stage.setTitle("Calculator");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
