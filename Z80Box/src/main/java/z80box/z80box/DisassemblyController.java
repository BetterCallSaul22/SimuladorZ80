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
            System.out.println("STARTDIR = " + startDir);
            int nextRow = 0;
            int nextRow2 = 0;
            String nextOpcode;
            try{
                if(startDir >= 32768 || startDir <=-1){
                    throw new InvalidHexDirectionException();
                }
                // Definimos el renglón en el que empezamos.
                int initRow = startDir/16;
                System.out.println("INITROW = " + initRow);
                // Definimos la columna en la que empezamos.
                int initColumn = startDir%16;
                System.out.println("INITCOLUMN = " + initColumn);
                String instruction;
                do {
                    String currentOpcode = Z80App.memoria.m[initRow][initColumn];
                    if ((initColumn + 1) % 16 == 0) {
                        nextOpcode = Z80App.memoria.m[initRow + 1][0];
                    } else {
                        nextOpcode = Z80App.memoria.m[initRow][initColumn + 1];
                    }

                    if (currentOpcode.startsWith("CB")) {
                        instruction = Instruction.findBitTableInstruction(nextOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else {
                            initColumn += 1;
                        }
                    } else if (Objects.equals((currentOpcode + nextOpcode), "DDCB")) {
                        instruction = Instruction.findIXBitTableInstruction(currentOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else if ((initColumn + 2) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else {
                            initColumn += 2;
                        }
                    } else if (Objects.equals((currentOpcode + nextOpcode), "FDCB")) {
                        instruction = Instruction.findIYBitTableInstruction(currentOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else if ((initColumn + 2) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else {
                            initColumn += 2;
                        }
                    } else if (currentOpcode.startsWith("DD")) {
                        instruction = Instruction.findIXTableInstruction(nextOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else {
                            initColumn += 1;
                        }
                        currentOpcode = Z80App.memoria.m[initRow][initColumn];
                    } else if (currentOpcode.startsWith("FD")) {
                        instruction = Instruction.findIYTableInstruction(nextOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        } else {
                            initColumn += 1;
                        }
                    }else if(currentOpcode.startsWith("ED")){
                        instruction = Instruction.findMiscTableInstruction(nextOpcode);
                        if ((initColumn + 1) % 16 == 0) {
                            initColumn = 1;
                            initRow += 1;
                        }else{
                            initColumn += 1;
                        }
                    }else {
                        instruction = Instruction.findMainTableInstruction(currentOpcode);
                    }

                    if (instruction == null) {
                        instruction = "NULL";
                    }
                    System.out.println(instruction);
                    System.out.println("LOOP START ROW = " + initRow + " LOOP START COLUMN = " + initColumn );
                    if (instruction.endsWith(",N") || instruction.endsWith(" N")|| instruction.endsWith(",D")) {
                        instruction = instruction.substring(0, instruction.length() - 2);
                        if (initRow == 2047 && initColumn == 15) {
                            initRow = 0;
                            initColumn = 0;
                            // De llegar al final de una fila, pasamos a la siguiente.
                        } else if (initColumn == 15) {
                            initRow += 1;
                            initColumn = 0;
                            // Si no ocurre ninguno de los dos casos, pasamos a la siguiente columna.
                        } else {
                            initColumn += 1;
                        }
                        instruction = instruction + Z80App.memoria.m[initRow][initColumn];
                    } else if (instruction.endsWith(",(NN)")) {
                        instruction = instruction.replace("(NN)", "");
                        if ((initColumn + 1) % 16 <= 1) {
                            nextRow = 1;
                        }
                        int base10Direction = Miscellaneous.calculateOverallStartValue(Z80App.memoria.m[initRow + nextRow][(initColumn + 2) % 16]
                                + Z80App.memoria.m[initRow + nextRow][(initColumn + 1) % 16]);
                        int directionColumn = base10Direction%16;
                        int directionRow = base10Direction/16;
                        instruction += Z80App.memoria.m[directionRow][directionColumn];
                    }else if(instruction.endsWith(",NN")) {
                        instruction = instruction.replace(",NN", ", ");
                        if ((initColumn + 1) % 16 == 0) {
                            nextRow = 1;
                        }
                        if ((initColumn + 2) % 16 <= 1) {
                            nextRow2 = 1;
                        }
                        String base16Direction = Z80App.memoria.m[initRow + nextRow2][(initColumn + 2) % 16]
                                + Z80App.memoria.m[initRow + nextRow][(initColumn + 1) % 16];
                        instruction += base16Direction;
                        if(nextRow == 1){
                            initRow += 1;
                            initColumn = 1;
                        }else if(nextRow2 == 1){
                            initColumn = 0;
                            initRow += 1;
                        }else{
                            initColumn += 2;
                        }
                        nextRow = nextRow2 = 0;
                    }else if(instruction.endsWith("+D)")){
                        instruction = instruction.replace("+D)", "");
                        if (initRow == 2047 && initColumn == 15) {
                            initRow = 0;
                            initColumn = 0;
                            // De llegar al final de una fila, pasamos a la siguiente.
                        } else if (initColumn == 15) {
                            initRow += 1;
                            initColumn = 0;
                            // Si no ocurre ninguno de los dos casos, pasamos a la siguiente columna.
                        } else {
                            initColumn += 1;
                        }
                        instruction = instruction + Z80App.memoria.m[initRow][initColumn];
                    }
                    Label label = new Label(instruction);
                    label.setFont(new Font("Courier new", 35));
                    disassemblyVisualizer.getChildren().add(label);

                    if (initRow == 2047 && initColumn == 15) {
                        initRow = 0;
                        initColumn = 0;
                        // De llegar al final de una fila, pasamos a la siguiente.
                    } else if (initColumn == 15) {
                        initRow += 1;
                        initColumn = 0;
                        // Si no ocurre ninguno de los dos casos, pasamos a la siguiente columna.
                    } else {
                        initColumn += 1;
                    }
                    System.out.println("CURRENT ROW = " + initRow);
                    System.out.println("CURRENT COLUMN = " + initColumn);

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
            varRow -= 1;
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

        return Objects.equals(prevData, "00") && Objects.equals(nextData, "00")
        && Objects.equals(Z80App.memoria.m[row][column], "00");



    }

}
