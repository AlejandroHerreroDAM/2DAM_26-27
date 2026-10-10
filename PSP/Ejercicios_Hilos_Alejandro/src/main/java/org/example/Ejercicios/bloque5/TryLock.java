package org.example.Ejercicios.bloque5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TryLock {

    private static ReentrantLock lock = new ReentrantLock();
    private static Boolean conseguido = false;

    public static void main(String[] args) {


    Runnable tarea = () -> {
        while (!conseguido) {
            try {
                if(lock.tryLock(500, TimeUnit.MILLISECONDS)){
                    try {
                        System.out.println("Hilo dentro");
                        Thread.sleep(5000);
                    } finally {
                        lock.unlock();
                        conseguido = true;
                    }
                }else{
                    System.out.println("Ocupado, lo intento más tarde.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    Thread hilo1 = new Thread(tarea);
    Thread hilo2 = new Thread(tarea);

    hilo1.start();
    hilo2.start();

    }
}
