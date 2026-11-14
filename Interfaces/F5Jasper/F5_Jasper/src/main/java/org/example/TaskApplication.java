package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.AppController;
import org.example.dao.TareaDAO;
import org.example.service.ReportService;

public class TaskApplication extends Application {
    public TaskApplication() {
    }

    @Override
    public void start(Stage stage) {
        var controller = new AppController(stage, new TareaDAO(), new ReportService());
        stage.setTitle("Gestor de tareas");
        stage.setMinWidth(820);
        stage.setMinHeight(560);
        stage.setScene(new Scene(controller.loginView(), 900, 620));
        stage.show();
    }
}
