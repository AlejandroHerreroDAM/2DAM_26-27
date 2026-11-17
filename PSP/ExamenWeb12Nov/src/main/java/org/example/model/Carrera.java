package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Carrera {
    private int longitud;
    private boolean comenzada;
    private boolean finalizada;
    private String ganador;
    private int rondaActual;
    private List<Participante> participantes;

    public Carrera(int longitud) {
        this.longitud = longitud;
        this.comenzada = false;
        this.finalizada = false;
        this.rondaActual = 1;
        this.participantes = new ArrayList<>();
    }

    public Participante buscarParticipante(String nombre) {
        return participantes.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public boolean puedeTirarEnRondaActual(Participante p) {
        // En la ronda 1 debe tener 0 tiradas; en la ronda 2 debe tener 1 tirada, etc.
        return p.getNumeroTiradas() == (rondaActual - 1);
    }

    public void comprobarFinDeRonda() {
        boolean todosHanTirado = participantes.stream()
                .allMatch(p -> p.getNumeroTiradas() >= rondaActual);

        if (todosHanTirado) {
            rondaActual++;
        }
    }


    public int getLongitud() { return longitud; }
    public boolean isComenzada() { return comenzada; }
    public void setComenzada(boolean comenzada) { this.comenzada = comenzada; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    public String getGanador() { return ganador; }
    public void setGanador(String ganador) { this.ganador = ganador; }
    public int getRondaActual() { return rondaActual; }
    public List<Participante> getParticipantes() { return participantes; }
}