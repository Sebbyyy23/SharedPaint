module com.example.sharedpaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sharedpaint to javafx.fxml;
    exports com.example.sharedpaint;
}