package z80box.z80box;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

// Este es el controlador que nos permitirá manipular la memoria de forma
// manual.
public class MemoryHandlingController {
    // Añadimos el visualizador de memoria como variable.
    @FXML
    GridPane memoryVisualizer;
    // Añadimos al paginador de memoria como variable.
    @FXML
    Pagination memoryPaginator;

    // Añadimos a la página actual del paginador como variable.
    public static int currentPage = 0;

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
    // Metodo de inicialización del menú de manejo de memoria.
    @FXML
    public void initialize() {
        // En este metodo, definimos un escuchador que nos permita hacer cambios a la página del menú.
        memoryPaginator.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            // Hacemos override al metodo superior que detecta cambios.
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Si el valor a cambiar del escuchador no es nulo, procedemos con el código.
                if(newValue != null){
                    // Damos valor de página actual a currentPage.
                    currentPage = memoryPaginator.getCurrentPageIndex();
                    // Borramos todos los elementos para reemplazarlos.
                    memoryVisualizer.getChildren().clear();
                    //  Comenzamos con un ciclo for anidado para llenar todos los espacios.
                    for(int i=0;i<33;i++){
                        for(int j=0;j<16;j++){
                            // Dejamos un espacio libre en la esquina izquierda.
                            if(i==0 && j == 0){
                                j+=1;
                            }
                            // Condicional que nos permite poner los valores unitarios en la fila superior.
                            if(i==0){
                                // Creamos un nuevo cuadro de texto
                                Label label = new Label(String.valueOf(j));
                                // Convertimos a hexadecimal en el caso de 10 hasta 15.
                                if(j>9){
                                    label.setText(Miscellaneous.returnHexEquivalent(j));
                                }
                                // Monerías visuales.
                                label.setTextFill(Color.HOTPINK);
                                label.setAlignment(Pos.CENTER);
                                // Añadimos el texto al cuadro que tenemos.
                                memoryVisualizer.add(label, j, i);
                            }
                            // Condicional que nos permite poner los valores mayores en la columna izquierda.
                            if(j==0 && i != 0){
                                // Utilizamos current page para multiplicar a i con una ecuación que nos permite
                                // poner el valor correspondiente de cada fila en el Label.
                                Label label = new Label(String.format("%04X", (512*currentPage)+((i-1)*16)));
                                label.setTextFill(Color.HOTPINK);
                                // Añadimos el label.
                                memoryVisualizer.add(label, j, i);
                            }
                            // En esta condicional, ya ponemos los valores de la memoria que nos competen como tal.
                            if(j!=0 && i != 0){
                                // Creamos un campo de texto que el usuario podrá personalizar.
                                TextField textField = new TextField(Z80App.memoria.m[(32*currentPage)+(i-1)][(j-1)]);
                                // Monerías visuales.
                                textField.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, null)));
                                textField.setAlignment(Pos.CENTER);
                                // Añadimos el campo de texto.
                                memoryVisualizer.add(textField, j, i);
                            }
                        }
                    }
                }
            }
        });
        // Este código es idéntico al del escuchador, pero se pone para que se inicialicen los valores
        // según se necesite.
        for(int i=0;i<33;i++){
            for(int j=0;j<16;j++){
                if(i==0 && j == 0){
                    j+=1;
                }

                if(i==0){
                    Label label = new Label(String.valueOf(j));
                    if(j>9){
                        label.setText(Miscellaneous.returnHexEquivalent(j));
                    }
                    label.setTextFill(Color.HOTPINK);
                    label.setAlignment(Pos.CENTER);
                    memoryVisualizer.add(label, j, i);
                }

                if(j==0 && i != 0){
                    Label label = new Label(String.format("%04X", (512*currentPage)+((i-1)*16)));
                    label.setTextFill(Color.HOTPINK);
                    memoryVisualizer.add(label, j, i);
                }

                if(j!=0 && i != 0){
                    TextField textField = new TextField(Z80App.memoria.m[(32*currentPage)+i-1][j-1]);
                    textField.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, null)));
                    textField.setAlignment(Pos.CENTER);
                    memoryVisualizer.add(textField, j, i);
                }
            }
        }

    }

    // Este es el metodo que nos permitirá cambiar valores de la memoria de manera manual.
    public void changeFieldValue(KeyEvent event){
        // Si la tecla presionada es enter, procedemos con el código.
        if(event.getCode() == KeyCode.ENTER){
            // Conseguimos el campo de texto que se quiere editar.
            TextField textField = (TextField) memoryVisualizer.getScene().getFocusOwner();
            // Java es quisquilloso, entonces nos aseguramos de que el campo de texto sea
            // un objeto de tipo TextField.
            if(textField instanceof TextField){
                // Conseguimos la columna y el espacio del campo de texto.
                int i = GridPane.getRowIndex(textField);
                int j = GridPane.getColumnIndex(textField);

                //Try catch que nos permitirá cambiar los valores.
                try{
                    // Si la entrada es mayor que 2, arrojamos una excepción
                    if(textField.getText().length() > 2){
                        throw new Exception();
                    }
                    // Si no hay excepciones, adjuntamos el valor que se requiere a la memoria
                    // del programa.
                    Z80App.memoria.m[(32*currentPage)+(i-1)][(j-1)] = textField.getText();
                } catch (Exception e) {

                }finally{
                    // Metodo de debug.
                    Z80App.memoria.imprimirMemoria();
                }
            }
        }
    }
}
