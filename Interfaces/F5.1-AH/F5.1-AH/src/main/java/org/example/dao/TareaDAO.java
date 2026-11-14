package org.example.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.model.Tarea;

public class TareaDAO {
    // Patrón Singleton para tener la misma lista en todas las pantallas
    private static TareaDAO instancia;
    private final ObservableList<Tarea> listaTareas;

    private TareaDAO() {
        listaTareas = FXCollections.observableArrayList();
        // Añadimos datos de prueba con los 5 parámetros
        listaTareas.add(new Tarea("Diseñar UI", "Bocetos en Figma", 100, "Cristopher", "Oficina"));
        listaTareas.add(new Tarea("Implementar Login", "Pantalla de acceso JavaFX", 50, "Alejandro", "Remoto"));
        listaTareas.add(new Tarea("Generar Informes", "Plantillas JasperReports", 0, "Alejandro", "Remoto"));
    }

    public static TareaDAO getInstancia() {
        if (instancia == null) {
            instancia = new TareaDAO();
        }
        return instancia;
    }

    public ObservableList<Tarea> getTareas() {
        return listaTareas;
    }

    public void anadirTarea(Tarea t) {
        listaTareas.add(t);
    }
}