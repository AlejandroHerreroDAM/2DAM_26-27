package org.example;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class LogicaJuego {
    private final LocalDate fechaObjetivo;
    private int intentos;

    public LogicaJuego() {
        long minDay = LocalDate.of(2015, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2024, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
        this.fechaObjetivo = LocalDate.ofEpochDay(randomDay);
        this.intentos = 0;
    }

    public int compararDia(int dia) {
        return Integer.compare(dia, fechaObjetivo.getDayOfMonth());
    }

    public int compararMes(int mes) {
        return Integer.compare(mes, fechaObjetivo.getMonthValue());
    }

    public int compararAno(int ano) {
        return Integer.compare(ano, fechaObjetivo.getYear());
    }

    public void incrementarIntento() {
        this.intentos++;
    }

    public int getIntentos() {
        return this.intentos;
    }
}