package org.example.model;

import javafx.beans.property.*;

public class Tarea {
    private final StringProperty nombre;
    private final StringProperty descripcion;
    private final DoubleProperty porcentaje;
    private final StringProperty responsable;
    private final StringProperty ubicacion; // Añadido
    private final StringProperty estado;

    public Tarea(String nombre, String descripcion, double porcentaje, String responsable, String ubicacion) {
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.porcentaje = new SimpleDoubleProperty(porcentaje);
        this.responsable = new SimpleStringProperty(responsable);
        this.ubicacion = new SimpleStringProperty(ubicacion == null ? "" : ubicacion);
        this.estado = new SimpleStringProperty(calcularEstado(porcentaje));

        this.porcentaje.addListener((obs, oldVal, newVal) ->
                this.estado.set(calcularEstado(newVal.doubleValue()))
        );
    }

    private String calcularEstado(double p) {
        if (p <= 0) return "Iniciada";
        if (p >= 100) return "Finalizada";
        return "En curso";
    }

    // Properties
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty descripcionProperty() { return descripcion; }
    public DoubleProperty porcentajeProperty() { return porcentaje; }
    public StringProperty responsableProperty() { return responsable; }
    public StringProperty ubicacionProperty() { return ubicacion; }
    public StringProperty estadoProperty() { return estado; }

    // Getters y Setters
    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public double getPorcentaje() { return porcentaje.get(); }
    public void setPorcentaje(double porcentaje) { this.porcentaje.set(porcentaje); }
    public String getResponsable() { return responsable.get(); }
    public void setResponsable(String responsable) { this.responsable.set(responsable); }
    public String getUbicacion() { return ubicacion.get(); }
    public void setUbicacion(String ubicacion) { this.ubicacion.set(ubicacion); }
    public String getEstado() { return estado.get(); }
}