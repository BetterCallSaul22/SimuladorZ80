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

    Registers registers = new Registers();

    @FXML
    Label registerAVal, registerBVal, registerCVal, registerDVal, registerEVal, registerHVal, registerLVal,
    registerIXVal, registerIYVal, zeroFlag, carryFlag, signFlag, parityFlag, halfCarryFlag, subtractFlag, positiveFlag, PCDisplay;

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
        }else if(event.getCode() == KeyCode.ENTER && !Objects.equals(fetchOpcodeRevise(), "76")){
            ejecutarCiclo();
            registerAVal.setText(String.valueOf(registers.A));
            registerBVal.setText(String.valueOf(registers.B));
            registerCVal.setText(String.valueOf(registers.C));
            registerDVal.setText(String.valueOf(registers.D));
            registerEVal.setText(String.valueOf(registers.E));
            registerHVal.setText(String.valueOf(registers.H));
            registerLVal.setText(String.valueOf(registers.L));
            registerIXVal.setText(registers.IX);
            registerIYVal.setText(registers.IY);
            zeroFlag.setText(registers.getFlagZero() ? "1" : "0");
            carryFlag.setText(registers.getFlagCarry() ? "1" : "0");
            signFlag.setText(registers.getFlagSign() ? "1" : "0");
            parityFlag.setText(registers.getFlagParity() ? "1" : "0");
            halfCarryFlag.setText(registers.getFlagHalfCarry() ? "1" : "0");
            subtractFlag.setText(registers.getFlagSubtract() ? "1" : "0");
            positiveFlag.setText(registers.getPositiveFlag() ? "1" : "0");

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
        }else if(event.getCode() == KeyCode.ENTER && Objects.equals(fetchOpcodeRevise(), "76")){
            System.out.println("HALT");
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

        PCDisplay.setText(opcode);

        // Decodificar y ejecutar la instrucción
        ejecutarInstruccion(opcode);
    }

    private String fetchOpcode() {
        //se le debe de preguntar donde empieza el código con base en la opción de carga para poder empezar a jalar las instrucicones
        int direccion = Registers.PC;
        //System.out.println("fetchOpcode: REGISTERS.PC = " + Registers.PC);
        int row = direccion/16;
        //System.out.println("fetchOpcode: ROW = " + row);
        int col = direccion%16;
        //System.out.println("fetchOpcode: COL = " + col);
        Registers.PC += 1;
        //System.out.println("INFO EN MEMORIA = " + Z80App.memoria.m[row][col]);
        if(Objects.equals(memoria.m[row][col], "DD") || Objects.equals(memoria.m[row][col], "FD")
                || Objects.equals(memoria.m[row][col], "CB") || Objects.equals(memoria.m[row][col], "ED")){
            return Z80App.memoria.m[row][col] + fetchOpcode();
        }else{
            return Z80App.memoria.m[row][col];
        }
    }

    private String fetchOpcodeRevise() {
        //se le debe de preguntar donde empieza el código con base en la opción de carga para poder empezar a jalar las instrucicones
        int direccion = Registers.PC;
        //System.out.println("fetchOpcode: REGISTERS.PC = " + Registers.PC);
        int row = direccion/16;
        //System.out.println("fetchOpcode: ROW = " + row);
        int col = direccion%16;
        //System.out.println("fetchOpcode: COL = " + col);
        //System.out.println("INFO EN MEMORIA = " + Z80App.memoria.m[row][col]);
        return Z80App.memoria.m[row][col];

    }

    // Metodo Decode & Execute: procesar la instrucción
    private void ejecutarInstruccion(String opcode) {
        int[] parametros;
        int row, column, res, memoryDir, displacement, effectiveAddress, newVal;
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
                System.out.println("EJECUTADO: CP A, " + res);
                break;
            case "4F": // LD C, A (inmediato de 8 bits)
                registers.C = registers.A; // Cargar valor en A
                System.out.println("Ejecutado: LD C, A");
                break;
            case "0D": // DEC C
                registers.C--;
                System.out.println("EJECUTADO: DEC C");
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
            case "04": // INC B
                registers.B++;
                System.out.println("EJECUTADO: B = " + registers.B);
                break;
            case "15": // DEC D
                registers.D --;
                System.out.println("EJECUTADO: DEC D");
                break;
            case "7A": // LD A, D (inmediato de 8 bits)
                registers.A = registers.D; // Cargar valor en A
                System.out.println("Ejecutado: LD A, D");
                break;
            case "7B": // LD A, E (inmediato de 8 bits)
                registers.A = registers.E; // Cargar valor en A
                System.out.println("Ejecutado: LD A, E");
                break;
            case "79": // LD A, C (inmediato de 8 bits)
                registers.A = registers.C; // Cargar valor en A
                System.out.println("Ejecutado: LD A, C");
                break;
            case "FA": // JP M, nn (salto si flag S está establecido)
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (registers.getFlagSign()) { // Verifica si el flag de signo está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP M a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP M");
                }
                break;
            case "F2": // JP P, nn (salto si flag S no está establecido)
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (!registers.getFlagSign()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP P a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP P");
                }
                break;
            case "C2": // JP NZ, nn (salto si flag Z no está establecido)
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (!registers.getFlagZero()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP NZ a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP NZ");
                }
                break;
            case "CA": // JP Z, nn (salto si flag Z está establecido)
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (registers.getFlagZero()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP Z a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP Z");
                }
                break;
            case "DD23": //Incrementar IX
                registers.IX = String.valueOf(Integer.parseInt(registers.IX) + 1);
                System.out.println("EJECUTADO, INC IX");
                break;
            case "3A": // LD A, (NN)
                parametros = decodificarParametros();
                memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                row = memoryDir/16;
                column = memoryDir%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println("EJECUTADO, LD A, (" + memoryDir + ")");
                break;
            case "FE": // CP N
                row = Registers.PC/16;
                column = Registers.PC%16;
                res = registers.A - Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println(res);
                registers.setFlagSign(res < 0);
                registers.setFlagZero(res == 0);
                registers.setPositiveFlag(res > 0);
                Registers.PC += 1;
                break;
            case "DD21":
                parametros = decodificarParametros();
                registers.IX = String.valueOf(parametros[0]);
                System.out.println("Ejecutado, IX = " + registers.IX);
                break;
            case "DD7E": // LD A, (IX + d)
                row = Registers.PC/16;
                column = Registers.PC%16;
                displacement = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer el desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer memoria
                System.out.println("Ejecutado: LD A, (IX + " + displacement + ")");
                Registers.PC += 1;
                break;
            case "DDBE": // CP (IX+D)
                row = Registers.PC/16;
                column = Registers.PC%16;
                displacement = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer el desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                res = registers.A - Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer memoria
                System.out.println("Ejecutado: CP (IX + " + displacement + ")");
                Registers.PC += 1;
                registers.setFlagSign(res < 0);
                registers.setFlagZero(res == 0);
                registers.setPositiveFlag(res > 0);
                break;
            case "DD66": //LD H, (IX + D)
                row = Registers.PC/16;
                column = Registers.PC%16;
                displacement = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer el desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                registers.H = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer memoria
                System.out.println("Ejecutado: LD H, (IX + " + displacement + ")");
                Registers.PC += 1;
                break;
            case "DD74": // LD (IX + d), H
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.H); // Escribir memoria
                System.out.println("Ejecutado: LD (IX + " + displacement + "), H");
                break;
            case "DD77": // LD (IX + d), A
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.A); // Escribir memoria
                System.out.println("Ejecutado: LD (IX + " + displacement + "), A");
                break;
            case "DD36": // LD (IX + d), N
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.H); // Escribir memoria
                System.out.println("Ejecutado: LD (IX + " + displacement + "), H");
                Registers.PC += 1;
                break;
            case "18": // JR d (salto relativo)
                // Leer el desplazamiento (8 bits)
                row = Registers.PC / 16;
                column = Registers.PC % 16;
                int offset = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer byte como hexadecimal
                Registers.PC += 1; // Incrementar PC para pasar al siguiente byte

                // Interpretar offset como número con signo
                if (offset > 127) { // Si el valor es mayor a 127, es negativo en complemento a 2
                    offset -= 256;
                }

                // Ajustar el PC al realizar el salto
                Registers.PC += offset;

                System.out.println("Ejecutado: JR " + offset + ", nueva dirección PC: " + Integer.toHexString(Registers.PC));
                break;
            case "00":
                System.out.println("NO OPERACIÓN");
                Registers.PC += 1;
                break;
            case "C3":
                parametros = decodificarParametros(); // Dirección de 16 bits
                memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                Registers.PC = memoryDir; // Salta a la dirección especificada
                System.out.println("Ejecutado: JP a dirección " + parametros[0]);
                break;
            case "C6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A += Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println("OPERACIÓN HECHA, ADD A, " + Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "D2":
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (!registers.getFlagCarry()) { // Verifica si el flag de carry no está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP NC a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP NC");
                }
                break;
            case "D3":
                row = Registers.PC/16;
                column = Registers.PC%16;
                newVal = Integer.parseInt(Z80App.memoria.m[row][column]);
                row = newVal/16;
                column = newVal%16;
                Z80App.memoria.m[row][column] = String.valueOf(registers.A);
                System.out.println("HECHO, OUT (" + newVal + "), A");
                Registers.PC += 1;
                break;
            case "D6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A -= Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println("OPERACIÓN HECHA, SUB " + Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "DA":
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (registers.getFlagCarry()) { // Verifica si el flag de carry está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP NC a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP NC");
                }
                break;
            case "DB":
                row = Registers.PC/16;
                column = Registers.PC%16;
                newVal = Integer.parseInt(Z80App.memoria.m[row][column]);
                row = newVal/16;
                column = newVal%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column]);
                System.out.println("HECHO, IN (" + newVal + "), A");
                Registers.PC += 1;
                break;
            case "E2":
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (!registers.getFlagParity()) { // Verifica si el flag de overflow no está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP PO a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP PO");
                }
                break;
            case "E6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A & Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "EA":
                parametros = decodificarParametros(); // Dirección de 16 bits
                if (registers.getFlagParity()) { // Verifica si el flag de overflow está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    System.out.println("Ejecutado: JP PE a dirección " + parametros[0]);
                } else {
                    System.out.println("Condición no cumplida: No se ejecuta JP PE");
                }
                break;
            case "EE":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A ^ Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "F6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A | Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "80":
                registers.A += registers.B;
                System.out.println("OPERACIÓN HECHA, ADD A, B");
                break;
            case "81":
                registers.A += registers.C;
                System.out.println("OPERACIÓN HECHA, ADD A, C");
                break;
            case "82":
                registers.A += registers.D;
                System.out.println("OPERACIÓN HECHA, ADD A, D");
                break;
            case "83":
                registers.A += registers.E;
                System.out.println("OPERACIÓN HECHA, ADD A, E");
                break;
            case "84":
                registers.A += registers.H;
                System.out.println("OPERACIÓN HECHA, ADD A, H");
                break;
            case "85":
                registers.A += registers.L;
                System.out.println("OPERACIÓN HECHA, ADD A, L");
                break;
            case "87":
                registers.A += registers.A;
                System.out.println("OPERACIÓN HECHA, ADD A, A");
                break;
            case "90":
                registers.A -= registers.B;
                System.out.println("OPERACIÓN HECHA, SUB B");
                break;
            case "91":
                registers.A -= registers.C;
                System.out.println("OPERACIÓN HECHA, SUB C");
                break;
            case "92":
                registers.A -= registers.D;
                System.out.println("OPERACIÓN HECHA, SUB D");
                break;
            case "93":
                registers.A -= registers.E;
                System.out.println("OPERACIÓN HECHA, SUB E");
                break;
            case "94":
                registers.A -= registers.H;
                System.out.println("OPERACIÓN HECHA, SUB H");
                break;
            case "95":
                registers.A -= registers.L;
                System.out.println("OPERACIÓN HECHA, SUB L");
                break;
            case "97":
                registers.A -= registers.A;
                System.out.println("OPERACIÓN HECHA, SUB A");
                break;
            case "A0":
                break;
            case "A1":
                break;
            case "A2":
                break;
            case "A3":
                break;
            case "A4":
                break;
            case "A5":
                break;
            case "A6":
                break;
            case "A7":
                break;
            case "A8":
                break;
            case "A9":
                break;
            case "AA":
                break;
            case "AB":
                break;
            case "AC":
                break;
            case "AD":
                break;
            case "AE":
                break;
            case "AF":
                break;
            case "B0":
                break;
            case "B1":
                break;
            case "B2":
                break;
            case "B3":
                break;
            case "B4":
                break;
            case "B5":
                break;
            case "B6":
                break;
            case "B7":
                break;
            case "B8":
                break;
            case "B9":
                break;
            case "BA":
                break;
            case "BB":
                break;
            case "BC":
                break;
            case "BD":
                break;
            case "BE":
                break;
            default:
                System.out.println("Instrucción no soportada: " + opcode);
        }
    }
}
