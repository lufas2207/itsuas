package model;

public class Buku {
    private String kodeBuku;
    private String judul;
    private String pengarang;
    private int tahunTerbit;
    private int stok;

    public Buku(String kodeBuku, String judul, String pengarang, int tahunTerbit, int stok) {
        this.kodeBuku = kodeBuku;
        this.judul = judul;
        this.pengarang = pengarang;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;
    }

    public String getKodeBuku() { return kodeBuku; }
    public void setKodeBuku(String kodeBuku) { this.kodeBuku = kodeBuku; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPengarang() { return pengarang; }
    public void setPengarang(String pengarang) { this.pengarang = pengarang; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}
