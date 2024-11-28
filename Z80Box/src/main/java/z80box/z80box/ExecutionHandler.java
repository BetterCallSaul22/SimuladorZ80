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
import javafx.stage.Stage;

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

    public static int currentPage = MemoryHandlingController.currentPage;

    Registers registers = new Registers();

    @FXML
    Label registerAVal, registerBVal, registerCVal, registerDVal, registerEVal, registerHVal, registerLVal,
    registerIXVal, registerIYVal, registerSPVal, instructionDisplay, sFlag, zFlag, hFlag, pFlag, nFlag, cFlag;

    @FXML
    TextField PCDisplay;

    public static String execMode;

    @FXML
    // Metodo que nos permitirá regresar al menú principal de ser necesario.
    public void keyEventHandler(KeyEvent event) throws IOException, InterruptedException {
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
        }else if(Objects.equals(execMode, "F") && event.getCode() == KeyCode.ENTER && !Objects.equals(fetchOpcodeRevise(), "76")){
            do{
                ejecutarCiclo();
                registerAVal.setText(Integer.toHexString(registers.A));
                registerBVal.setText(Integer.toHexString(registers.B));
                registerCVal.setText(Integer.toHexString(registers.C));
                registerDVal.setText(Integer.toHexString(registers.D));
                registerEVal.setText(Integer.toHexString(registers.E));
                registerHVal.setText(Integer.toHexString(registers.H));
                registerLVal.setText(Integer.toHexString(registers.L));
                registerIXVal.setText(registers.IX);
                registerIYVal.setText(registers.IY);
                registerSPVal.setText(Integer.toHexString(Registers.SP));
                sFlag.setText(registers.getFlagSign() ? "1" : "0");
                zFlag.setText(registers.getFlagZero() ? "1" : "0");
                hFlag.setText(registers.getFlagHalfCarry() ? "1" : "0");
                pFlag.setText(registers.getFlagParity() ? "1" : "0");
                nFlag.setText(registers.getFlagSubtract() ? "1" :"0");
                sFlag.setText(registers.getFlagSign() ? "1" : "0");
                cFlag.setText(registers.getFlagCarry() ? "1" : "0");

                memoryVisualizer.getChildren().clear();
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

            }while(!Objects.equals(fetchOpcodeRevise(), "76"));
            instructionDisplay.setText("HALT");
        }else if(event.getCode() == KeyCode.ENTER && !Objects.equals(fetchOpcodeRevise(), "76") && Objects.equals(execMode, "S")){
            ejecutarCiclo();
            registerAVal.setText(Integer.toHexString(registers.A));
            registerBVal.setText(Integer.toHexString(registers.B));
            registerCVal.setText(Integer.toHexString(registers.C));
            registerDVal.setText(Integer.toHexString(registers.D));
            registerEVal.setText(Integer.toHexString(registers.E));
            registerHVal.setText(Integer.toHexString(registers.H));
            registerLVal.setText(Integer.toHexString(registers.L));
            registerIXVal.setText(registers.IX);
            registerIYVal.setText(registers.IY);
            registerSPVal.setText(Integer.toHexString(Registers.SP));
            sFlag.setText(registers.getFlagSign() ? "1" : "0");
            zFlag.setText(registers.getFlagZero() ? "1" : "0");
            hFlag.setText(registers.getFlagHalfCarry() ? "1" : "0");
            pFlag.setText(registers.getFlagParity() ? "1" : "0");
            nFlag.setText(registers.getFlagSubtract() ? "1" :"0");
            sFlag.setText(registers.getFlagSign() ? "1" : "0");
            cFlag.setText(registers.getFlagCarry() ? "1" : "0");

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
            instructionDisplay.setText("HALT");
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
        parametros[0] = Integer.parseInt(z, 16); // Combinar ambos bytes en una dirección de 16 bits
        System.out.println("PARAMETRO DECODIFICADO: " + parametros[0]);
        return parametros;
    }

    private int[] decodificarParametrosEsp() {
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

        PCDisplay.setPromptText(String.format("%04X", Registers.PC));

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
        String s;
        switch (opcode) {
            case "3E": // LD A, n (inmediato de 8 bits)
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en A
                Registers.PC += 1;
                instructionDisplay.setText("LD A, " + Z80App.memoria.m[row][column]);
                break;
            case "7E": // LD A, (N lugar de memoria)
                parametros = decodificarParametros();
                registers.A = parametros[0]; // Cargar valor en A
                instructionDisplay.setText("LD A, " + registers.A);
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
                instructionDisplay.setText("CP A, " + res);
                break;
            case "4F": // LD C, A (inmediato de 8 bits)
                registers.C = registers.A; // Cargar valor en A
                instructionDisplay.setText("LD C, A");
                break;
            case "0D": // DEC C
                registers.C--;
                instructionDisplay.setText("DEC C");
                break;
            case "51": // LD D, C (inmediato de 8 bits)
                registers.D = registers.C; // Cargar valor en A
                instructionDisplay.setText("LD D, C");
                break;
            case "06": // LD B, n (inmediato de 8 bits)
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.B = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en A
                Registers.PC += 1;
                instructionDisplay.setText("LD B, " + Z80App.memoria.m[row][column]);
                break;
            case "1E": // LD E, n (inmediato de 8 bits)
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.E = Integer.parseInt(Z80App.memoria.m[row][column]); // Cargar valor en A
                Registers.PC += 1;
                instructionDisplay.setText("LD E, " + registers.E);
                break;
            case "04": // INC B
                registers.B++;
                instructionDisplay.setText("B = " + registers.B);
                break;
            case "15": // DEC D
                registers.D --;
                instructionDisplay.setText("DEC D");
                break;
            case "7A": // LD A, D (inmediato de 8 bits)
                registers.A = registers.D; // Cargar valor en A
                instructionDisplay.setText("LD A, D");
                break;
            case "7B": // LD A, E (inmediato de 8 bits)
                registers.A = registers.E; // Cargar valor en A
                instructionDisplay.setText("LD A, E");
                break;
            case "79": // LD A, C (inmediato de 8 bits)
                registers.A = registers.C; // Cargar valor en A
                instructionDisplay.setText("LD A, C");
                break;
            case "FA": // JP M, nn (salto si flag S está establecido)
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (registers.getFlagSign()) { // Verifica si el flag de signo está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP M " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP M");
                }
                break;
            case "F2": // JP P, nn (salto si flag S no está establecido)
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (!registers.getFlagSign()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP P " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP P");
                }
                break;
            case "C2": // JP NZ, nn (salto si flag Z no está establecido)
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (!registers.getFlagZero()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP NZ " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP NZ");
                }
                break;
            case "CA": // JP Z, nn (salto si flag Z está establecido)
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (registers.getFlagZero()) { // Verifica si el flag de signo está inactivo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP Z " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP Z");
                }
                break;
            case "DD23": //Incrementar IX
                System.out.println(registers.IX);
                registers.IX = String.valueOf(Integer.parseInt(registers.IX) + 1);
                System.out.println(registers.IX);
                instructionDisplay.setText("INC IX");
                break;
            case "3A": // LD A, (NN)
                parametros = decodificarParametrosEsp();
                memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                row = memoryDir/16;
                column = memoryDir%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column]);
                instructionDisplay.setText("LD A, (" + memoryDir + ")");
                break;
            case "FE": // CP N
                row = Registers.PC/16;
                column = Registers.PC%16;
                res = registers.A - Integer.parseInt(Z80App.memoria.m[row][column]);
                instructionDisplay.setText("CP " + String.valueOf(res));
                registers.setFlagSign(res < 0);
                registers.setFlagZero(res == 0);
                registers.setPositiveFlag(res > 0);
                Registers.PC += 1;
                break;
            case "DD21":
                parametros = decodificarParametrosEsp();
                registers.IX = String.valueOf(parametros[0]);
                instructionDisplay.setText("IX = " + registers.IX);
                break;
            case "DD7E": // LD A, (IX + d)
                row = Registers.PC/16;
                column = Registers.PC%16;
                displacement = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer el desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Leer memoria
                instructionDisplay.setText("LD A, (IX + " + displacement + ")");
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
                System.out.println("CP (IX + " + displacement + ")");
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
                instructionDisplay.setText("LD H, (IX + " + displacement + ")");
                Registers.PC += 1;
                break;
            case "DD74": // LD (IX + d), H
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.H); // Escribir memoria
                instructionDisplay.setText("LD (IX + " + displacement + "), H");
                break;
            case "DD77": // LD (IX + d), A
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.A); // Escribir memoria
                instructionDisplay.setText("LD (IX + " + displacement + "), A");
                break;
            case "DD36": // LD (IX + d), N
                displacement = Integer.parseInt(fetchOpcode(), 16); // Leer desplazamiento d
                effectiveAddress = Integer.parseInt(registers.IX, 16) + displacement; // IX + d
                row = effectiveAddress / 16;
                column = effectiveAddress % 16;
                Z80App.memoria.m[row][column] = String.format("%02X", registers.H); // Escribir memoria
                instructionDisplay.setText("LD (IX + " + displacement + "), H");
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

                instructionDisplay.setText("JR " + offset);
                break;
            case "00":
                instructionDisplay.setText("NO OPERACIÓN");
                Registers.PC += 1;
                break;
            case "C3":
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                Registers.PC = memoryDir; // Salta a la dirección especificada
                instructionDisplay.setText("JP " + parametros[0]);
                break;
            case "C6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A += Integer.parseInt(Z80App.memoria.m[row][column]);
                instructionDisplay.setText("ADD A, " + Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "D2":
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (!registers.getFlagCarry()) { // Verifica si el flag de carry no está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP NC " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP NC");
                }
                break;
            case "D3":
                row = Registers.PC/16;
                column = Registers.PC%16;
                newVal = Integer.parseInt(Z80App.memoria.m[row][column]);
                row = newVal/16;
                column = newVal%16;
                Z80App.memoria.m[row][column] = String.valueOf(registers.A);
                instructionDisplay.setText("OUT (" + newVal + "), A");
                Registers.PC += 1;
                break;
            case "D6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A -= Integer.parseInt(Z80App.memoria.m[row][column]);
                instructionDisplay.setText("SUB " + Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                break;
            case "DA":
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (registers.getFlagCarry()) { // Verifica si el flag de carry está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP NC " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP NC");
                }
                break;
            case "DB":
                row = Registers.PC/16;
                column = Registers.PC%16;
                newVal = Integer.parseInt(Z80App.memoria.m[row][column]);
                row = newVal/16;
                column = newVal%16;
                registers.A = Integer.parseInt(Z80App.memoria.m[row][column]);
                instructionDisplay.setText("IN (" + newVal + "), A");
                Registers.PC += 1;
                break;
            case "E2":
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (!registers.getFlagParity()) { // Verifica si el flag de overflow no está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP PO " + parametros[0]);
                } else {
                    instructionDisplay.setText("No JP PO");
                }
                break;
            case "E6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A & Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                instructionDisplay.setText("AND A");
                break;
            case "EA":
                parametros = decodificarParametrosEsp(); // Dirección de 16 bits
                if (registers.getFlagParity()) { // Verifica si el flag de overflow está activo
                    memoryDir = Miscellaneous.calculateOverallStartValue(String.valueOf(parametros[0]));
                    Registers.PC = memoryDir; // Salta a la dirección especificada
                    instructionDisplay.setText("JP PE " + parametros[0]);
                } else {
                    System.out.println("No JP PE");
                }
                break;
            case "EE":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A ^ Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                instructionDisplay.setText("XOR A");
                break;
            case "F6":
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.A = registers.A | Integer.parseInt(Z80App.memoria.m[row][column]);
                Registers.PC += 1;
                instructionDisplay.setText("OR A");
                break;
            case "80":
                registers.A += registers.B;
                instructionDisplay.setText("ADD A, B");
                break;
            case "81":
                registers.A += registers.C;
                instructionDisplay.setText("ADD A, C");
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
            case "C1": // POP BC
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.C = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.B = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                instructionDisplay.setText("POP BC");
                break;
            case "C5": // PUSH BC
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.B);
                Registers.SP -= 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.C);
                Registers.SP -= 1;
                instructionDisplay.setText("PUSH BC");
                break;
            case "D1": // POP DE
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.E = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.D = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                instructionDisplay.setText("POP DE");
                break;
            case "D5": // PUSH DE
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.D);
                Registers.SP -= 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.E);
                Registers.SP -= 1;
                instructionDisplay.setText("PUSH DE");
                break;
            case "E1": // POP HL
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.L = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                registers.H = Integer.parseInt(Z80App.memoria.m[row][column], 16);
                Z80App.memoria.m[row][column] = "00";
                instructionDisplay.setText("POP HL");
                break;
            case "E5": // PUSH HL
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.H);
                Registers.SP -= 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = Integer.toHexString(registers.L);
                Registers.SP -= 1;
                instructionDisplay.setText("PUSH HL");
                break;
            case "C4": // CALL NZ, NN
                if(!registers.getFlagZero()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL " + parametros[0]);
                }
                break;
            case "CD": // CALL NN
                parametros = decodificarParametros();
                s = String.format("%04X", Registers.PC);
                Registers.PC = parametros[0];
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = s.substring(0, 2);
                Registers.SP -= 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                Z80App.memoria.m[row][column] = s.substring(2, 4);
                Registers.SP -= 1;
                instructionDisplay.setText("CALL " + parametros[0]);
                break;
            case "CC": // CALL Z, NN
                if(registers.getFlagZero()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL " + parametros[0]);
                }
                break;
            case "D4": // CALL NC, NN
                if(!registers.getFlagCarry()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL " + parametros[0]);
                }
                break;
            case "DC": // CALL C, NN
                if(registers.getFlagCarry()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL " + parametros[0]);
                }
                break;
            case "C9": // RET
                s = "";
                row = Registers.SP/16;
                column = Registers.SP%16;
                s += Z80App.memoria.m[row][column];
                Z80App.memoria.m[row][column] = "00";
                Registers.SP += 1;
                row = Registers.SP/16;
                column = Registers.SP%16;
                s += Z80App.memoria.m[row][column];
                Z80App.memoria.m[row][column] = "00";
                Registers.SP += 1;
                Registers.PC = Integer.parseInt(s, 16);
                instructionDisplay.setText("RET");
                break;
            case "C0": // RET NZ
                if(!registers.getFlagZero()){
                    s = "";
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    Registers.PC = Integer.parseInt(s, 16);
                    instructionDisplay.setText("RET NZ");
                }
                break;
            case "C8": // RET Z
                if(registers.getFlagZero()){
                    s = "";
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    Registers.PC = Integer.parseInt(s, 16);
                    instructionDisplay.setText("RET NZ");
                }
                break;
            case "D0": // RET NC
                if(!registers.getFlagCarry()){
                    s = "";
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    Registers.PC = Integer.parseInt(s, 16);
                    instructionDisplay.setText("RET NZ");
                }
                break;
            case "D8": // RET C
                if(registers.getFlagCarry()){
                    s = "";
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    Registers.PC = Integer.parseInt(s, 16);
                    instructionDisplay.setText("RET NZ");
                }
                break;
            case "E0": // RET PO
                if(!registers.getFlagParity()){
                    s = "";
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    s += Z80App.memoria.m[row][column];
                    Z80App.memoria.m[row][column] = "00";
                    Registers.SP += 1;
                    Registers.PC = Integer.parseInt(s, 16);
                    instructionDisplay.setText("RET NZ");
                }
                break;
            case "E4": // CALL PO, NN
                if(!registers.getFlagParity()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL PO" + parametros[0]);
                }
                break;
            case "EC": // CALL PE, NN
                if(registers.getFlagParity()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL PE" + parametros[0]);
                }
                break;
            case "F4": // CALL P, NN
                if(!registers.getFlagSign()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL P" + parametros[0]);
                }
                break;
            case "FC": // CALL M, NN
                if(registers.getFlagSign()){
                    parametros = decodificarParametros();
                    s = String.format("%04X", Registers.PC);
                    Registers.PC = parametros[0];
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(0, 2);
                    Registers.SP -= 1;
                    row = Registers.SP/16;
                    column = Registers.SP%16;
                    Z80App.memoria.m[row][column] = s.substring(2, 4);
                    Registers.SP -= 1;
                    instructionDisplay.setText("CALL M" + parametros[0]);
                }
                break;
            case "0E": // LD C, N
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.C = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en C
                Registers.PC += 1;
                instructionDisplay.setText("LD C, " + Z80App.memoria.m[row][column]);
                break;
            case "16": // LD D, N
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.D = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en D
                Registers.PC += 1;
                instructionDisplay.setText("LD D, " + Z80App.memoria.m[row][column]);
                break;
            case "26": // LD H, N
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.H = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en H
                Registers.PC += 1;
                instructionDisplay.setText("LD H, " + Z80App.memoria.m[row][column]);
                break;
            case "2E": // LD L, N
                row = Registers.PC/16;
                column = Registers.PC%16;
                registers.L = Integer.parseInt(Z80App.memoria.m[row][column], 16); // Cargar valor en L
                Registers.PC += 1;
                instructionDisplay.setText("LD L, " + Z80App.memoria.m[row][column]);
                break;
            case "48": // LD C, B (inmediato de 8 bits)
                registers.C = registers.B; // Cargar valor en A
                instructionDisplay.setText("LD C, B");
                break;
            case "57": // LD D, A (inmediato de 8 bits)
                registers.D = registers.A; // Cargar valor en A
                instructionDisplay.setText("LD D, A");
                break;
            case "62": // LD H, D (inmediato de 8 bits)
                registers.H = registers.D; // Cargar valor en A
                instructionDisplay.setText("LD H, D");
                break;
            case "EB": // EX DE, HL
                int temp = registers.D;
                int temp2 = registers.E;
                registers.D = registers.H;
                registers.E = registers.L;
                registers.H = temp;
                registers.L = temp2;
                instructionDisplay.setText("EX DE, HL");
            default:
                System.out.println("Instrucción no soportada: " + opcode);
        }
    }

    public void updatePC(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            try{
                String s = PCDisplay.getText();
                int newPC = Integer.parseInt(s);
                if(newPC >= 32768 || newPC <= -1){
                    throw new InvalidHexDirectionException();
                }
                Registers.PC = newPC;
                PCDisplay.setPromptText(String.format("%04X", Registers.PC));
            }catch(InvalidHexDirectionException | NumberFormatException ignored){

            } finally{
                PCDisplay.clear();
                memoryVisualizer.requestFocus();
            }
        }
    }
}
