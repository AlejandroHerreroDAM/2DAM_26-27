package org.example.bloque3;

public class Deadlock {
    private static Object recursoA = new Object();
    private static Object recursoB = new Object();


    public static void main(String[] args) {
 /*
        System.out.println("DEADLOCK PROVOCADO");

        Thread t1 = new Thread(()->{
            synchronized (recursoA){
                System.out.println("t1 Agarra sarten");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (recursoB){
                    System.out.println("t1 Agarra aceite");
                }
            }
        });

        Thread t2 = new Thread(()->{
            synchronized (recursoB){
                System.out.println("t2 Agarra aceite");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (recursoA){
                    System.out.println("t2 Agarra sarten");
                }
            }
        });
        t1.start();
        t2.start();

     */


        System.out.println("DEADLOCK ARREGLADO");

        Thread t3 = new Thread(()->{
            synchronized (recursoA){
                System.out.println("t3 Agarra sarten");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (recursoB){
                    System.out.println("t3 Agarra aceite");
                }
            }
        });

        Thread t4= new Thread(()->{
            synchronized (recursoA){
                System.out.println("t4 Agarra aceite");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (recursoB){
                    System.out.println("t4 Agarra sarten");
                }
            }
        });
        t3.start();
        t4.start();

    }


}
