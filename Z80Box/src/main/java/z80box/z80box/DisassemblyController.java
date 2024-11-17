package z80box.z80box;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class DisassemblyController {
    @FXML
    // Método que nos permitirá regresar al menú principal de ser necesario.
    public void backToMainMenu(KeyEvent event) throws IOException {


        if(event.getCode() == KeyCode.ESCAPE){
            FXMLLoader fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80MainMenu.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Z80Box");
            stage.setScene(scene);
            stage.show();
        }
    }
}
