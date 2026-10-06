package org.example.bloque2;

import java.time.LocalTime;
import java.util.Random;

public class DescargasJoin {
    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        Thread[] descargas = new Thread[3];

        for (int i = 0; i < 3; i++) {
            int contador = i+1;
            descargas[i] = new Thread(()->{
                int duracion = rand.nextInt(1000, 5000);
                System.out.println("Descarga "+contador+" Inicidada. Duracion: "+duracion+" ms");
                try {
                    Thread.sleep(duracion);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Descarga "+contador+" Finalizada");
            });
            descargas[i].start();
        }

        for (Thread t : descargas) {
            t.join();
        }
        System.out.println("Descargas terminadas");
    }
}
