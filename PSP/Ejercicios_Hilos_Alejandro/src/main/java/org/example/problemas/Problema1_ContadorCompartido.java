package org.example.problemas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Problema1_ContadorCompartido {

    int contador = 0;

    public synchronized void incrementarVisita(){
        contador++;
    }

    public static void main(String[] args) throws InterruptedException {

        int HILOS = 1000;
        Problema1_ContadorCompartido obj = new Problema1_ContadorCompartido();
        Random rand = new Random();
        List<Thread> threads = new ArrayList<>();
        long tiempoMs = 0;
        long tiempoIni;

        tiempoIni = System.currentTimeMillis();

        for (int i = 1; i <= HILOS; i++) {

            int id = i;

            Thread t = new Thread(() ->{
                try {

                    System.out.println("Hilo "+ id + " ha visitado la web");
                    Thread.sleep(50+rand.nextInt(101));
                    obj.incrementarVisita();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();
            threads.add(t);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        tiempoMs = System.currentTimeMillis()-tiempoIni;

        System.out.println("Visitas contadas "+ obj.contador);
        System.out.println("Tiempo final: "+ tiempoMs);

    }
}


