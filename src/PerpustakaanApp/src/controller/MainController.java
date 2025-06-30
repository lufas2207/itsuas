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
    private void handleBuku(ActionEvent event) {
        bukaWindow("/view/BukuView.fxml", "Manajemen Buku");
    }

    @FXML
    private void handleAnggota(ActionEvent event) {
        bukaWindow("/view/AnggotaView.fxml", "Manajemen Anggota");
    }

    @FXML
    private void handlePeminjaman(ActionEvent event) {
        bukaWindow("/view/PeminjamanView.fxml", "Manajemen Peminjaman");
    }

    @FXML
    private void handleTambahAnggota(ActionEvent event) {
        bukaWindow("/view/TambahAnggotaView.fxml", "Tambah Anggota Baru");
    }

    // (Opsional) Logout - bisa panggil dari tombol
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            // Tutup window sekarang
            Stage thisStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi bantu buka window FXML
    private void bukaWindow(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Gagal Membuka", "Gagal membuka jendela: " + title);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
