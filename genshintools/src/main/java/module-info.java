module com.hua.genshintools {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna.platform;
    requires com.sun.jna;


    opens com.hua.genshintools to javafx.fxml;
    exports com.hua.genshintools;
}