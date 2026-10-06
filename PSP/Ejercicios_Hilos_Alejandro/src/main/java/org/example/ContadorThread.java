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


}
