package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String user = data[0];
                    String pass = data[1];
                    String role = data[2];

                    if (user.equals(username) && pass.equals(password)) {
                        bukaHalaman(role);
                        return;
                    }
                }
            }
            showAlert("Login Gagal", "Username atau password salah");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bukaHalaman(String role) {
        try {
            Stage stage = new Stage();
            String fxml = role.equals("admin") ? "/view/MainView.fxml" : "/view/UserView.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setTitle("Aplikasi Perpustakaan");
            stage.setScene(new Scene(root));
            stage.show();

            tfUsername.getScene().getWindow().hide(); // tutup login window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}