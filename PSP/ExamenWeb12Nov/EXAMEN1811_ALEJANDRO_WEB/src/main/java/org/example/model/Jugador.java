package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private int victorias;
    private List<Intento> historialIntentos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.victorias = 0;
        this.historialIntentos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public void registrarIntento(Intento intento) {
        this.historialIntentos.add(intento);
    }

    public List<Intento> getHistorialIntentos() {
        return historialIntentos;
    }
}
