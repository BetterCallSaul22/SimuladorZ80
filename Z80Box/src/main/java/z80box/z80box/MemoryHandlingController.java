package z80box.z80box;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class MemoryHandlingController {

    @FXML
    GridPane memoryVisualizer;

    @FXML
    Pagination memoryPaginator;

    int currentPage = 0;

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

    @FXML
    public void initialize() {
        // Listener to track page changes
        memoryPaginator.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue != null){
                    currentPage = memoryPaginator.getCurrentPageIndex();
                    memoryVisualizer.getChildren().clear();
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
                                TextField textField = new TextField(Z80App.memoria.m[(32*currentPage)+(i-1)][(j-1)]);
                                textField.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, null)));
                                textField.setAlignment(Pos.CENTER);
                                memoryVisualizer.add(textField, j, i);
                            }
                        }
                    }
                }
            }
        });

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
}
