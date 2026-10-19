package org.example.Ejercicios.bloque8;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TrabajoEnFases {

    public static void accionAlLlegarTodos() {
        System.out.println("Fase completada, todos avanzan");
    }


    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(4, TrabajoEnFases::accionAlLlegarTodos);
        Random random = new Random();

        for (int i = 1; i <= 4; i++) {
            int id = i;
            Thread t = new Thread(() -> {
                for (int j = 1; j <= 3; j++) {
                    int fase = j;
                    try {

                        System.out.println("Iniciando thread " + id + " en fase " + fase);
                        Thread.sleep(1000+random.nextInt(1000,3000));
                        System.out.println("Hilo "+id+ " espera en la barrera "+fase);
                        barrier.await();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t.start();
        }

    }


}
