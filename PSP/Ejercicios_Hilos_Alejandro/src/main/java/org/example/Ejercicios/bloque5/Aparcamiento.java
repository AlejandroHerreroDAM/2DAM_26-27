package org.example.Ejercicios.bloque5;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Aparcamiento {
    public static void main(String[] args) {

        Random rand = new Random();
        Semaphore plazas = new Semaphore(3,true);

        for (int i = 1; i <= 10; i++) {
            int coche = i;
            new Thread(()->{
                try {
                    plazas.acquire();
                    System.out.println("Coche " + coche+ " ha entradado. Plazas libres "+plazas.availablePermits());
                    Thread.sleep(500+ rand.nextInt(3000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println("Coche " + coche+ " ha salido. Plazas libres "+plazas.availablePermits());
                    plazas.release();
                }

            }).start();
        }

    }
}
