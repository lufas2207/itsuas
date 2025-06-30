package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Peminjaman {
    private final StringProperty idPeminjaman;
    private final StringProperty idAnggota;
    private final StringProperty kodeBuku;
    private final ObjectProperty<LocalDate> tanggalPinjam;
    private final ObjectProperty<LocalDate> tanggalKembali;

    public Peminjaman(String idPeminjaman, String idAnggota, String kodeBuku,
                      LocalDate tanggalPinjam, LocalDate tanggalKembali) {
        this.idPeminjaman = new SimpleStringProperty(idPeminjaman);
        this.idAnggota = new SimpleStringProperty(idAnggota);
        this.kodeBuku = new SimpleStringProperty(kodeBuku);
        this.tanggalPinjam = new SimpleObjectProperty<>(tanggalPinjam);
        this.tanggalKembali = new SimpleObjectProperty<>(tanggalKembali);
    }

    // Getter Property (dibutuhkan TableView dan Binding)
    public StringProperty idPeminjamanProperty() {
        return idPeminjaman;
    }

    public StringProperty idAnggotaProperty() {
        return idAnggota;
    }

    public StringProperty kodeBukuProperty() {
        return kodeBuku;
    }

    public ObjectProperty<LocalDate> tanggalPinjamProperty() {
        return tanggalPinjam;
    }

    public ObjectProperty<LocalDate> tanggalKembaliProperty() {
        return tanggalKembali;
    }

    // Getter
    public String getIdPeminjaman() {
        return idPeminjaman.get();
    }

    public String getIdAnggota() {
        return idAnggota.get();
    }

    public String getKodeBuku() {
        return kodeBuku.get();
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam.get();
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali.get();
    }

    // Setter
    public void setIdPeminjaman(String idPeminjaman) {
        this.idPeminjaman.set(idPeminjaman);
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota.set(idAnggota);
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku.set(kodeBuku);
    }

    public void setTanggalPinjam(LocalDate tanggalPinjam) {
        this.tanggalPinjam.set(tanggalPinjam);
    }

    public void setTanggalKembali(LocalDate tanggalKembali) {
        this.tanggalKembali.set(tanggalKembali);
    }
}
