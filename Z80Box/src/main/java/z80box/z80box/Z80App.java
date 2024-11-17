package z80box.z80box;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Z80App extends Application {
    // Inicializamos la memoria de la aplicación.
    public static Memory memoria = new Memory();

    // Metodo que crea la escena con la que vamos a estar interactuando.
    @Override
    public void start(Stage stage) throws IOException {
        // Creamos un FXMLLoader que cargará la escena del menú principal.
        FXMLLoader fxmlLoader = new FXMLLoader(Z80App.class.getResource("Z80MainMenu.fxml"));
        // Creamos la escena con base en el FXMLLoader.
        Scene scene = new Scene(fxmlLoader.load());
        // Le damos un título a la ventana.
        stage.setTitle("Z80Box");
        // Ponemos la escena en la plantilla.
        stage.setScene(scene);
        // Mostramos la escena.
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /*public static void initialize(){

    }

     */
}