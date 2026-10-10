package org.example.bloque3;

public class ContadorBien {
    private int contador = 0;

    public synchronized void incrementar(){
        contador++;
    }

    public synchronized int getContador(){
        return contador;
    }

    public static void main(String[] args) throws InterruptedException {

        Thread[] hilos = new Thread[10];
        ContadorBien contador = new ContadorBien();

        Runnable tarea = () -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        };

        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new Thread(tarea);
        }

        for (Thread t : hilos) {
            t.start();
        }

        for (Thread t : hilos) {
            t.join();
        }

        System.out.println(contador.getContador());

    }


}
