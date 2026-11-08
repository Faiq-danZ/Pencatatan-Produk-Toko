package kelas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/kelas/form.fxml"));
        
        // Otomatis mengikuti ukuran Pref Width & Pref Height dari Scene Builder
        Scene scene = new Scene(root);
        
        stage.setTitle("Aplikasi Pencatatan Produk Toko");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}