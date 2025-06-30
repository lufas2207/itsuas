package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Buku;

import java.io.*;

public class BukuController {
    @FXML private TextField tfKode, tfJudul, tfPengarang, tfTahun, tfStok, tfJumlahStok;
    @FXML private TableView<Buku> tableBuku;
    @FXML private TableColumn<Buku, String> colKode, colJudul, colPengarang;
    @FXML private TableColumn<Buku, Integer> colTahun, colStok;

    private ObservableList<Buku> daftarBuku = FXCollections.observableArrayList();
    private final String FILE_PATH = "buku.txt";
    private Buku bukuYangDiedit = null;

    @FXML
    public void initialize() {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kodeBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        tableBuku.setItems(daftarBuku);
        muatDataDariFile();
    }

    @FXML
    public void tambahBuku() {
        try {
            Buku buku = new Buku(
                    tfKode.getText(),
                    tfJudul.getText(),
                    tfPengarang.getText(),
                    Integer.parseInt(tfTahun.getText()),
                    Integer.parseInt(tfStok.getText()));
            daftarBuku.add(buku);
            simpanSemuaKeFile();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Input salah", "Tahun dan stok harus angka.");
        }
    }

    @FXML
    public void hapusBuku() {
        Buku selected = tableBuku.getSelectionModel().getSelectedItem();
        if (selected != null) {
            daftarBuku.remove(selected);
            simpanSemuaKeFile();
        }
    }

    @FXML
    public void tambahStok() {
        Buku selected = tableBuku.getSelectionModel().getSelectedItem();
        try {
            int tambahan = Integer.parseInt(tfJumlahStok.getText());
            if (selected != null) {
                selected.setStok(selected.getStok() + tambahan);
                tableBuku.refresh();
                simpanSemuaKeFile();
                tfJumlahStok.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Input salah", "Masukkan jumlah stok yang valid.");
        }
    }

    @FXML
    public void kurangiStok() {
        Buku selected = tableBuku.getSelectionModel().getSelectedItem();
        try {
            int kurang = Integer.parseInt(tfJumlahStok.getText());
            if (selected != null && selected.getStok() >= kurang) {
                selected.setStok(selected.getStok() - kurang);
                tableBuku.refresh();
                simpanSemuaKeFile();
                tfJumlahStok.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Input salah", "Masukkan jumlah stok yang valid.");
        }
    }

    @FXML
    public void editBuku() {
        bukuYangDiedit = tableBuku.getSelectionModel().getSelectedItem();
        if (bukuYangDiedit != null) {
            tfKode.setText(bukuYangDiedit.getKodeBuku());
            tfJudul.setText(bukuYangDiedit.getJudul());
            tfPengarang.setText(bukuYangDiedit.getPengarang());
            tfTahun.setText(String.valueOf(bukuYangDiedit.getTahunTerbit()));
            tfStok.setText(String.valueOf(bukuYangDiedit.getStok()));
        }
    }

    @FXML
    public void simpanEditBuku() {
        if (bukuYangDiedit != null) {
            try {
                bukuYangDiedit.setKodeBuku(tfKode.getText());
                bukuYangDiedit.setJudul(tfJudul.getText());
                bukuYangDiedit.setPengarang(tfPengarang.getText());
                bukuYangDiedit.setTahunTerbit(Integer.parseInt(tfTahun.getText()));
                bukuYangDiedit.setStok(Integer.parseInt(tfStok.getText()));
                tableBuku.refresh();
                simpanSemuaKeFile();
                clearForm();
                bukuYangDiedit = null;
            } catch (NumberFormatException e) {
                showAlert("Input salah", "Tahun dan stok harus angka.");
            }
        }
    }

    private void simpanSemuaKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Buku buku : daftarBuku) {
                writer.write(String.join(",",
                        buku.getKodeBuku(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        String.valueOf(buku.getTahunTerbit()),
                        String.valueOf(buku.getStok())));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDataDariFile() {
        daftarBuku.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    daftarBuku.add(new Buku(data[0], data[1], data[2],
                            Integer.parseInt(data[3]),
                            Integer.parseInt(data[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfKode.clear(); tfJudul.clear(); tfPengarang.clear(); tfTahun.clear(); tfStok.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
