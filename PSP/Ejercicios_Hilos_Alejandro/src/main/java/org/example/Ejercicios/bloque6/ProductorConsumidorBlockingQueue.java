package org.example.Ejercicios.bloque6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProductorConsumidorBlockingQueue {
    public static void main(String[] args) {

        BlockingQueue<Integer> cola = new ArrayBlockingQueue<Integer>(5);

        Thread hiloProductor = new Thread(() ->{
            for (int i = 1; i <= 5; i++) {
                try {
                    cola.put(i);
                    System.out.println("Produce "+i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread hiloConsumidor = new Thread(() ->{
            for (int i = 1; i <= 5; i++) {
                try {
                    int valor = cola.take();
                    System.out.println("Consume "+valor);
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
        });

        hiloProductor.start();
        hiloConsumidor.start();

    }


}
