package model;

import javafx.beans.property.*;

public class Buku {
    private final StringProperty kodeBuku;
    private final StringProperty judul;
    private final StringProperty pengarang;
    private final IntegerProperty tahunTerbit;
    private final IntegerProperty stok;

    public Buku(String kodeBuku, String judul, String pengarang, int tahunTerbit, int stok) {
        this.kodeBuku = new SimpleStringProperty(kodeBuku);
        this.judul = new SimpleStringProperty(judul);
        this.pengarang = new SimpleStringProperty(pengarang);
        this.tahunTerbit = new SimpleIntegerProperty(tahunTerbit);
        this.stok = new SimpleIntegerProperty(stok);
    }

    // Getter untuk property (dipakai TableView)
    public StringProperty kodeBukuProperty() { return kodeBuku; }
    public StringProperty judulProperty() { return judul; }
    public StringProperty pengarangProperty() { return pengarang; }
    public IntegerProperty tahunTerbitProperty() { return tahunTerbit; }
    public IntegerProperty stokProperty() { return stok; }

    // Getter biasa
    public String getKodeBuku() { return kodeBuku.get(); }
    public String getJudul() { return judul.get(); }
    public String getPengarang() { return pengarang.get(); }
    public int getTahunTerbit() { return tahunTerbit.get(); }
    public int getStok() { return stok.get(); }

    // Setter biasa
    public void setKodeBuku(String value) { kodeBuku.set(value); }
    public void setJudul(String value) { judul.set(value); }
    public void setPengarang(String value) { pengarang.set(value); }
    public void setTahunTerbit(int value) { tahunTerbit.set(value); }
    public void setStok(int value) { stok.set(value); }

    // Method tambahan
    public boolean isTersedia() {
        return getStok() > 0;
    }

    public void kurangiStok() {
        if (getStok() > 0) {
            setStok(getStok() - 1);
        }
    }
}
