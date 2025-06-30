module PerpustakaanApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media; // <-- Tambahkan ini untuk dukung MediaPlayer & MediaView

    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    opens view to javafx.fxml;

    exports controller;
    exports model;
    exports view;
}
