package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Anggota;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {
    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        animateButton((Button) event.getSource());

        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Gagal", "Username dan password tidak boleh kosong.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String user = data[0];
                    String pass = data[1];
                    String role = data[2];

                    if (user.equalsIgnoreCase(username) && pass.equals(password)) {
                        if (role.equalsIgnoreCase("user")) {
                            Anggota anggota = cariAnggota(user);
                            if (anggota != null) {
                                bukaHalamanUser(anggota);
                            } else {
                                showAlert("Login Gagal", "Data anggota tidak ditemukan.");
                            }
                        } else if (role.equalsIgnoreCase("admin")) {
                            bukaHalamanAdmin();
                        } else {
                            showAlert("Login Gagal", "Role tidak dikenali.");
                        }
                        return;
                    }
                }
            }
            showAlert("Login Gagal", "Username atau password salah");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kesalahan", "Gagal membaca file login.txt");
        }
    }

    private Anggota cariAnggota(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("anggota.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].equalsIgnoreCase(id)) {
                    return new Anggota(data[0], data[1], data[2], data[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void bukaHalamanAdmin() {
        bukaHalamanUmum("/view/MainView.fxml", "Halaman Admin");
    }

    private void bukaHalamanUser(Anggota anggota) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
            Parent root = loader.load();

            // Animasikan saat halaman muncul
            fadeInScene(root);

            UserController controller = loader.getController();
            controller.setAnggota(anggota);

            stage.setTitle("Halaman Pengguna");
            stage.setScene(new Scene(root));
            stage.show();
            tfUsername.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kesalahan", "Gagal membuka halaman user.");
        }
    }

    private void bukaHalamanUmum(String pathFXML, String title) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(pathFXML));

            fadeInScene(root); // Tambahkan animasi

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            tfUsername.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kesalahan", "Gagal membuka halaman.");
        }
    }

    @FXML
    private void handleDaftar(ActionEvent event) {
        animateButton((Button) event.getSource());
        bukaHalamanUmum("/view/DaftarAnggotaView.fxml", "Form Daftar Anggota Baru");
    }

    @FXML
    private void handleLupaPassword(ActionEvent event) {
        animateButton((Button) event.getSource());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lupa Password");
        alert.setHeaderText("Reset Password");
        alert.setContentText("Silakan hubungi admin untuk melakukan reset password.");
        alert.showAndWait();
    }

    private void animateButton(Button button) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void fadeInScene(Parent root) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
