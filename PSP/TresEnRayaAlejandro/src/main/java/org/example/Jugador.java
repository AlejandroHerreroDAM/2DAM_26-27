package org.example;

public class Jugador {
    private String nombre;
    private int victorias;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.victorias = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", victorias=" + victorias +
                '}';
    }
}
