package org.example.bloque1;

import java.time.LocalTime;

public class ThreadDeamon {
    public static void main(String[] args) throws InterruptedException {
        Thread reloj = new Thread(() -> {
            while (true) {
                System.out.println(LocalTime.now());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        reloj.setDaemon(true);
        reloj.start();

        Thread.sleep(10000);
        System.out.println("Fin del main, el daemon morirá con él.");
    }
}
