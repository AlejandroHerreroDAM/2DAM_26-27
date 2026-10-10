package org.example.Threads;

import java.util.concurrent.CompletableFuture;

public class Practica5 {

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
                    System.out.println("Hilo calculando");
                    return 10 + 20;
                }).thenApply(resultadoSuma -> {
                    System.out.println("Multiplicando por 2");
                    return resultadoSuma * 2;
                }).thenAccept(resultadoFinal -> {
                    System.out.println("Resultado: " + resultadoFinal);
                });
    }
}