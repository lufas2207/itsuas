package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML
            AnchorPane root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

            // Ambil ukuran layar
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();

            // Buat scene dengan ukuran proporsional (misal 50% dari layar)
            Scene scene = new Scene(root, screenWidth * 0.5, screenHeight * 0.6);

            // Atur stage
            primaryStage.setTitle("Aplikasi Perpustakaan");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen(); // Pusatkan window
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
