package org.example.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.HelloFX;
import org.example.dao.TareaDAO;
import org.example.model.Tarea;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Tarea> tablaTareas;
    @FXML private TableColumn<Tarea, String> colNombre;
    @FXML private TableColumn<Tarea, String> colResponsable;
    @FXML private TableColumn<Tarea, Number> colPorcentaje;
    @FXML private TableColumn<Tarea, String> colEstado;
    @FXML private TableColumn<Tarea, String> colUbicacion;

    @FXML
    public void initialize() {
        // Vincular columnas con las propiedades de Tarea
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colResponsable.setCellValueFactory(cellData -> cellData.getValue().responsableProperty());
        colPorcentaje.setCellValueFactory(cellData -> cellData.getValue().porcentajeProperty());
        colEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        colUbicacion.setCellValueFactory(cellData -> cellData.getValue().ubicacionProperty());

        // Cargar datos del DAO
        tablaTareas.setItems(TareaDAO.getInstancia().getTareas());
    }

    @FXML
    private void onCrearClick(ActionEvent event) {
        abrirFormulario(null);
    }

    @FXML
    private void onModificarClick(ActionEvent event) {
        Tarea seleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selección requerida", "Por favor, selecciona una tarea de la tabla para modificar.");
            return;
        }
        abrirFormulario(seleccionada);
    }

    private void abrirFormulario(Tarea tareaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloFX.class.getResource("/vistas/form.fxml"));
            Scene scene = new Scene(loader.load(), 450, 480);

            // Pasamos la tarea al controlador del formulario (null si es nueva)
            FormController controller = loader.getController();
            controller.setTarea(tareaEditar);

            Stage stage = new Stage();
            stage.setTitle(tareaEditar == null ? "Nueva Tarea" : "Modificar Tarea");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta cerrar
            stage.setResizable(false);
            stage.showAndWait();

            // Refrescamos la tabla por si se modificó algún valor existente
            tablaTareas.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEstadisticasClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloFX.class.getResource("/vistas/stats.fxml"));
            Scene scene = new Scene(loader.load(), 600, 450);
            Stage stage = new Stage();
            stage.setTitle("Estadísticas de Tareas");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onInformeClick(ActionEvent event) {
        // Lo implementaremos en el último paso con JasperReports
        System.out.println("Generando informe JasperReports...");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}