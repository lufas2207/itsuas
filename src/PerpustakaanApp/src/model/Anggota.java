package model;

public class Anggota {
    private String idAnggota;
    private String nama;
    private String alamat;
    private String nomorHp;

    public Anggota(String idAnggota, String nama, String alamat, String nomorHp) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.alamat = alamat;
        this.nomorHp = nomorHp;
    }

    // Getter dan Setter
    public String getIdAnggota() { return idAnggota; }
    public void setIdAnggota(String idAnggota) { this.idAnggota = idAnggota; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getNomorHp() { return nomorHp; }
    public void setNomorHp(String nomorHp) { this.nomorHp = nomorHp; }
}
