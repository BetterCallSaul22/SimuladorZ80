package z80box.z80box;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class ScenesController {
    FXMLLoader fxmlLoader;
    public void switchToHexLoad(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80LoadMemoryMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Carga de Memoria");
        stage.setScene(scene);
        stage.show();
    }
}