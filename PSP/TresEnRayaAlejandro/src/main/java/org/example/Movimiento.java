package org.example;

public class Movimiento {
    private String turno;
    private String coordenada;

    public Movimiento(String turno, String coordenada) {
        this.turno = turno;
        this.coordenada = coordenada;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(String coordenada) {
        this.coordenada = coordenada;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "turno='" + turno + '\'' +
                ", coordenada='" + coordenada + '\'' +
                '}';
    }
}
