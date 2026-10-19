package org.example;

import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;
import java.util.Random;

public class LogicaJuego {

    static int ANIOMIN = 2000;
    static int ANIOMAX = 2026;

    int diaRandom;
    int mesRandom;
    int anioRandom;



    public void getRandomFecha(){
        Random random = new Random();

        diaRandom = random.nextInt(1,31);
        mesRandom = random.nextInt(1,12);
        diaRandom = random.nextInt(ANIOMIN,ANIOMAX);
    }








}
