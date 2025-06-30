package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class DaftarAnggotaController {
    @FXML private TextField tfID, tfNama, tfAlamat, tfHP;
    @FXML private PasswordField pfPassword;

    private static final String FILE_ANGGOTA = "anggota.txt";
    private static final String FILE_LOGIN = "login.txt";

    @FXML
    private void handleDaftar() {
        String id = tfID.getText().trim();
        String nama = tfNama.getText().trim();
        String alamat = tfAlamat.getText().trim();
        String hp = tfHP.getText().trim();
        String password = pfPassword.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || password.isEmpty()) {
            showAlert("Gagal", "ID, Nama, dan Password wajib diisi!");
            return;
        }

        if (anggotaSudahTerdaftar(id)) {
            showAlert("Gagal", "ID Anggota sudah terdaftar. Gunakan ID lain.");
            return;
        }

        boolean anggotaTersimpan = simpanKeFile(FILE_ANGGOTA, id + "," + nama + "," + alamat + "," + hp);
        boolean loginTersimpan = simpanKeFile(FILE_LOGIN, id + "," + password + ",user");

        if (anggotaTersimpan && loginTersimpan) {
            showAlert("Berhasil", "Pendaftaran berhasil! Silakan login.");
            clearForm();
            kembaliKeLogin();
        } else {
            showAlert("Gagal", "Terjadi kesalahan saat menyimpan data.");
        }
    }

    @FXML
    private void handleBatal() {
        kembaliKeLogin();
    }

    private void kembaliKeLogin() {
        // Tutup halaman ini dan kembali ke login
        tfID.getScene().getWindow().hide();

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kesalahan", "Gagal kembali ke halaman login.");
        }
    }

    private boolean simpanKeFile(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean anggotaSudahTerdaftar(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_ANGGOTA))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1 && data[0].equalsIgnoreCase(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearForm() {
        tfID.clear();
        tfNama.clear();
        tfAlamat.clear();
        tfHP.clear();
        pfPassword.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}