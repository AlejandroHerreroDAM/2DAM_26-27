package org.example.Threads;

import java.util.concurrent.*;

public class Practica1 {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> tarea = () -> {
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> futuroResultado = executor.submit(tarea);

        try {
            Integer resultado = futuroResultado.get();
            System.out.println("Resultado: " + resultado);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
