package org.example;

public class ContadorRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + " -> " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    void main(String[] args) {

        ContadorThread t1 = new ContadorThread();
        ContadorThread t2 = new ContadorThread();
        t1.start();
        t2.start();
    }
}