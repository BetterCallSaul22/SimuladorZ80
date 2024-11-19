package z80box.z80box;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ExecutionPreludeHandler {
    @FXML
    Label errorDisplayer;
    @FXML
    TextField hexField;

    @FXML
    // Metodo que nos permitirá regresar al menú principal de ser necesario.
    public void backToMainMenu(KeyEvent event) throws IOException {
        // El evento solo toma lugar si la tecla presionada es la de escape.
        if(event.getCode() == KeyCode.ESCAPE){
            // Creamos un FXMLLoader que cargará la escena del menú principal.
            FXMLLoader fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80MainMenu.fxml"));
            // Se define el objeto Stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // Se define la escena con base en el cargador.
            Scene scene = new Scene(fxmlLoader.load());
            // Se pone el título del stage.
            stage.setTitle("Z80Box");
            // Se pone la escena cargada.
            stage.setScene(scene);
            // Mostramos la escena.
            stage.show();
        }
    }

    public void goOntoExecution(KeyEvent event){
        if(!hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            try{
                // Calculamos el valor donde comenzamos a escribir en memoria.
                int startDir = Miscellaneous.calculateOverallStartValue(hexField.getText());
                // Si el valor es muy grande o muy chico, arrojamos una excepción.
                if(startDir >= 32768 || startDir <=-1){
                    throw new InvalidHexDirectionException();
                }
                // Creamos un FXMLLoader que cargará la escena del menú principal.
                FXMLLoader fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80ExecutionMenu.fxml"));
                // Se define el objeto Stage
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                // Se define la escena con base en el cargador.
                Scene scene = new Scene(fxmlLoader.load());
                // Se pone el título del stage.
                stage.setTitle("Ejecución");
                // Se pone la escena cargada.
                stage.setScene(scene);
                // Mostramos la escena.
                stage.show();
            }catch (InvalidHexDirectionException e){
                errorDisplayer.setText("La dirección introducida es inválida.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
