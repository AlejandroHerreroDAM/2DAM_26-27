package org.example.bloque3;

public class ContadorRoto {
    private int contador = 0;

    public void incrementar(){
        contador++;
    }

    public int getContador(){
        return contador;
    }

    public static void main(String[] args) throws InterruptedException {

        Thread[] hilos = new Thread[10];
        ContadorRoto contador = new ContadorRoto();

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
