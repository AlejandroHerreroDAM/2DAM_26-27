package org.example.model;

import java.util.Arrays;

public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    private String simbolo1;
    private String simbolo2;
    private Jugador ganador;
    private Jugador perdedor;
    private int contadorMovimientos;
    private Movimiento[] movimientos;

    public Partida(Jugador jugador1, Jugador jugador2, String simbolo1, String simbolo2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.simbolo1 = simbolo1;
        this.simbolo2 = simbolo2;
        this.movimientos = new Movimiento[9];
        this.contadorMovimientos = 0;
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

    public String getSimbolo1() {
        return simbolo1;
    }

    public void setSimbolo1(String simbolo1) {
        this.simbolo1 = simbolo1;
    }

    public String getSimbolo2() {
        return simbolo2;
    }

    public void setSimbolo2(String simbolo2) {
        this.simbolo2 = simbolo2;
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

    public Movimiento[] getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Movimiento[] movimientos) {
        this.movimientos = movimientos;
    }

    public int getContadorMovimientos() {
        return contadorMovimientos;
    }

    public void setContadorMovimientos(int contadorMovimientos) {
        this.contadorMovimientos = contadorMovimientos;
    }

    public void registrarMovimiento(Movimiento mov) {
        if (contadorMovimientos < 9) {
            this.movimientos[contadorMovimientos] = mov;
            contadorMovimientos++;
        }
    }

    @Override
    public String toString() {
        return "Partida{" +
                "jugador1=" + jugador1 +
                ", jugador2=" + jugador2 +
                ", simbolo1='" + simbolo1 + '\'' +
                ", simbolo2='" + simbolo2 + '\'' +
                ", ganador=" + ganador +
                ", perdedor=" + perdedor +
                ", movimientos=" + Arrays.toString(movimientos) +
                '}';
    }
}
