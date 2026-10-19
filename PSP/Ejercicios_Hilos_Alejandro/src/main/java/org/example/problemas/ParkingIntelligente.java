package org.example.problemas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.DoubleAdder;

public class ParkingIntelligente {

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

        int PLAZASNORMALES = 20;
        int PLAZASVIP = 5;
        int COCHES = 50;
        int COLA = 10;

        Random rand = new Random();
        List<Thread> Threads = new ArrayList<>();

        Semaphore semaforo = new Semaphore(PLAZASNORMALES);
        Semaphore semaforoVIP = new Semaphore(PLAZASVIP);
        Semaphore semaforoCola = new Semaphore(COLA);

        ReentrantLock lockEntrada = new ReentrantLock();
        ReentrantLock lockSalida = new ReentrantLock();

        DoubleAdder ingresosTotales = new DoubleAdder();

        for (int i = 1; i <= COCHES; i++) {
            int idCoche = i;

            TipoVehiculo tipoVehiculo = (rand.nextInt(10) < 2) ? TipoVehiculo.VIP : TipoVehiculo.NORMAL;

            Thread hilo = Thread.ofVirtual().name("Coche " + i).start(() -> {

                lockEntrada.lock();
                try {
                    System.out.println("Coche " + idCoche + " espera en la barrera de entrada");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lockEntrada.unlock();
                }

                if (tipoVehiculo == TipoVehiculo.VIP) {
                    if (semaforoVIP.tryAcquire()) {
                        aparca(idCoche, tipoVehiculo, " aparca en plaza VIP", rand, ingresosTotales);
                        semaforoVIP.release();
                        System.out.println("Coche " + idCoche + " " + tipoVehiculo + " ha salido del parking");
                    } else {
                        if (semaforo.tryAcquire()) {
                            aparca(idCoche, tipoVehiculo, " aparca en plaza normal", rand, ingresosTotales);
                            semaforo.release();
                            System.out.println("Coche " + idCoche + " " + tipoVehiculo + " ha salido del parking");
                        } else {
                            if (semaforoCola.tryAcquire()) {
                                try {
                                    semaforo.acquire();
                                    semaforoCola.release();
                                    aparca(idCoche, tipoVehiculo, " aparca en plaza normal tras esperar en la cola", rand, ingresosTotales);
                                    semaforo.release();
                                    System.out.println("Coche " + idCoche + " " + tipoVehiculo + " ha salido del parking");
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                System.out.println("Coche " + idCoche + " " + tipoVehiculo + " se va, no hay sitio en la cola");
                            }
                        }
                    }
                } else {
                    if (semaforo.tryAcquire()) {
                        aparca(idCoche, tipoVehiculo, " aparca en plaza normal", rand, ingresosTotales);
                        semaforo.release();
                        System.out.println("Coche " + idCoche + " " + tipoVehiculo + " ha salido del parking");
                    } else {
                        if (semaforoCola.tryAcquire()) {
                            try {
                                semaforo.acquire();
                                semaforoCola.release();
                                aparca(idCoche, tipoVehiculo, " aparca en plaza normal tras esperar en la cola", rand, ingresosTotales);
                                semaforo.release();
                                System.out.println("Coche " + idCoche + " " + tipoVehiculo + " ha salido del parking");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            System.out.println("Coche " + idCoche + " " + tipoVehiculo + " se va, no hay sitio en la cola");
                        }
                    }
                }

                lockSalida.lock();
                try {
                    System.out.println("Coche " + idCoche + " espera en la barrera de salida");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lockSalida.unlock();
                }
            });

            Threads.add(hilo);
        }

        for (Thread t : Threads) {
            t.join();
        }

        System.out.println("\n===========================");
        System.out.printf("Ingresos totales del día: %.2f€\n", ingresosTotales.sum());

    }

    private static void aparca(int idCoche, TipoVehiculo tipoVehiculo, String x, Random rand, DoubleAdder ingresosTotales) {
        try {
            System.out.println("Coche " + idCoche + " " + tipoVehiculo + x);
            int tiempoEstanciaMs = rand.nextInt(10000, 30000);
            Thread.sleep(tiempoEstanciaMs);
            double coste = (tiempoEstanciaMs / 1000.0) * tipoVehiculo.getTarifaPorMinuto();
            ingresosTotales.add(coste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}