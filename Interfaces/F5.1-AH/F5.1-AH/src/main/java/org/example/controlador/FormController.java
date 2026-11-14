package org.example.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.dao.TareaDAO;
import org.example.model.Tarea;

public class FormController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtResponsable;
    @FXML private TextField txtUbicacion;
    @FXML private Slider sliderPorcentaje;
    @FXML private Label lblPorcentaje;
    @FXML private Label lblError;

    private Tarea tareaActual;

    @FXML
    public void initialize() {
        // Actualizar la etiqueta cuando se mueve el slider
        sliderPorcentaje.valueProperty().addListener((obs, oldVal, newVal) ->
                lblPorcentaje.setText(String.format("%.0f%%", newVal.doubleValue()))
        );
    }

    public void setTarea(Tarea tarea) {
        this.tareaActual = tarea;
        if (tarea != null) {
            lblTitulo.setText("Modificar Tarea");
            txtNombre.setText(tarea.getNombre());
            txtDescripcion.setText(tarea.getDescripcion());
            txtResponsable.setText(tarea.getResponsable());
            txtUbicacion.setText(tarea.getUbicacion());
            sliderPorcentaje.setValue(tarea.getPorcentaje());
        } else {
            lblTitulo.setText("Crear Nueva Tarea");
        }
    }

    @FXML
    private void onGuardarClick(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String responsable = txtResponsable.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        double porcentaje = Math.round(sliderPorcentaje.getValue());

        // Validación de campos obligatorios (Ubicación es opcional)
        if (nombre.isEmpty() || descripcion.isEmpty() || responsable.isEmpty()) {
            lblError.setText("Nombre, descripción y responsable son obligatorios.");
            return;
        }

        if (tareaActual == null) {
            // Crear nueva
            Tarea nueva = new Tarea(nombre, descripcion, porcentaje, responsable, ubicacion);
            TareaDAO.getInstancia().anadirTarea(nueva);
        } else {
            // Actualizar existente
            tareaActual.setNombre(nombre);
            tareaActual.setDescripcion(descripcion);
            tareaActual.setResponsable(responsable);
            tareaActual.setUbicacion(ubicacion);
            tareaActual.setPorcentaje(porcentaje);
        }

        cerrarVentana(event);
    }

    @FXML
    private void onCancelarClick(ActionEvent event) {
        cerrarVentana(event);
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}