package kelas.controller;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import kelas.model.Produk;

public class DetailController {

    @FXML
    private Label kodeLabel;

    @FXML
    private Label namaLabel;

    @FXML
    private Label kategoriLabel;

    @FXML
    private Label stokLabel;

    @FXML
    private Label hargaLabel;

    @FXML
    private ImageView gambarView;

    @FXML
    private Label pesanGambarLabel;

    // Method untuk menerima data produk yang dikirim dari halaman lain
    public void setProduk(Produk produk) {
        if (produk != null) {
            kodeLabel.setText(produk.getKode());
            namaLabel.setText(produk.getNama());
            kategoriLabel.setText(produk.getKategori());
            stokLabel.setText(produk.getStok() + " Pcs");

            // Format angka ke format mata uang Rupiah
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            hargaLabel.setText(formatRupiah.format(produk.getHarga()));

            // Menampilkan foto jika tersedia
            if (produk.getPathGambar() != null && !produk.getPathGambar().isEmpty()) {
                File file = new File(produk.getPathGambar());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    gambarView.setImage(image);
                    pesanGambarLabel.setVisible(false);
                    pesanGambarLabel.setManaged(false);
                } else {
                    pesanGambarLabel.setText("Gambar tidak ditemukan.");
                }
            } else {
                pesanGambarLabel.setText("Tanpa lampiran gambar.");
            }
        }
    }

    @FXML
    private void bukaDaftar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/kelas/daftar.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bukaForm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/kelas/form.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}