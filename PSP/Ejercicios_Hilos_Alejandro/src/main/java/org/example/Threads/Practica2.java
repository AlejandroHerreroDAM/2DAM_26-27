package org.example.Threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Practica2 {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> tareaLarga = () -> {
            System.out.println("Hilo abierto");
            Thread.sleep(5000);
            return "Tarea completada";
        };

        Future<String> futuro = executor.submit(tareaLarga);

        while (!futuro.isDone()) {
            System.out.println("Esperando...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        try {
            String resultado = futuro.get();
            System.out.println("Resultado final: " + resultado);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}