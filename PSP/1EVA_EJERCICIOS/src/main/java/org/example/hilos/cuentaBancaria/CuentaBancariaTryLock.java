package org.example.hilos.cuentaBancaria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class CuentaBancariaTryLock {
    private double saldo = 5000;
    private int operacionesFallidas = 0;
    private List<String> historial = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public int getOperacionesFallidas() {
        return operacionesFallidas;
    }

    public boolean retirar(double cantidad) {

        lock.lock();
        try {
            boolean esSaldoPositivo = true;

            if (cantidad <= saldo) {
                saldo = saldo - cantidad;
                historial.add("[" + LocalDateTime.now() + "] Retirado: " + cantidad + ", salto total: " + saldo + "€");
            } else {
                operacionesFallidas++;
                historial.add("[" + LocalDateTime.now() + "] Operacion fallida, saldo infuciciente");
                esSaldoPositivo = false;
            }

            return esSaldoPositivo;
        } finally {
            lock.unlock();
        }


    }


    public void ingresar(double cantidad) {
        lock.lock();
        try {
            saldo = saldo + cantidad;
            historial.add("[" + LocalDateTime.now() + "] Ingresado: " + cantidad + ", salto total: " + saldo + "€");
        } finally {
            lock.unlock();
        }

    }

    public double consultarSaldo() {
        lock.lock();
        try {
            return saldo;
        } finally {
            lock.unlock();
        }

    }

    public List<String> obtenerHistorial() {
        lock.lock();
        try {
            return historial;
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        int CLIENTES = 50;
        int NOPERACIONES = 10;
        long tiempoTotal;
        long tiempoIni = System.currentTimeMillis();

        CuentaBancariaTryLock cuentaBancaria = new CuentaBancariaTryLock();
        Random random = new Random();
        List<Thread> threads = new ArrayList<>();

        System.out.println("""
                === BANCO VIRTUAL ===
                Saldo inicial: 10000.00€
                50 clientes realizando 500 operaciones totales...
                """);

        for (int i = 1; i <= CLIENTES; i++) {
            int idCliente = i;

            Thread t = new Thread(() -> {
                for (int j = 1; j <= NOPERACIONES; j++) {
                    System.out.println("Cliente " + idCliente + " entra a la cuenta");

                    try {

                        Thread.sleep(100 + random.nextInt(201));
                        System.out.println("--------------------");
                        if ((1 + random.nextInt(10)) <= 6) {
                            System.out.println("Cliente " + idCliente + " retira");
                            if (cuentaBancaria.retirar(1 + random.nextInt(100))) {
                                System.out.println("Saldo restante: " + cuentaBancaria.consultarSaldo());
                            } else {
                                System.out.println("Operacion cancelada, saldo insuficiente");
                            }

                        } else {
                            System.out.println("Cliente " + idCliente + " ingresa");
                            cuentaBancaria.ingresar(1 + random.nextInt(50));
                            System.out.println("Saldo restante: " + cuentaBancaria.consultarSaldo());
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            });


            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        tiempoTotal = System.currentTimeMillis() - tiempoIni;

        System.out.println("===========================");
        System.out.println("OPERACIONES TERMINADAS");
        System.out.println("Operaciones fallidas: " + cuentaBancaria.getOperacionesFallidas());
        System.out.println("Tiempo total: " + tiempoTotal + " ms");

    }


}
