module com.rttz.assignment {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rttz.assignment to javafx.fxml;
    exports com.rttz.assignment;
}
