package com.example.SimuladorZ80;
// Hacemos los importes necesarios de JavaFX
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;

public class SimuladorZ80UI extends Application {
    // Damos un valor hardcodeado al tamaño de la interfaz gráfica.
    static final int width = 1080;
    static final int height = 720;

    public static void main(String[] args) {
        // Argumento main en el que se ejecutará todo.
        launch(args);
    }
    // El override es necesario porque el mismo método debe de ser reemplazado.
    @Override
    public void start(Stage primaryStage) {
        // Definimos la ventana que se va a abrir cuando inicie el programa
        Pane root = new Pane();
        // Le damos el tamaño a la ventana que predefinimos
        root.setPrefSize(width, height);
        // Le ponemos un fondo negro a la ventana creada.
        root.setBackground(Background.fill(Color.BLACK));

        // Creamos el título como un objeto de JavaFX.
        Label titulo = new Label("SIMZ80");
        // Creamos todos los botones que estarán presentes en esta escena en particular.
        Button insertar_Programa = new Button("Insertar programa a memoria");
        Button manejar_Memoria = new Button("Manejo manual de memoria");
        Button desensamblar = new Button("Desensamblar código");
        Button ejecucion = new Button("Ejecutar código");

        // Para el fácil manejo y organización de los botones, los metemos a una ArrayList.
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(insertar_Programa);
        buttons.add(manejar_Memoria);
        buttons.add(desensamblar);
        buttons.add(ejecucion);

        //Mandamos a los botones al método de inicializar botones.
        initializeButtons(buttons, root);

        //Le damos el formato al título del programa.
        titulo.setTextFill(Color.PINK);
        titulo.setBorder(Border.stroke(Color.WHITE));
        titulo.setPrefWidth(300);
        titulo.setAlignment(Pos.CENTER);
        titulo.setLayoutX((root.getPrefWidth()-titulo.getPrefWidth())/2);
        titulo.setLayoutY(50);
        titulo.setFont(new Font("Trebuchet MS", 70));



        root.getChildren().add(titulo);
        for(Button btt: buttons){
            root.getChildren().add(btt);
        }

        primaryStage.setTitle("SIMULADOR Z80");
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();


    }

    public static void initializeButtons(ArrayList<Button> buttons, Pane root) {
        //Definimos una altura arbitraria para distribuir los botones en cuestión.
        double arb_height = (double) (height - 600) / 5;

        for (Button btt: buttons){
            btt.setBackground(Background.fill(Color.PINK));
            btt.setPadding(new Insets(10, 10, 10, 10));
            btt.setFont(new Font("Impact", 20));
            btt.setPrefSize(300, 55);
            btt.setLayoutX((root.getPrefWidth()-btt.getPrefWidth())/2);
            btt.setLayoutY(200 + arb_height);
            arb_height += (double)(height - 130)/5;
        }

    }

}


