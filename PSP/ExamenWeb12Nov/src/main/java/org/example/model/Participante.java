package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Participante {
    private String nombre;
    private int distanciaTotal;
    private List<Integer> historialTiradas;

    public Participante(String nombre) {
        this.nombre = nombre;
        this.distanciaTotal = 0;
        this.historialTiradas = new ArrayList<>();
    }

    public void registrarTirada(int dado) {
        this.distanciaTotal += dado;
        this.historialTiradas.add(dado);
    }

    public int getNumeroTiradas() {
        return historialTiradas.size();
    }

    public String getNombre() { return nombre; }
    public int getDistanciaTotal() { return distanciaTotal; }
    public List<Integer> getHistorialTiradas() { return historialTiradas; }
}