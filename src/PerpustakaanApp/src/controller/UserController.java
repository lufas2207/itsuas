package controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Anggota;
import model.Buku;

import java.io.*;
import java.time.LocalDate;

public class UserController {

    @FXML private BorderPane root;
    @FXML private Label labelNama;
    @FXML private TextField tfCari;
    @FXML private TableView<Buku> tableBuku;
    @FXML private TableColumn<Buku, String> colKode, colJudul, colPengarang;
    @FXML private TableColumn<Buku, Integer> colTahun, colStok;

    private final ObservableList<Buku> daftarBuku = FXCollections.observableArrayList();
    private Anggota anggota;

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
        labelNama.setText("Selamat Datang, " + anggota.getNama() + "!");
        tampilkanDataBuku();
    }

    @FXML
    public void initialize() {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kodeBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        tableBuku.setItems(daftarBuku);

        // Tambahkan animasi saat view muncul
        FadeTransition fade = new FadeTransition(Duration.millis(800), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void tampilkanDataBuku() {
        daftarBuku.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("buku.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    Buku buku = new Buku(
                            data[0],
                            data[1],
                            data[2],
                            Integer.parseInt(data[3]),
                            Integer.parseInt(data[4])
                    );
                    daftarBuku.add(buku);
                }
            }
        } catch (IOException e) {
            showError("Gagal Memuat Buku", "Terjadi kesalahan saat membaca data buku.");
        }
    }

    private void simpanDataBuku() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("buku.txt"))) {
            for (Buku buku : daftarBuku) {
                writer.write(String.join(",", buku.getKodeBuku(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        String.valueOf(buku.getTahunTerbit()),
                        String.valueOf(buku.getStok())));
                writer.newLine();
            }
        } catch (IOException e) {
            showError("Gagal Menyimpan", "Data buku gagal disimpan.");
        }
    }

    @FXML
    private void handlePinjam() {
        Buku selected = tableBuku.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Pilih Buku", "Silakan pilih buku yang ingin dipinjam.");
            return;
        }

        if (!selected.isTersedia()) {
            showInfo("Stok Habis", "Buku tidak tersedia untuk dipinjam.");
            return;
        }

        selected.kurangiStok();
        simpanDataBuku();
        tableBuku.refresh();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("peminjaman.txt", true))) {
            String idPeminjaman = "PJ" + System.currentTimeMillis();
            String tanggalPinjam = LocalDate.now().toString();
            writer.write(String.join(",", idPeminjaman, anggota.getIdAnggota(), selected.getKodeBuku(), tanggalPinjam, "-"));
            writer.newLine();
        } catch (IOException e) {
            showError("Gagal Mencatat", "Peminjaman tidak bisa disimpan.");
            return;
        }

        showInfo("Sukses", "Peminjaman berhasil disimpan.");
    }

    @FXML
    private void showRiwayat() {
        StringBuilder riwayat = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("peminjaman.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5 && data[1].equals(anggota.getIdAnggota())) {
                    riwayat.append("ID: ").append(data[0])
                            .append(" | Buku: ").append(data[2])
                            .append(" | Tanggal: ").append(data[3])
                            .append("\n");
                }
            }
        } catch (IOException e) {
            showError("Gagal Memuat", "Riwayat peminjaman gagal dimuat.");
            return;
        }

        if (riwayat.length() == 0) {
            showInfo("Riwayat Kosong", "Belum ada data peminjaman.");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Riwayat Peminjaman");
            alert.setHeaderText("Riwayat milik: " + anggota.getNama());
            alert.setContentText(riwayat.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) this.root.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showError("Gagal Logout", "Tidak dapat kembali ke halaman login.");
        }
    }

    @FXML
    private void handleCari() {
        String keyword = tfCari.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableBuku.setItems(daftarBuku);
            return;
        }

        ObservableList<Buku> hasil = FXCollections.observableArrayList();
        for (Buku b : daftarBuku) {
            if (b.getJudul().toLowerCase().contains(keyword) || b.getPengarang().toLowerCase().contains(keyword)) {
                hasil.add(b);
            }
        }

        tableBuku.setItems(hasil);

        if (hasil.isEmpty()) {
            showInfo("Pencarian", "Tidak ditemukan buku dengan kata kunci: " + keyword);
        }
    }

    @FXML
    private void handleRefresh() {
        tampilkanDataBuku();
        tableBuku.refresh();
        tfCari.clear();
    }

    // Alert
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Terjadi Kesalahan");
        alert.setContentText(message);
        alert.showAndWait();
    }
}