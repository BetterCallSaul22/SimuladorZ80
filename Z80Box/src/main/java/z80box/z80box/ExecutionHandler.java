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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.IOException;
import java.util.Objects;

import static z80box.z80box.Z80App.memoria;

public class ExecutionHandler {
    static int currentAddress;

    @FXML
    GridPane memoryVisualizer;
    // Añadimos al paginador de memoria como variable.
    @FXML
    Pagination memoryPaginator;

    public static int currentPage = 0;

    public static int PC = Registers.PC;

    Registers registers = new Registers();

    @FXML
    // Metodo que nos permitirá regresar al menú principal de ser necesario.
    public void keyEventHandler(KeyEvent event) throws IOException {
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
        }else if(event.getCode() == KeyCode.ENTER){
            ejecutarCiclo();
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
                        for(int j=0;j<17;j++){
                            // Dejamos un espacio libre en la esquina izquierda.
                            if(i==0 && j == 0){
                                j+=1;
                            }
                            // Condicional que nos permite poner los valores unitarios en la fila superior.
                            if(i==0){
                                // Creamos un nuevo cuadro de texto
                                Label label = new Label(String.valueOf(j-1));
                                // Convertimos a hexadecimal en el caso de 10 hasta 15.
                                if(j>10){
                                    label.setText(Miscellaneous.returnHexEquivalent(j-1));
                                }
                                // Monerías visuales.
                                label.setTextFill(Color.HOTPINK);
                                label.setAlignment(Pos.CENTER);
                                // Añadimos el texto al cuadro que tenemos.
                                memoryVisualizer.add(label, j, i);
                            }
                            // Condicional que nos permite poner los valores mayores en la columna izquierda.
                            if(j==0){
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
                                TextField textField = new TextField(memoria.m[(32*currentPage)+(i-1)][(j-1)]);
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
        memoryPaginator.setCurrentPageIndex(currentPage);

        // Este código es idéntico al del escuchador, pero se pone para que se inicialicen los valores
        // según se necesite.
        for(int i=0;i<33;i++){
            for(int j=0;j<17;j++){
                if(i==0 && j == 0){
                    j+=1;
                }

                if(i==0){
                    Label label = new Label(String.valueOf(j-1));
                    if(j>10){
                        label.setText(Miscellaneous.returnHexEquivalent(j-1));
                    }
                    label.setTextFill(Color.HOTPINK);
                    label.setAlignment(Pos.CENTER);
                    memoryVisualizer.add(label, j, i);
                }

                if(j==0){
                    Label label = new Label(String.format("%04X", (512*currentPage)+((i-1)*16)));
                    label.setTextFill(Color.HOTPINK);
                    memoryVisualizer.add(label, j, i);
                }

                if(j!=0 && i != 0){
                    TextField textField = new TextField(memoria.m[(32*currentPage)+i-1][j-1]);
                    textField.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, null)));
                    textField.setAlignment(Pos.CENTER);
                    memoryVisualizer.add(textField, j, i);
                }
            }
        }

    }

    public boolean getNoCode(int row, int column){
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
        String prevData = memoria.m[varRow][varColumn];

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
        String nextData = memoria.m[varRow][varColumn];

        return Objects.equals(prevData, "00") && Objects.equals(nextData, "00")
                && Objects.equals(memoria.m[row][column], "00");

    }

    private int[] decodificarParametros() {
        int[] parametros = new int[1];
        int row, column;
        int lowByte = Registers.PC;
        row = lowByte/16; // Leer byte menos significativo
        column = lowByte%16;
        String s = Z80App.memoria.m[row][column];
        Registers.PC++; // Incrementar PC
        int highByte = Registers.PC;
        row = highByte/16; // Leer byte menos significativo
        column = highByte%16;
        String z = Z80App.memoria.m[row][column] + s;
        Registers.PC++; // Incrementar PC
        parametros[0] = Integer.parseInt(z); // Combinar ambos bytes en una dirección de 16 bits
        System.out.println("PARAMETRO DECODIFICADO: " + parametros[0]);
        return parametros;
    }

    public void ejecutarCiclo() {
        String opcode = fetchOpcode(); // Leer el opcode

        // Decodificar y ejecutar la instrucción
        ejecutarInstruccion(opcode);
    }

    private String fetchOpcode() {
        //se le debe de preguntar donde empieza el código con base en la opción de carga para poder empezar a jalar las instrucicones
        int direccion = Registers.PC;
        int row = direccion/16;
        int col = direccion % 16;
        Registers.PC += 1;
        if(Objects.equals(memoria.m[row][col], "DD") || Objects.equals(memoria.m[row][col], "FD")
                || Objects.equals(memoria.m[row][col], "CB") || Objects.equals(memoria.m[row][col], "ED")){
            return Z80App.memoria.m[row][col] + fetchOpcode();
        }else{
            return Z80App.memoria.m[row][col];
        }
    }

    // Método Decode & Execute: procesar la instrucción
    private void ejecutarInstruccion(String opcode) {
        int[] parametros;
        int row, column, res, memoryDir;
        switch (opcode) {
            case "3E": // LD A, n (inmediato de 8 bits)
                registers.A = Registers.PC; // Cargar valor en A
                Registers.PC += 1;
                System.out.println("Ejecutado: LD A, " + registers.A);
                break;
            case "7E": // LD A, (N lugar de memoria)
                parametros = decodificarParametros();
                registers.A = parametros[0]; // Cargar valor en A
                System.out.println("Ejecutado: LD A, " + registers.A);
                break;
            case "BF": // CP A, N
                parametros = decodificarParametros();
                res = registers.A - parametros[0];
                if ( res < 0){
                    registers.setFlagSign(true);
                }
                if ( res == 0){
                    registers.setFlagZero(true);
                }
                if (res > 0){
                    registers.setPositiveFlag(true);
                }
                break;
            case "4F": // LD C, A (inmediato de 8 bits)
                registers.C = registers.A; // Cargar valor en A
                System.out.println("Ejecutado: LD C, A");
                break;
            case "0D":
                registers.C--;
                Registers.PC += 1;
                break;
            case "51": // LD D, C (inmediato de 8 bits)
                registers.D = registers.C; // Cargar valor en A
                System.out.println("Ejecutado: LD D, C");
                break;
            case "06": // LD B, n (inmediato de 8 bits)
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.B = Integer.parseInt(Z80App.memoria.m[row][column]); // Cargar valor en A
                Registers.PC += 1;
                System.out.println("Ejecutado: LD B, " + registers.B);
                break;
            case "1E": // LD E, n (inmediato de 8 bits)
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.E = Integer.parseInt(Z80App.memoria.m[row][column]); // Cargar valor en A
                Registers.PC += 1;
                System.out.println("Ejecutado: LD E, " + registers.E);
                break;
            case "04":
                registers.B++;
                break;
            case "15":
                registers.D --;
                break;
            case "7A": // LD A, D (inmediato de 8 bits)
                parametros = decodificarParametros();
                registers.A = parametros[0]; // Cargar valor en A
                System.out.println("Ejecutado: LD A, D");
                break;
            case "7B": // LD D, C (inmediato de 8 bits)
                parametros = decodificarParametros();
                registers.A = parametros[0]; // Cargar valor en A
                System.out.println("Ejecutado: LD A, D");
                break;
            case "79": // LD A, C (inmediato de 8 bits)
                parametros = decodificarParametros();
                registers.A = parametros[0]; // Cargar valor en A
                System.out.println("Ejecutado: LD A, C");
                break;
            case "FA": // JP M, nn (salto si flag S está establecido)
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (registers.getFlagSign()) { // Verifica si el flag de signo está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    row = memoryDir/16;
                    column = memoryDir%16;
                    registers.setPC(Integer.parseInt(Z80App.memoria.m[row][column])); // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP M a dirección " + Integer.toHexString(parametros[0]));
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP M");
                }
                break;
            case "DD23": //Incrementar IX
                registers.IX = Integer.toHexString(Integer.parseInt(registers.IX) + 1);
                registers.setPC(registers.getPC() + 1);
                System.out.println();
                break;
            case "3A": // LD A, (NN)
                parametros = decodificarParametros();
                memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                row = memoryDir/16;
                column = memoryDir%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column]);
                break;
            case "FE":
                row = Registers.PC/16;
                column = Registers.PC%16;
                res = registers.A - Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println(res);
                if (res < 0){
                    registers.setFlagSign(true);
                }
                if (res == 0){
                    registers.setFlagZero(true);
                }
                if (res > 0){
                    registers.setPositiveFlag(true);
                }
                break;
            case "DD21":
                parametros = decodificarParametros();
                registers.IX = String.valueOf(parametros[0]);
                break;
            default:
                System.out.println("Instrucción no soportada: " + opcode);
        }
    }
}
