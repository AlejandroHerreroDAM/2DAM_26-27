package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import org.example.dao.TareaDAO;
import org.example.model.Tarea;
import org.example.service.ReportService;

import java.util.List;

public class AppController {
    private final Stage stage;
    private final TareaDAO dao;
    private final ReportService reports;
    private final List<Tarea> tareas;
    private final TableView<Tarea> tabla = new TableView<>();

    public AppController(Stage stage, TareaDAO dao, ReportService reports) {
        this.stage = stage;
        this.dao = dao;
        this.reports = reports;
        this.tareas = dao.listar();
    }

    public Parent loginView() {

        var usuario = new TextField();
        usuario.setPromptText("Usuario");

        var password = new PasswordField();
        password.setPromptText("Contraseña");

        var mensaje = new Label();
        var entrar = new Button("Acceder");

        entrar.setDefaultButton(true);

        entrar.setOnAction(e -> {
            if (usuario.getText().isBlank() || password.getText().isBlank()) {
                mensaje.setText("Introduce usuario y contraseña.");
                mensaje.setStyle("-fx-text-fill: #b91c1c;");
            } else {
                stage.setScene(new Scene(mainView(), 900, 620));
            }
        });

        var titulo = new Label("GESTOR DE TAREAS - Iniciar sesión");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        var formulario = new VBox(14, usuario, password, entrar);

        formulario.setAlignment(Pos.CENTER);
        formulario.setMaxWidth(420);
        usuario.setMaxWidth(Double.MAX_VALUE);
        password.setMaxWidth(Double.MAX_VALUE);
        entrar.setMaxWidth(Double.MAX_VALUE);

        var root = new VBox(18, titulo, formulario, mensaje);

        root.setAlignment(Pos.CENTER);
        root.setFillWidth(true);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f7f9fc;");

        return root;
    }

    private Parent mainView() {

        configurarTabla();

        var titulo = new Label("Tareas del equipo");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        var crear = new Button("Nueva tarea");
        var editar = new Button("Modificar");
        var estadisticas = new Button("Estadísticas");
        var informe = new Button("Generar informe");

        crear.setOnAction(e -> mostrarFormulario(null));
        editar.setOnAction(e -> {
            var seleccion = tabla.getSelectionModel().getSelectedItem();
            if (seleccion == null) aviso("Selecciona una tarea para modificar.");
            else mostrarFormulario(seleccion);
        });

        estadisticas.setOnAction(e -> mostrarEstadisticas());

        informe.setOnAction(e -> {
            try {
                reports.mostrarInforme(tareas);
            } catch (JRException ex) {
                aviso("No se pudo generar el informe: " + ex.getMessage());
            }
        });

        var acciones = new HBox(10, crear, editar, estadisticas, informe);
        acciones.setAlignment(Pos.CENTER_LEFT);

        var root = new BorderPane();

        root.setTop(new VBox(12, titulo, acciones));
        root.setCenter(tabla);
        root.setPadding(new Insets(24));
        BorderPane.setMargin(tabla, new Insets(18, 0, 0, 0));
        root.setStyle("-fx-background-color: #f7f9fc;");

        return root;
    }

    private void configurarTabla() {

        tabla.getColumns().clear();
        tabla.setPlaceholder(new Label("No hay tareas. Pulsa «Nueva tarea» para comenzar."));
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.getColumns().addAll(col("Tarea", Tarea::getNombre), col("Responsable", Tarea::getResponsable),
                col("Avance", t -> t.getPorcentaje() + "%"), col("Estado", Tarea::getEstado),
                col("Ubicación", t -> t.getUbicacion().isBlank() ? "-" : t.getUbicacion()));
        tabla.setItems(FXCollections.observableArrayList(tareas));

    }

    private <T> TableColumn<Tarea, String> col(String title, java.util.function.Function<Tarea, String> value) {
        var c = new TableColumn<Tarea, String>(title);
        c.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue())));

        return c;
    }

    private void mostrarFormulario(Tarea original) {
        var nombre = new TextField(original == null ? "" : original.getNombre());
        var descripcion = new TextArea(original == null ? "" : original.getDescripcion());
        descripcion.setPrefRowCount(3);
        var porcentaje = new Spinner<Integer>(0, 100, original == null ? 0 : original.getPorcentaje());
        var responsable = new TextField(original == null ? "" : original.getResponsable());
        var ubicacion = new TextField(original == null ? "" : original.getUbicacion());
        var error = new Label();
        var guardar = new Button("Guardar");
        guardar.setOnAction(e -> {
            if (nombre.getText().isBlank() || descripcion.getText().isBlank() || responsable.getText().isBlank()) {
                error.setText("Nombre, descripción y responsable son obligatorios.");
                error.setStyle("-fx-text-fill: #b91c1c;");
                return;
            }
            if (original == null) tareas.add(new Tarea(nombre.getText().trim(), descripcion.getText().trim(),
                    porcentaje.getValue(), responsable.getText().trim(), ubicacion.getText().trim()));
            else {
                original.setNombre(nombre.getText().trim());
                original.setDescripcion(descripcion.getText().trim());
                original.setPorcentaje(porcentaje.getValue());
                original.setResponsable(responsable.getText().trim());
                original.setUbicacion(ubicacion.getText().trim());
            }
            try {
                dao.guardar(tareas);
            } catch (IllegalStateException ex) {
                aviso("Error al guardar las tareas: " + ex.getMessage());
                return;
            }
            stage.setScene(new Scene(mainView(), 900, 620));
        });

        var volver = new Button("Cancelar");
        volver.setOnAction(e -> stage.setScene(new Scene(mainView(), 900, 620)));

        var form = new GridPane();

        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Nombre:"), nombre);
        form.addRow(1, new Label("Descripción:"), descripcion);
        form.addRow(2, new Label("Avance:"), porcentaje);
        form.addRow(3, new Label("Responsable:"), responsable);
        form.addRow(4, new Label("Ubicación:"), ubicacion);

        GridPane.setHgrow(nombre, Priority.ALWAYS);
        GridPane.setHgrow(descripcion, Priority.ALWAYS);
        GridPane.setHgrow(responsable, Priority.ALWAYS);
        GridPane.setHgrow(ubicacion, Priority.ALWAYS);

        var root = new VBox(18, new Label(original == null ? "Nueva tarea" : "Modificar tarea"),
                form, new HBox(10, guardar, volver), error);

        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f7f9fc;");
        stage.setScene(new Scene(root, 900, 620));
    }

    private void mostrarEstadisticas() {

        long iniciadas = tareas.stream().filter(t -> t.getPorcentaje() == 0).count();
        long curso = tareas.stream().filter(t -> t.getPorcentaje() > 0 && t.getPorcentaje() < 100).count();
        long finalizadas = tareas.stream().filter(t -> t.getPorcentaje() >= 100).count();
        var dialog = new Alert(Alert.AlertType.INFORMATION);

        dialog.setTitle("Estadísticas");
        dialog.setHeaderText("Resumen del trabajo");
        dialog.setContentText("Total de tareas: " + tareas.size() + "\nIniciadas: " + iniciadas
                + "\nEn curso: " + curso + "\nFinalizadas: " + finalizadas);
        dialog.showAndWait();

    }

    private void aviso(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).showAndWait();
    }
}
