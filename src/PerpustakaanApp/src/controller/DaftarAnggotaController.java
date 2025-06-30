package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Anggota;

import java.io.*;

public class DaftarAnggotaController {
    @FXML private TextField tfID, tfNama, tfAlamat, tfHP;
    @FXML private PasswordField pfPassword;

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

        // Simpan ke anggota.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("anggota.txt", true))) {
            writer.write(id + "," + nama + "," + alamat + "," + hp);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Simpan ke login.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login.txt", true))) {
            writer.write(id + "," + password + ",user");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Berhasil", "Pendaftaran berhasil! Silakan login.");
        clearForm();
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
