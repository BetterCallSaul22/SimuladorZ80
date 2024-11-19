package z80box.z80box;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class DisassemblyController {
    @FXML
    VBox disassemblyVisualizer;

    @FXML
    TextField hexField;

    @FXML
    Label errorDisplayer;

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

    @FXML
    public void disassemble(KeyEvent event){
        if(!hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            disassemblyVisualizer.getChildren().clear();
            int startDir = Miscellaneous.calculateOverallStartValue(hexField.getText());
            System.out.println(startDir);
            try{
                if(startDir >= 32768 || startDir <=-1){
                    throw new InvalidHexDirectionException();
                }
                // Definimos el renglón en el que empezamos.
                int initRow = startDir/16;
                // Definimos la columna en la que empezamos.
                int initColumn = startDir%16;
                String instruction;
                do{
                    if(Objects.equals(Z80App.memoria.m[initRow][initColumn], "BC")){
                        instruction = Instruction.findBitTableInstruction(Z80App.memoria.m[initRow][initColumn]);
                    }else{
                        instruction = Instruction.findMainTableInstruction(Z80App.memoria.m[initRow][initColumn]);
                    }

                    if(instruction == null){
                        instruction = "NULL";
                    }
                    Label label = new Label(Z80App.memoria.m[initRow][initColumn] + " " + instruction);
                    label.setFont(new Font("Courier new", 35));
                    disassemblyVisualizer.getChildren().add(label);

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
                }while(!getNoCode(initRow, initColumn));
                errorDisplayer.setText("");
            }catch(InvalidHexDirectionException e){
                errorDisplayer.setText("La dirección introducida es incorrecta.");
            }


        }
    }

    public boolean getNoCode(int row, int column){
        String currentData = Z80App.memoria.m[row][column];
        int varRow = row, varColumn = column;
        if(row == 0 && column == 0){
            varRow = 2047;
            varColumn = 15;
        }else if(column == 0){
            row -= 1;
            varColumn = 15;
        }else{
            varColumn -= 1;
        }
        String prevData = Z80App.memoria.m[varRow][varColumn];

        varRow = row;
        varColumn = column;

        if(row == 2047 && column == 15){
            varRow = 0;
            varColumn = 0;
        }else if(column == 15){
            row += 1;
            varColumn = 0;
        }else{
            varColumn += 1;
        }
        String nextData = Z80App.memoria.m[varRow][varColumn];

        if(prevData == "00" && nextData == "00"){
            return true;
        }else{
            return false;
        }
    }



}
