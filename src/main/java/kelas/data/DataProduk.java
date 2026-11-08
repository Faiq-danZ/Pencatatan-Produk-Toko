package kelas.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kelas.model.Produk;

public class DataProduk {
    // ObservableList khusus JavaFX agar tampilan tabel otomatis membarui diri
    private static final ObservableList<Produk> listProduk = FXCollections.observableArrayList();

    public static ObservableList<Produk> getListProduk() {
        return listProduk;
    }

    public static void tambahProduk(Produk produkBaru) {
        boolean ditemukan = false;

        // 1. Cek apakah produk dengan nama yang sama sudah ada
        for (Produk p : listProduk) {
            if (p.getNama().equalsIgnoreCase(produkBaru.getNama())) {
                // 2. Jika ada, tambahkan jumlah lama dengan jumlah baru
                p.setStok(p.getStok() + produkBaru.getStok());
                ditemukan = true;
                break; // Hentikan pencarian karena sudah diperbarui
            }
        }

        // 3. Jika belum pernah ada, tambahkan sebagai baris baru
        if (!ditemukan) {
            listProduk.add(produkBaru);
        }
    }
}