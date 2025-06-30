package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleBuku(final ActionEvent event) {
        bukaWindow("/view/BukuView.fxml", "Manajemen Buku");
    }

    @FXML
    private void handleAnggota(final ActionEvent event) {
        bukaWindow("/view/AnggotaView.fxml", "Manajemen Anggota");
    }

    @FXML
    private void handlePeminjaman(final ActionEvent event) {
        bukaWindow("/view/PeminjamanView.fxml", "Manajemen Peminjaman");
    }

    @FXML
    private void handleTambahAnggota(final ActionEvent event) {
        bukaWindow("/view/TambahAnggotaView.fxml", "Tambah Anggota Baru");
    }

    @FXML
    private void handleLogout(final ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();

            // Menutup stage saat ini
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Gagal", "Tidak dapat membuka halaman login.");
            e.printStackTrace();
        }
    }

    private void bukaWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Kesalahan", "Gagal membuka halaman: " + title);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Ganti dari INFORMATION ke ERROR
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
