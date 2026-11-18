package org.example.model;

public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    private String palabra;
    private Jugador ganador;
    private Jugador perdedor;
    private boolean comenzada;
    private boolean finalizada;

    public Partida() {
        this.comenzada = false;
        this.finalizada = false;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public Jugador getPerdedor() {
        return perdedor;
    }

    public void setPerdedor(Jugador perdedor) {
        this.perdedor = perdedor;
    }

    public boolean isComenzada() { return comenzada; }
    public void setComenzada(boolean comenzada) { this.comenzada = comenzada; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }
}
