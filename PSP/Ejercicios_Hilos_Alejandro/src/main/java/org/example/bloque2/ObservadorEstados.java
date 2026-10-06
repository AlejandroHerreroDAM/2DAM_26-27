package org.example.bloque2;

public class ObservadorEstados {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
        });

        System.out.println("Estado antes de start: " + t.getState()); // NEW
        t.start();

        while (t.isAlive()) {
            System.out.println("Estado: " + t.getState());
            Thread.sleep(500);
        }
        System.out.println("Estado final: " + t.getState()); // TERMINATED
    }
}
