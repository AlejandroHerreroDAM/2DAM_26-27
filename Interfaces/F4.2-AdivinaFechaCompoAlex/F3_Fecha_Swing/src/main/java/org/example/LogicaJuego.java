package org.example;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class LogicaJuego {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final LocalDate fechaObjetivo;
    private int intentos;

    public LogicaJuego(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaObjetivo = generarFechaAleatoriaEnRango(fechaInicio, fechaFin);
        this.intentos = 0;
    }

    public static LocalDate generarFechaAleatoriaEnRango(LocalDate inicio, LocalDate fin) {
        long minDay = inicio.toEpochDay();
        long maxDay = fin.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalDate[] generarRangoAleatorio(int anoMin, int anoMax) {
        LocalDate f1 = generarFechaAleatoriaEnRango(LocalDate.of(anoMin, 1, 1), LocalDate.of(anoMax, 12, 31));
        LocalDate f2 = generarFechaAleatoriaEnRango(LocalDate.of(anoMin, 1, 1), LocalDate.of(anoMax, 12, 31));
        if (f1.isAfter(f2)) {
            return new LocalDate[]{f2, f1};
        }
        return new LocalDate[]{f1, f2};
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