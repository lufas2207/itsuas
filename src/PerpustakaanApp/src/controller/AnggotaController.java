package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Anggota;

import java.io.*;

public class AnggotaController {
    @FXML private TextField tfID, tfNama, tfAlamat, tfHP, tfPassword;
    @FXML private TableView<Anggota> tableAnggota;
    @FXML private TableColumn<Anggota, String> colID, colNama, colAlamat, colHP;

    private ObservableList<Anggota> daftarAnggota = FXCollections.observableArrayList();
    private final String FILE_PATH = "anggota.txt";
    private Anggota anggotaYangDiedit = null;

    @FXML
    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idAnggota"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colHP.setCellValueFactory(new PropertyValueFactory<>("nomorHp"));
        tableAnggota.setItems(daftarAnggota);
        muatDataDariFile();
    }

    @FXML
    public void tambahAnggota(ActionEvent event) {
        String id = tfID.getText().trim();
        String nama = tfNama.getText().trim();
        String alamat = tfAlamat.getText().trim();
        String hp = tfHP.getText().trim();
        String password = tfPassword.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || password.isEmpty()) return;

        Anggota anggota = new Anggota(id, nama, alamat, hp);
        daftarAnggota.add(anggota);
        simpanSemuaKeFile();

        // Simpan ke login.txt dengan password dari form
        simpanKeLoginFile(id, password, "user");

        clearForm();
    }

    private void simpanKeLoginFile(String id, String password, String role) {
        boolean sudahAda = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1 && data[0].equals(id)) {
                    sudahAda = true;
                    break;
                }
            }
        } catch (IOException e) {
            // file belum ada, tidak masalah
        }

        if (!sudahAda) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("login.txt", true))) {
                writer.write(id + "," + password + "," + role);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void hapusAnggota(ActionEvent event) {
        Anggota selected = tableAnggota.getSelectionModel().getSelectedItem();
        if (selected != null) {
            daftarAnggota.remove(selected);
            simpanSemuaKeFile();
        }
    }

    @FXML
    public void editAnggota(ActionEvent event) {
        anggotaYangDiedit = tableAnggota.getSelectionModel().getSelectedItem();
        if (anggotaYangDiedit != null) {
            tfID.setText(anggotaYangDiedit.getIdAnggota());
            tfNama.setText(anggotaYangDiedit.getNama());
            tfAlamat.setText(anggotaYangDiedit.getAlamat());
            tfHP.setText(anggotaYangDiedit.getNomorHp());
        }
    }

    @FXML
    public void simpanEditAnggota(ActionEvent event) {
        if (anggotaYangDiedit != null) {
            anggotaYangDiedit.setIdAnggota(tfID.getText());
            anggotaYangDiedit.setNama(tfNama.getText());
            anggotaYangDiedit.setAlamat(tfAlamat.getText());
            anggotaYangDiedit.setNomorHp(tfHP.getText());

            tableAnggota.refresh();
            simpanSemuaKeFile();
            clearForm();
            anggotaYangDiedit = null;
        }
    }

    private void simpanSemuaKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Anggota anggota : daftarAnggota) {
                writer.write(anggota.getIdAnggota() + "," + anggota.getNama() + "," + anggota.getAlamat() + "," + anggota.getNomorHp());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDataDariFile() {
        daftarAnggota.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    Anggota anggota = new Anggota(data[0], data[1], data[2], data[3]);
                    daftarAnggota.add(anggota);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfID.clear();
        tfNama.clear();
        tfAlamat.clear();
        tfHP.clear();
        tfPassword.clear();
    }
}
