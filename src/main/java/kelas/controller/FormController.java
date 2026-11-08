package kelas.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import kelas.model.Produk;
import kelas.data.DataProduk;

public class FormController implements Initializable {

    @FXML
    private TextField namaField;

    @FXML
    private ComboBox<String> kategoriBox;

    @FXML
    private TextField hargaField;

    @FXML 
    private TextField jumlahField;

    @FXML
    private ImageView gambarView;

    @FXML
    private Label pesanLabel;

    private File fileGambarTerpilih; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kategoriBox.getItems().addAll("Makanan", "Minuman");
    }

    @FXML
    private void pilihGambar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Produk");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Berkas Gambar (*.png, *.jpg)", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            fileGambarTerpilih = file;
            Image image = new Image(file.toURI().toString());
            gambarView.setImage(image);
        }
    }

    @FXML
    private void simpan(ActionEvent event) {
        String nama = namaField.getText().trim();
        String kategori = kategoriBox.getValue();
        String hargaText = hargaField.getText().trim();
        String jumlahText = jumlahField.getText().trim();

        // 1. Validasi input tidak boleh kosong
        if (nama.isEmpty() || kategori == null || hargaText.isEmpty() || jumlahText.isEmpty()) {
            pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
            pesanLabel.setText("Semua kolom (Nama, Kategori, Harga, Jumlah) wajib diisi!");
            return;
        }

        try {
            // 2. Parsing String ke tipe angka
            double harga = Double.parseDouble(hargaText);
            int jumlah = Integer.parseInt(jumlahText);

            // 3. Validasi batasan angka
            if (harga <= 0) {
                pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
                pesanLabel.setText("Harga produk harus lebih dari 0!");
                return;
            }

            if (jumlah < 0) {
                pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
                pesanLabel.setText("Jumlah produk tidak boleh kurang dari 0!");
                return;
            }

            String pathGambar = (fileGambarTerpilih != null) ? fileGambarTerpilih.getAbsolutePath() : "";

            // 4. Buat objek produk dan tambahkan ke data
            Produk produkBaru = new Produk(nama, kategori, harga, jumlah, pathGambar);
            DataProduk.tambahProduk(produkBaru);

            // 5. Bersihkan isi form (untuk reset jika pengguna kembali ke halaman ini)
            namaField.clear();
            kategoriBox.setValue(null);
            hargaField.clear();
            jumlahField.clear();
            gambarView.setImage(null);
            fileGambarTerpilih = null;

            // 6. Buka halaman detail dan kirim data produkBaru
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/kelas/detail.fxml"));
                Parent root = loader.load();

                // Mengambil controller dari halaman detail dan mengirim data
                DetailController detailController = loader.getController();
                detailController.setProduk(produkBaru);

                // Mengubah tampilan layar
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
                pesanLabel.setText("Gagal membuka halaman detail!");
                ex.printStackTrace();
            }

        } catch (NumberFormatException e) {
            pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
            pesanLabel.setText("Harga dan Jumlah harus berupa angka yang valid!");
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
            pesanLabel.setStyle("-fx-text-fill: #e53935; -fx-font-weight: bold;");
            pesanLabel.setText("Gagal membuka halaman daftar produk!");
            e.printStackTrace();
        }
    }
}