package org.example.bloque2;

public class InterrupcionCooperativa {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(()->{

            while(!Thread.currentThread().isInterrupted()){

                System.out.println("Interrupcion Cooperativa");

            }
            System.out.println("Hilo terminado");
        });

        t.start();
        Thread.sleep(2000);
        t.interrupt();


    }


}
