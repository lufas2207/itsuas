package controller;

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
import model.Anggota;
import model.Buku;
import model.Peminjaman;

import java.io.*;
import java.time.LocalDate;

public class UserController {
    @FXML private TableView<Buku> tableBuku;
    @FXML private TableColumn<Buku, String> colKode, colJudul, colPengarang;
    @FXML private TableColumn<Buku, Integer> colTahun, colStok;
    @FXML private Label labelNama;
    @FXML private BorderPane root;

    private ObservableList<Buku> daftarBuku = FXCollections.observableArrayList();
    private Anggota anggota;

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
        if (labelNama != null) {
            labelNama.setText("Selamat Datang, " + anggota.getNama() + "!");
            tampilkanDataBuku();
        }
    }

    @FXML
    public void initialize() {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kodeBuku"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        tableBuku.setItems(daftarBuku);
    }

    @FXML
    public void showBuku() {
        tampilkanDataBuku();
    }

    public void tampilkanDataBuku() {
        daftarBuku.clear();
        muatDataBuku();
    }

    private void muatDataBuku() {
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
            e.printStackTrace();
        }
    }

    private void simpanDataBuku() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("buku.txt"))) {
            for (Buku buku : daftarBuku) {
                writer.write(buku.getKodeBuku() + "," +
                        buku.getJudul() + "," +
                        buku.getPengarang() + "," +
                        buku.getTahunTerbit() + "," +
                        buku.getStok());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePinjam() {
        Buku selected = tableBuku.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Gagal", "Pilih buku terlebih dahulu.");
            return;
        }

        if (!selected.isTersedia()) {
            showAlert("Tidak Tersedia", "Buku sudah habis dipinjam.");
            return;
        }

        selected.kurangiStok();
        simpanDataBuku();
        tableBuku.refresh();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("peminjaman.txt", true))) {
            String idPeminjaman = "PJ" + System.currentTimeMillis();
            String tanggalPinjam = LocalDate.now().toString();
            writer.write(idPeminjaman + "," +
                    anggota.getIdAnggota() + "," +
                    selected.getKodeBuku() + "," +
                    tanggalPinjam + "," +
                    "-");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Berhasil", "Peminjaman berhasil disimpan dan akan muncul di admin!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showRiwayat() {
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
            e.printStackTrace();
        }

        if (riwayat.length() == 0) {
            showAlert("Riwayat Kosong", "Belum ada peminjaman buku.");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Riwayat Peminjaman");
            alert.setHeaderText("Data peminjaman oleh " + anggota.getNama());
            alert.setContentText(riwayat.toString());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            // Tutup jendela saat ini
            Stage currentStage = (Stage) tableBuku.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
