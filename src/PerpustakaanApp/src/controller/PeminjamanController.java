// PeminjamanController disempurnakan agar sesuai model.Peminjaman (5 kolom)
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Peminjaman;
import model.Buku;

import java.io.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PeminjamanController {
    @FXML private TextField tfIDAnggota, tfKodeBuku;
    @FXML private TableView<Peminjaman> tablePeminjaman;
    @FXML private TableColumn<Peminjaman, String> colIdPeminjaman, colIDAnggota, colKodeBuku;
    @FXML private TableColumn<Peminjaman, LocalDate> colTanggalPinjam, colTanggalKembali;
    @FXML private Button btnKembalikan;

    private ObservableList<Peminjaman> daftarPeminjaman = FXCollections.observableArrayList();
    private Set<String> daftarAnggota = new HashSet<>();
    private ObservableList<Buku> daftarBuku = FXCollections.observableArrayList();

    private final String FILE_ANGGOTA = "anggota.txt";
    private final String FILE_BUKU = "buku.txt";
    private final String FILE_PINJAM = "peminjaman.txt";

    @FXML
    public void initialize() {
        colIdPeminjaman.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));
        colIDAnggota.setCellValueFactory(new PropertyValueFactory<>("idAnggota"));
        colKodeBuku.setCellValueFactory(new PropertyValueFactory<>("kodeBuku"));
        colTanggalPinjam.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        colTanggalKembali.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        tablePeminjaman.setItems(daftarPeminjaman);

        muatDataAnggota();
        muatDataBuku();
        muatDataPeminjaman();
    }

    private void muatDataAnggota() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_ANGGOTA))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1) daftarAnggota.add(data[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDataBuku() {
        daftarBuku.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_BUKU))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    Buku buku = new Buku(data[0], data[1], data[2],
                            Integer.parseInt(data[3]),
                            Integer.parseInt(data[4]));
                    daftarBuku.add(buku);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDataPeminjaman() {
        daftarPeminjaman.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PINJAM))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    LocalDate tglPinjam = LocalDate.parse(data[3]);
                    LocalDate tglKembali = data[4].equals("-") ? null : LocalDate.parse(data[4]);
                    Peminjaman p = new Peminjaman(data[0], data[1], data[2], tglPinjam, tglKembali);
                    daftarPeminjaman.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void tambahPeminjaman(ActionEvent event) {
        String idAnggota = tfIDAnggota.getText().trim();
        String kodeBuku = tfKodeBuku.getText().trim();

        if (idAnggota.isEmpty() || kodeBuku.isEmpty()) {
            showAlert("Gagal", "Harap isi semua field!");
            return;
        }

        if (!daftarAnggota.contains(idAnggota)) {
            showAlert("Gagal", "ID Anggota tidak ditemukan!");
            return;
        }

        Buku bukuDipinjam = null;
        for (Buku b : daftarBuku) {
            if (b.getKodeBuku().equals(kodeBuku)) {
                bukuDipinjam = b;
                break;
            }
        }

        if (bukuDipinjam == null) {
            showAlert("Gagal", "Kode Buku tidak ditemukan!");
            return;
        }

        if (bukuDipinjam.getStok() <= 0) {
            showAlert("Gagal", "Stok buku habis!");
            return;
        }

        bukuDipinjam.setStok(bukuDipinjam.getStok() - 1);
        simpanBukuKeFile();

        LocalDate sekarang = LocalDate.now();
        Peminjaman pinjam = new Peminjaman(
                "PJ" + System.currentTimeMillis(), idAnggota, kodeBuku, sekarang, null);

        daftarPeminjaman.add(pinjam);
        simpanPeminjamanKeFile(pinjam);

        tfIDAnggota.clear();
        tfKodeBuku.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Peminjaman berhasil ditambahkan!");
        alert.showAndWait();
    }

    private void simpanBukuKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_BUKU))) {
            for (Buku b : daftarBuku) {
                writer.write(b.getKodeBuku() + "," + b.getJudul() + "," + b.getPengarang() + ","
                        + b.getTahunTerbit() + "," + b.getStok());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void simpanPeminjamanKeFile(Peminjaman data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PINJAM, true))) {
            writer.write(data.getIdPeminjaman() + "," +
                    data.getIdAnggota() + "," +
                    data.getKodeBuku() + "," +
                    data.getTanggalPinjam() + "," +
                    "-");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void simpanUlangPeminjamanKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PINJAM))) {
            for (Peminjaman p : daftarPeminjaman) {
                writer.write(p.getIdPeminjaman() + "," +
                        p.getIdAnggota() + "," +
                        p.getKodeBuku() + "," +
                        p.getTanggalPinjam() + "," +
                        (p.getTanggalKembali() != null ? p.getTanggalKembali() : "-"));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void hapusPeminjaman() {
        Peminjaman selected = tablePeminjaman.getSelectionModel().getSelectedItem();
        if (selected != null) {
            daftarPeminjaman.remove(selected);
            simpanUlangPeminjamanKeFile();
        } else {
            showAlert("Pilih Data", "Silakan pilih peminjaman yang ingin dihapus.");
        }
    }

    @FXML
    public void kembalikanBuku() {
        Peminjaman selected = tablePeminjaman.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getTanggalKembali() == null) {
            selected.setTanggalKembali(LocalDate.now());

            // Tambah stok bukunya kembali
            for (Buku b : daftarBuku) {
                if (b.getKodeBuku().equals(selected.getKodeBuku())) {
                    b.setStok(b.getStok() + 1);
                    break;
                }
            }
            simpanBukuKeFile();
            simpanUlangPeminjamanKeFile();
            tablePeminjaman.refresh();
        } else {
            showAlert("Tidak Valid", "Pilih peminjaman yang belum dikembalikan.");
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
