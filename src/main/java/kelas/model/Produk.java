package kelas.model;

public class Produk {
    private static int counter = 1; // Penambah angka otomatis
    
    private String kode;
    private String nama;
    private String kategori;
    private double harga;
    private int stok;           // Ditambahkan untuk kolom Jumlah
    private String pathGambar;

    // Constructor utama (Kode otomatis dibuat program PRD-001, dst)
    public Produk(String nama, String kategori, double harga, int stok, String pathGambar) {
        this.kode = String.format("PRD-%03d", counter++);
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.pathGambar = pathGambar;
    }

    // --- Getter dan Setter ---
    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    // Getter & Setter untuk stok (dibutuhkan TableView)
    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getPathGambar() {
        return pathGambar;
    }

    public void setPathGambar(String pathGambar) {
        this.pathGambar = pathGambar;
    }
    
    
}