package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Cargaremos la vista de login (que crearemos en el siguiente paso)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloFX.class.getResource("/vistas/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        stage.setTitle("Gestión de Tareas");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}