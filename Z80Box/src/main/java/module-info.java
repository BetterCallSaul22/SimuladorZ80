module z80box.z80box {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported.desktop;


    opens z80box.z80box to javafx.fxml;
    exports z80box.z80box;
}