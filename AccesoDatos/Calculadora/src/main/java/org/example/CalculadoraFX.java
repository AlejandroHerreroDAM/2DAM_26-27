package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculadoraFX extends Application {

    private TextField pantalla;

    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        HBox topBox = new HBox(10);
        pantalla = new TextField();
        pantalla.setEditable(false);
        pantalla.setAlignment(Pos.CENTER_RIGHT);
        pantalla.setFont(Font.font(24));
        pantalla.setPrefHeight(60);
        HBox.setHgrow(pantalla, Priority.ALWAYS);

        Button btnIgual = new Button("=");
        btnIgual.setPrefSize(60, 60);
        btnIgual.setMinSize(60, 60);
        btnIgual.setFont(Font.font(24));
        btnIgual.setPadding(new Insets(0));

        configurarAccionBoton(btnIgual, "=");

        topBox.getChildren().addAll(pantalla, btnIgual);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        String[][] teclas = {
                {"C", "( )", "%", "<-"},
                {"7", "8", "9", "/"},
                {"4", "5", "6", "X"},
                {"1", "2", "3", "-"},
                {"+/-", "0", ",", "+"}
        };

        for (int fila = 0; fila < teclas.length; fila++) {
            for (int col = 0; col < teclas[fila].length; col++) {
                String textoBoton = teclas[fila][col];
                Button boton = new Button(textoBoton);
                boton.setPrefSize(70, 70);
                boton.setFont(Font.font(18));

                configurarAccionBoton(boton, textoBoton);

                grid.add(boton, col, fila);
            }
        }

        root.getChildren().addAll(topBox, grid);

        Scene scene = new Scene(root, 360, 520);
        primaryStage.setTitle("CALCULADORA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarAccionBoton(Button boton, String texto) {
        boton.setOnAction(e -> {
            String contenidoActual = pantalla.getText();

            switch (texto) {
                case "C":
                    pantalla.clear();
                    break;
                case "<-":
                    if (!contenidoActual.isEmpty()) {
                        pantalla.setText(contenidoActual.substring(0, contenidoActual.length() - 1));
                    }
                    break;
                case "+/-":
                    if (!contenidoActual.isEmpty() && !contenidoActual.equals("0")) {
                        if (contenidoActual.startsWith("-")) {
                            pantalla.setText(contenidoActual.substring(1));
                        } else {
                            pantalla.setText("-" + contenidoActual);
                        }
                    }
                    break;
                default:
                    pantalla.setText(contenidoActual + texto);
                    break;
            }
        });
    }
}