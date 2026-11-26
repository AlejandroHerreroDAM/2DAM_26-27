package org.example.hilos.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.locks.ReentrantLock;

public class Parking2 {

    enum TipoVehiculo {
        NORMAL(1.0),
        VIP(2.0);

        private final double tarifaPorMinuto;

        TipoVehiculo(double tarifaPorMinuto) {
            this.tarifaPorMinuto = tarifaPorMinuto;
        }

        public double getTarifaPorMinuto() {
            return tarifaPorMinuto;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int NUM_COCHES = 50;
        int NUM_PLAZAS_NORMALES = 20;
        int NUM_PLAZAS_VIP = 5;
        int NUM_MAX_COLA = 10;

        int TIEMPO_ENTRADA = 2000;
        int TIEMPO_SALIDA = 1000;

        Random random = new Random();
        List<Thread> threads = new ArrayList<>();

        ReentrantLock lockEntrada = new ReentrantLock();
        ReentrantLock lockSalida = new ReentrantLock();
        Semaphore semaforoCola = new Semaphore(NUM_MAX_COLA);
        Semaphore semaforoPlazasVIP = new Semaphore(NUM_PLAZAS_VIP);
        Semaphore semaforoPlazasNormales = new Semaphore(NUM_PLAZAS_NORMALES);

        // Estadísticas finales
        AtomicInteger cochesAtendidos = new AtomicInteger(0);
        AtomicInteger cochesRechazados = new AtomicInteger(0);
        AtomicLong tiempoTotalEstanciaMs = new AtomicLong(0);
        AtomicInteger ocupacionActual = new AtomicInteger(0);
        AtomicInteger ocupacionMaxima = new AtomicInteger(0);
        DoubleAdder ingresosTotales = new DoubleAdder();

        for (int i = 1; i <= NUM_COCHES; i++) {
            int idCoche = i;
            int TIEMPO_ESTANCIA = 5000 + random.nextInt(10001);
            TipoVehiculo tipoVehiculo = (random.nextInt(10) < 2) ? TipoVehiculo.VIP : TipoVehiculo.NORMAL;

            Thread hilo = Thread.ofVirtual().name("Coche " + i).start(() -> {
                System.out.println("Coche " + idCoche + " (" + tipoVehiculo + ") creado");

                // 1. CONTROL DE ADMISIÓN EN COLA
                if (semaforoCola.tryAcquire()) {
                    boolean enCola = true;
                    boolean tienePlaza = false;
                    boolean esPlazaVip = false;

                    try {
                        int enColaAhora = NUM_MAX_COLA - semaforoCola.availablePermits();
                        System.out.println("Coche " + idCoche + " (" + tipoVehiculo + ") entra en cola " + enColaAhora + "/" + NUM_MAX_COLA);

                        // 2. ASIGNACIÓN DE PLAZA
                        if (tipoVehiculo == TipoVehiculo.VIP) {
                            while (!tienePlaza) {
                                if (semaforoPlazasVIP.tryAcquire()) {
                                    tienePlaza = true;
                                    esPlazaVip = true;
                                } else if (semaforoPlazasNormales.tryAcquire()) {
                                    tienePlaza = true;
                                    esPlazaVip = false;
                                } else {
                                    Thread.sleep(100);
                                }
                            }
                        } else {
                            semaforoPlazasNormales.acquire();
                            tienePlaza = true;
                            esPlazaVip = false;
                        }

                        // 3. CRUZAR BARRERA DE ENTRADA Y LIBERAR HUECO EN COLA
                        lockEntrada.lock();
                        try {
                            Thread.sleep(TIEMPO_ENTRADA);
                        } finally {
                            lockEntrada.unlock();
                        }

                        semaforoCola.release();
                        enCola = false;

                        cochesAtendidos.incrementAndGet();
                        tiempoTotalEstanciaMs.addAndGet(TIEMPO_ESTANCIA);
                        int ocupadasTotal = ocupacionActual.incrementAndGet();
                        ocupacionMaxima.accumulateAndGet(ocupadasTotal, Math::max);

                        if (esPlazaVip) {
                            int ocupadas = NUM_PLAZAS_VIP - semaforoPlazasVIP.availablePermits();
                            System.out.println("Coche " + idCoche + " (" + tipoVehiculo + ") entra en plaza VIP " + ocupadas + "/" + NUM_PLAZAS_VIP);
                        } else {
                            int ocupadas = NUM_PLAZAS_NORMALES - semaforoPlazasNormales.availablePermits();
                            System.out.println("Coche " + idCoche + " (" + tipoVehiculo + ") entra en plaza NORMAL " + ocupadas + "/" + NUM_PLAZAS_NORMALES);
                        }

                        // 4. ESTANCIA EN EL PARKING
                        Thread.sleep(TIEMPO_ESTANCIA);

                        // 5. CRUZAR BARRERA DE SALIDA
                        lockSalida.lock();
                        try {
                            Thread.sleep(TIEMPO_SALIDA);
                        } finally {
                            lockSalida.unlock();
                        }

                        // 6. COBRO Y LIBERACIÓN DE PLAZA
                        double coste = (TIEMPO_ESTANCIA / 60000.0) * tipoVehiculo.getTarifaPorMinuto();
                        ingresosTotales.add(coste);

                        ocupacionActual.decrementAndGet();
                        if (esPlazaVip) {
                            semaforoPlazasVIP.release();
                        } else {
                            semaforoPlazasNormales.release();
                        }
                        tienePlaza = false;

                        System.out.printf("Coche %d (%s) sale tras %.1fs - Pagó: %.2f€%n", idCoche, tipoVehiculo, TIEMPO_ESTANCIA / 1000.0, coste);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        if (enCola) {
                            semaforoCola.release();
                        }
                        if (tienePlaza) {
                            ocupacionActual.decrementAndGet();
                            if (esPlazaVip) {
                                semaforoPlazasVIP.release();
                            } else {
                                semaforoPlazasNormales.release();
                            }
                        }
                    }

                } else {
                    cochesRechazados.incrementAndGet();
                    System.out.println("Coche " + idCoche + " (" + tipoVehiculo + ") no ha podido entrar y se va");
                }
            });
            threads.add(hilo);

            // Llegada escalonada entre coches para simular flujo real
            Thread.sleep(300 + random.nextInt(601));
        }

        for (Thread t : threads) {
            t.join();
        }

        int atendidos = cochesAtendidos.get();
        int rechazados = cochesRechazados.get();
        int totalPlazas = NUM_PLAZAS_NORMALES + NUM_PLAZAS_VIP;
        int maxOcup = ocupacionMaxima.get();

        System.out.println("\n--- RESUMEN DEL DÍA ---");
        System.out.println("Vehículos procesados: " + NUM_COCHES);
        System.out.printf("Vehículos atendidos: %d (%.1f%%)%n", atendidos, (atendidos * 100.0) / NUM_COCHES);
        System.out.println("Vehículos rechazados: " + rechazados + " (parking+cola llenos)");
        System.out.printf("Tiempo promedio de estancia: %.1fs%n", (atendidos > 0) ? (tiempoTotalEstanciaMs.get() / 1000.0) / atendidos : 0.0);
        System.out.printf("Ingresos totales: %.2f€%n", ingresosTotales.sum());
        System.out.printf("Ocupación máxima: %d/%d plazas (%.0f%%)%n", maxOcup, totalPlazas, (maxOcup * 100.0) / totalPlazas);
    }
}