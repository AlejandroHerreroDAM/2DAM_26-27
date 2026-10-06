package org.example;

public class ContadorThread extends Thread {
    public void run() {
        for (int i=0; i<=10;i++){
            System.out.println(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    void main(String[] args) {


        Thread r1 = new Thread(new ContadorRunnable());
        Thread r2 = new Thread(new ContadorRunnable());
        r1.start();
        r2.start();
    }


}
