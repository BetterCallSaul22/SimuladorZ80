package z80box.z80box;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

// Este se podría considerar el controlador de escenas "madre".
public class ScenesController {
    // Creamos un cargador universal.
    FXMLLoader fxmlLoader;

    //Este metodo nos cambia al menú de carga de memoria.
    @FXML
    public void switchToHexLoad(ActionEvent event) throws IOException {

        fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80LoadMemoryMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Carga de memoria");
        stage.setScene(scene);
        stage.show();
    }
    //Este metodo nos cambia al menú de desensamblado.
    @FXML
    public void switchToDisassembly(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80Disassemble.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Desensamblado");
        stage.setScene(scene);
        stage.show();
    }

    //Este metodo nos cambia al menú de desensamblado.
    @FXML
    public void switchToMemoryHandling(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80MemoryHandlerMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Manejo de memoria");
        stage.setScene(scene);
        stage.show();
    }


}