package kelas.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import kelas.data.DataProduk;
import kelas.model.Produk;

public class DaftarController implements Initializable {

    @FXML private TextField cariField;
    @FXML private ComboBox<String> kategoriCombo;
    @FXML private Button btnCari;
    @FXML private Button btnReset;
    @FXML private Button btnTambah;
    @FXML private Button btnDetail;
    @FXML private Button btnHapus;
    @FXML private CheckBox chkTotal;
    @FXML private CheckBox chkJumlah;
    
    @FXML private TableView<Produk> produkTable;
    @FXML private TableColumn<Produk, String> kodeCol;
    @FXML private TableColumn<Produk, String> namaCol;
    @FXML private TableColumn<Produk, String> kategoriCol;
    @FXML private TableColumn<Produk, Double> hargaCol;
    @FXML private TableColumn<Produk, Integer> jumlahCol;
    @FXML private TableColumn<Produk, String> gambarCol;

    private FilteredList<Produk> filteredData;
    
    @FXML private Button btnKembali;

    @FXML
    private void handleKembali(ActionEvent event) {
        pindahHalaman(event, "/kelas/form.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Pembagian proporsi lebar kolom (% dari total lebar tabel)
        kodeCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.12));    // 12%
        namaCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.35));    // 35% (Paling Lebar)
        kategoriCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.15)); // 15%
        hargaCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.14));    // 14%
        jumlahCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.10));   // 10%
        gambarCol.prefWidthProperty().bind(produkTable.widthProperty().multiply(0.14));   // 14%
        
        produkTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        
        // 1. Hilangkan pesan default "No content in table"
        produkTable.setPlaceholder(new Label(""));

        // 2. Hubungkan kolom tabel ke atribut di class Produk
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kategoriCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("stok"));
        gambarCol.setCellValueFactory(new PropertyValueFactory<>("pathGambar"));

        // 3. Render thumbnail gambar (40x40 px) - fleksibel membaca path lokal & URI string
        gambarCol.setCellFactory(col -> new TableCell<Produk, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String pathGambar, boolean empty) {
                super.updateItem(pathGambar, empty);

                if (empty || pathGambar == null || pathGambar.trim().isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image img;
                        if (pathGambar.startsWith("file:") || pathGambar.startsWith("http")) {
                            img = new Image(pathGambar, 40, 40, true, true);
                        } else {
                            File file = new File(pathGambar);
                            img = file.exists() ? new Image(file.toURI().toString(), 40, 40, true, true) : null;
                        }

                        if (img != null && !img.isError()) {
                            imageView.setImage(img);
                            setGraphic(imageView);
                        } else {
                            setGraphic(null);
                        }
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        // 4. Isi item ComboBox Kategori jika belum terisi
        if (kategoriCombo != null && kategoriCombo.getItems().isEmpty()) {
            kategoriCombo.getItems().addAll("Semua Kategori", "Makanan", "Minuman");
        }

        // 5. Inisialisasi Filter Pencarian & Kategori
        setupFilterPencarian();
    }

    private void setupFilterPencarian() {
        filteredData = new FilteredList<>(DataProduk.getListProduk(), p -> true);

        // Listener saat mengetik di pencarian
        cariField.textProperty().addListener((obs, oldValue, newValue) -> applyFilter());

        // Listener saat memilih kategori
        if (kategoriCombo != null) {
            kategoriCombo.valueProperty().addListener((obs, oldValue, newValue) -> applyFilter());
        }

        produkTable.setItems(filteredData);
    }

    private void applyFilter() {
        String filterTeks = (cariField.getText() != null) ? cariField.getText().toLowerCase().trim() : "";
        String filterKategori = (kategoriCombo != null) ? kategoriCombo.getValue() : null;

        filteredData.setPredicate(produk -> {
            // Check Kategori Filter
            boolean matchKategori = (filterKategori == null || filterKategori.isEmpty() || filterKategori.equals("Semua Kategori")) 
                || (produk.getKategori() != null && produk.getKategori().equalsIgnoreCase(filterKategori));

            // Check Kode / Nama Filter
            boolean matchTeks = filterTeks.isEmpty() 
                || (produk.getKode() != null && produk.getKode().toLowerCase().contains(filterTeks))
                || (produk.getNama() != null && produk.getNama().toLowerCase().contains(filterTeks));

            return matchKategori && matchTeks;
        });
    }

    // --- EVENT HANDLERS ---

    @FXML
    private void handleFilterKategori(ActionEvent event) {
        applyFilter();
    }

    @FXML
    private void handleCari(ActionEvent event) {
        applyFilter();
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cariField.clear();
        if (kategoriCombo != null) {
            kategoriCombo.getSelectionModel().clearSelection();
        }
        applyFilter();
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        pindahHalaman(event, "/kelas/form.fxml");
    }

    @FXML
    private void handleDetail(ActionEvent event) {
        Produk terpilih = produkTable.getSelectionModel().getSelectedItem();
        if (terpilih != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/kelas/detail.fxml"));
                Parent root = loader.load();

                DetailController detailController = loader.getController();
                detailController.setProduk(terpilih);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                tampilkanPeringatan("Error Navigasi", "Gagal membuka halaman detail produk!");
                e.printStackTrace();
            }
        } else {
            tampilkanPeringatan("Pilih Produk", "Silakan pilih salah satu baris produk di tabel untuk melihat detail!");
        }
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        Produk terpilih = produkTable.getSelectionModel().getSelectedItem();
        if (terpilih != null) {
            DataProduk.getListProduk().remove(terpilih);
        } else {
            tampilkanPeringatan("Pilih Produk", "Silakan pilih salah satu baris produk di tabel yang ingin dihapus!");
        }
    }

    @FXML
    private void handleToggleTotal(ActionEvent event) {
        // Logika toggle kolom harga / total
    }

    @FXML
    private void handleToggleJumlah(ActionEvent event) {
        if (jumlahCol != null && chkJumlah != null) {
            jumlahCol.setVisible(chkJumlah.isSelected());
        }
    }

    // --- HELPER METHOD NAVIGASI & DIALOG ---

    private void pindahHalaman(ActionEvent event, String pathFxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pathFxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            tampilkanPeringatan("Error Navigasi", "Gagal membuka halaman: " + pathFxml);
            e.printStackTrace();
        }
    }

    private void tampilkanPeringatan(String header, String isi) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(header);
        alert.setContentText(isi);
        alert.showAndWait();
    }
}