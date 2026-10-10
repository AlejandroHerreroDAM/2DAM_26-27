package org.example.Ejercicios.bloque8;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Corredores {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch listos = new CountDownLatch(5);
        CountDownLatch salida = new CountDownLatch(1);
        Random random = new Random();

        for(int i = 1; i <= 5; i++){
            int idCorredor = i;
            Thread corredor = new Thread(()->{
                try {

                    System.out.println("Hilo "+idCorredor+" Calentando");
                    Thread.sleep(500+random.nextInt(1000));
                    listos.countDown();
                    System.out.println("Hilo "+idCorredor+" Listo, quedan "+listos.getCount());
                    salida.await();
                    System.out.println("Hilo "+idCorredor+" Salida!");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
            corredor.start();
        }


        listos.await();
        System.out.println("Todos listos");
        salida.countDown();





    }





}
