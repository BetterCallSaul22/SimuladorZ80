package z80box.z80box;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

// Esta es la clase que controla las operaciones llevadas a cabo en el menú de carga de memoria.
public class MemoryLoadController {

    // Esta clase tiene un atributo de archivo que nos permitirá ver
    // si el usuario tiene un archivo seleccionado a la hora de
    // poner una dirección hexadecimal.
    File selectedHexFile = null;

    // Este es un objeto de tipo Label que nos permitirá indicar al
    // usuario el estado de lo que está ocurriendo.
    @FXML
    Label errorDisplayer;
    // Este es el campo donde el usuario introducirá su valor hexa-
    // decimal.
    @FXML
    TextField hexField;

    // Metodo que regresará al menú principal.
    @FXML
    public void backToMainMenu(KeyEvent event) throws IOException {
        // Si el botón presionado es el de escape, entonces procede el
        // bloque de código.
        if(event.getCode() == KeyCode.ESCAPE){
            // Carga de archivo FXML de la escena principal.
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

    // Metodo principal para cargar el archivo a memoria.
    @FXML
    public void selectAFile(ActionEvent event){
        // Creamos un selector de archivos cuando se presiona el botón de seleccionar.
        FileChooser fc = new FileChooser();
        // Ponemos un título.
        fc.setTitle("ELEGIR ARCHIVO HEX");
        // Restringimos la selección a archivos HEX exclusivamente.
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos HEX", "*.HEX"));
        // Creamos una ventana para poner el FileChooser.
        Window parentWindow = ((Node) event.getSource()).getScene().getWindow();
        // A selectedHexFile se le da el valor del archivo seleccionado.
        selectedHexFile = fc.showOpenDialog(parentWindow);
    }

    // Metodo que ya carga la memoria según la dirección proveida.
    public void loadHexToMemory(KeyEvent event) throws IOException {
        // Si el campo NO está vacío y se presiona ENTER, se ejecuta el resto del código.
        if(!hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            System.out.println(selectedHexFile);
            // Si el archivo no ha sido seleccionado, procede este bloque.
            if(selectedHexFile == null){
                // Mostramos un error al usuario.
                errorDisplayer.setTextFill(Color.RED);
                errorDisplayer.setText("Elija un archivo HEX.");
                // Limpiamos el campo de entrada.
                hexField.clear();
            }else{
                // En caso contrario, procedemos con la escritura de memoria.
                // Creamos un lector de datos del archivo seleccionado.
                BufferedReader br = Files.newBufferedReader(selectedHexFile.toPath());
                // Creamos una cadena arbitraria.
                String input;

                // Bloque try-catch que atrapa varias excepciones.
                try{
                    // Calculamos el valor donde comenzamos a escribir en memoria.
                    int startDir = Miscellaneous.calculateOverallStartValue(hexField.getText());
                    // Si el valor es muy grande o muy chico, arrojamos una excepción.
                    if(startDir >= 32768 || startDir <=-1){
                        throw new InvalidHexDirectionException();
                    }
                    // Definimos el renglón en el que empezamos.
                    int initRow = startDir/16;
                    // Definimos la columna en la que empezamos.
                    int initColumn = startDir%16;
                    // Ciclo while que lee todas las líneas del archivo.
                    while((input = br.readLine()) != null){
                        // Como los HEX tienen un patrón, creamos una subcadena con la
                        // información que nos es relevante.
                        String input_sub = input.substring(9, input.length() - 2);
                        // Creamos una arraylist para obtener las instrucciones.
                        ArrayList<String> individual_HexCodes = new ArrayList<>();
                        // Añadimos las instrucciones al arraylist.
                        for(int i = 0;i<(input_sub.length());i+=2){
                            individual_HexCodes.add(input_sub.substring(i,i+2));
                        }
                        for(int i = 0;i<individual_HexCodes.size();i++){
                            // Cargamos la instrucción en cuestión al espacio encontrado.
                            Z80App.memoria.m[initRow][initColumn] = individual_HexCodes.get(i);
                            // Si llegamos al final de la memoria, regresamos al principio.
                            if(initRow == 2047 && initColumn == 15){
                                initRow = 0;
                                initColumn = 0;
                            // De llegar al final de una fila, pasamos a la siguiente.
                            }else if(initColumn == 15){
                                initRow += 1;
                                initColumn = 0;
                            // Si no ocurre ninguno de los dos casos, pasamos a la siguiente columna.
                            }else{
                                initColumn += 1;
                            }
                        }
                    }
                // Excepción en caso de que no se pueda leer el archivo.
                }catch(IOException e){
                    errorDisplayer.setTextFill(Color.RED);
                    errorDisplayer.setText("Hubo un error al leer el archivo.");
                // Excepción si la dirección introducida es inválida.
                }catch(InvalidHexDirectionException e){
                    errorDisplayer.setTextFill(Color.RED);
                    errorDisplayer.setText("La dirección introducida es incorrecta.");
                }finally{
                    // Cerramos el lector de archivos.
                    br.close();
                    // Limpiamos el campo de introducción de datos.
                    hexField.clear();
                }
                // Imprimimos memoria; para debuggear.
                // Indicamos al usuario que hubo éxito.
                errorDisplayer.setTextFill(Color.GREEN);
                errorDisplayer.setText("¡Archivo exitosamente cargado!");
            }
            Z80App.memoria.imprimirMemoria();
            selectedHexFile = null;
        // Si el campo está vacío y se presiona enter, damos a entender que se necesita una dirección.
        }else if(hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            errorDisplayer.setTextFill(Color.RED);
            errorDisplayer.setText("Introduzca una dirección hexadecimal.");
        }
    }
}
