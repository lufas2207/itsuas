module PerpustakaanApp {
    requires javafx.controls;
    requires javafx.fxml;

    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    opens view to javafx.fxml;

    exports controller;
    exports model;
    exports view;
}
