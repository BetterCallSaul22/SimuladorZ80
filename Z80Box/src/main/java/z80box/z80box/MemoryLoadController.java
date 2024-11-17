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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MemoryLoadController {

    File selectedHexFile;

    @FXML
    Label errorDisplayer;
    @FXML
    TextField hexField;

    @FXML
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

    @FXML
    public void selectAFile(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.setTitle("ELEGIR ARCHIVO HEX");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos HEX", "*.HEX"));
        Window parentWindow = ((Node) event.getSource()).getScene().getWindow();
        selectedHexFile = fc.showOpenDialog(parentWindow);
        System.out.println(selectedHexFile);
    }

    public void loadHexToMemory(KeyEvent event) throws IOException {
        if(!hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            if(selectedHexFile == null){
                errorDisplayer.setText("Elija un archivo HEX.");
                hexField.clear();
            }else{
                BufferedReader br = Files.newBufferedReader(selectedHexFile.toPath());
                String input;

                try{
                    int startDir = Miscellaneous.calculateOverallStartValue(hexField.getText());
                    System.out.println(startDir);
                    if(startDir >= 32768 || startDir <=-1){
                        throw new InvalidHexDirectionException();
                    }
                    int initRow = startDir/16;
                    int finRow = startDir%16;
                    while((input = br.readLine()) != null){

                        String input_sub = input.substring(9, input.length() - 2);
                        String[] individual_HexCodes = input_sub.split("[0-9A-F][0-9A-F]");
                        for(int i = 0;i<individual_HexCodes.length;i++){
                            Z80App.memoria.m[initRow][finRow] = individual_HexCodes[i];
                            if(initRow == 2047 && finRow == 15){
                                initRow = 0;
                                finRow = 0;
                            }else if(finRow == 15){
                                initRow += 1;
                                finRow = 0;
                            }else{
                                finRow += 1;
                            }
                        }
                    }

                }catch(NullPointerException e){
                    errorDisplayer.setText("Hubo un error al leer el archivo.");
                }catch(InvalidHexDirectionException e){
                    errorDisplayer.setText("La dirección introducida es incorrecta.");
                }finally{
                    br.close();
                    hexField.clear();
                }

            }
            Z80App.memoria.imprimirMemoria();
            hexField.clear();
        }else if(hexField.getText().isEmpty() && event.getCode() == KeyCode.ENTER){
            errorDisplayer.setText("Introduzca una dirección hexadecimal.");
        }
    }
}
