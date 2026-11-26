package org.example.model;

import java.util.Random;

public class Jugador {

    private int id;
    private int posicion;

    public Jugador(int id) {
        this.id = id;
        this.posicion = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }


    int NUM_AVANZA_MIN = 1;
    int NUM_AVANZA_MAX = 20;


    public int tiraDado(){
        Random random = new Random();
        return NUM_AVANZA_MIN + random.nextInt(NUM_AVANZA_MAX+1);
    }


}
