package org.example.hilos.cocina;
/*
PROBLEMA 3 - El Restaurante Concurrente de clase dado como solucion
Patron Productor-Consumidor con 5 camareros (productores), 3 cocineros
(consumidores) y 100 clientes (hilos virtuales).
con los siguientes cambios

1Pto El cierre del resturante deberá de ser más controlado, y no salir hasta parar a los cocineros y camareros.

0,5 Pto Tb pon precio a los platos y acumula cuanto ha ganado el restaurente en el servicio.

2Pto Flujo de un cliente (hilo virtual):

    llega ->
    espera mesa libre durante un tiempo(elige el tiempo para que no se vayan todos) ->
    se sienta si la consigue y si no se va->
    pide ->
    espera el plato ->
    se lo come ->
    libera la mesa ->
     se va

2Pto Los camareros tienen PRIORIDAD para servir platos: antes de tomar un pedido nuevo
     o de meter uno en cocina, siempre miran si hay platos listos para entregarlos a los clientes.

*/
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

public class Cocina {

    enum TipoPlato {
        ENSALADA(2000, 8.50),
        PASTA(3000, 11.00),
        PIZZA(4000, 12.50),
        CARNE(5000, 16.00);

        final int tiempoMs;
        final double precio;

        TipoPlato(int tiempoMs, double precio) {
            this.tiempoMs = tiempoMs;
            this.precio = precio;
        }

        static TipoPlato aleatorio(Random r) {
            return values()[r.nextInt(values().length)];
        }
    }

    // Incluye un CountDownLatch individual para que el hilo virtual del cliente espere su plato
    record Pedido(int cliente, TipoPlato plato, long horaCreacionMs, CountDownLatch platoServido) {}

    // --- Constantes del enunciado ---
    static final int COCINEROS = 3;
    static final int CAMAREROS = 5;
    static final int CLIENTES = 100;
    static final int NUM_MESAS = 12;
    static final int CAPACIDAD_MESA = 10; // Capacidad de la barra/pasaplatos de cocina
    static final long LLEGADA_CLIENTE_MS = 500;
    static final long TIEMPO_ESPERA_MESA_MS = 4000; // Tiempo máximo que espera un cliente por mesa libre
    static final long TIEMPO_CAMARERO_MS = 1000;
    static final long TIEMPO_COMER_MS = 3000;

    // --- Recursos compartidos ---
    static final Semaphore mesasLibres = new Semaphore(NUM_MESAS, true);
    static final BlockingQueue<Pedido> mesa = new ArrayBlockingQueue<>(CAPACIDAD_MESA); // Pedidos pendientes de cocinar
    static final BlockingQueue<Pedido> colaClientes = new ArrayBlockingQueue<>(CLIENTES); // Clientes sentados esperando pedir
    static final BlockingQueue<Pedido> platosListos = new LinkedBlockingQueue<>(); // Platos cocinados listos para servir
    static final AtomicBoolean abierto = new AtomicBoolean(true);

    // --- Estadisticas ---
    static final AtomicInteger clientesAtendidos = new AtomicInteger();
    static final AtomicInteger clientesMarchados = new AtomicInteger();
    static final AtomicInteger platosServidos = new AtomicInteger();
    static final AtomicLong esperaTotalMs = new AtomicLong();
    static final AtomicInteger mesaLlenaCount = new AtomicInteger();
    static final AtomicLong cocinerosEsperandoMs = new AtomicLong();
    static final DoubleAdder recaudacionTotal = new DoubleAdder();

    static String ahora() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RESTAURANTE CONCURRENTE ===");
        System.out.println("Iniciando servicio con " + COCINEROS + " cocineros, "
                + CAMAREROS + " camareros y " + NUM_MESAS + " mesas...\n");

        List<Thread> hilosCocineros = new ArrayList<>();
        List<Thread> hilosCamareros = new ArrayList<>();
        List<Thread> hilosClientes = new ArrayList<>();

        // --- Cocineros (consumidores de 'mesa' -> dejan platos en 'platosListos') ---
        for (int i = 1; i <= COCINEROS; i++) {
            final int id = i;
            Thread cocinero = Thread.ofPlatform().name("cocinero-" + id).start(() -> {
                try {
                    while (abierto.get() && !Thread.currentThread().isInterrupted()) {
                        long t0 = System.currentTimeMillis();
                        Pedido p = mesa.take(); // espera si no hay pedidos en cocina
                        cocinerosEsperandoMs.addAndGet(System.currentTimeMillis() - t0);

                        Thread.sleep(p.plato().tiempoMs); // cocina el plato

                        System.out.println("[" + ahora() + "] Cocinero-" + id + " termina "
                                + p.plato() + " para Cliente-" + p.cliente());

                        platosListos.put(p); // pasa el plato a la cola de platos listos para servir
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // cierre del restaurante
                }
                System.out.println("[" + ahora() + "] Cocinero-" + id + " finaliza su turno.");
            });
            hilosCocineros.add(cocinero);
        }

        // --- Camareros (PRIORIDAD: 1º servir platos listos, 2º tomar pedido y meter en cocina) ---
        for (int i = 1; i <= CAMAREROS; i++) {
            final int id = i;
            Thread camarero = Thread.ofPlatform().name("camarero-" + id).start(() -> {
                try {
                    while (abierto.get() && !Thread.currentThread().isInterrupted()) {
                        // PRIORIDAD 1: Mirar siempre antes si hay platos listos para entregar
                        Pedido listo = platosListos.poll();
                        if (listo != null) {
                            servirPlato(id, listo);
                            continue; // vuelve arriba para seguir dando prioridad a platos listos
                        }

                        // PRIORIDAD 2: Si no hay platos listos, tomar pedido nuevo
                        Pedido p = colaClientes.poll(100, TimeUnit.MILLISECONDS);
                        if (p != null) {
                            Thread.sleep(TIEMPO_CAMARERO_MS); // toma el pedido
                            System.out.println("[" + ahora() + "] Camarero-" + id
                                    + " toma pedido " + p.plato() + " de Cliente-" + p.cliente());

                            // Antes de meterlo en cocina, vuelve a comprobar si hay platos listos
                            Pedido urgente = platosListos.poll();
                            if (urgente != null) {
                                servirPlato(id, urgente);
                            }

                            if (mesa.remainingCapacity() == 0) {
                                mesaLlenaCount.incrementAndGet();
                            }
                            mesa.put(p); // mete el pedido en cocina (espera si está llena)
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[" + ahora() + "] Camarero-" + id + " finaliza su turno.");
            });
            hilosCamareros.add(camarero);
        }

        // --- Clientes (100 hilos virtuales, llegan cada 500 ms) ---
        Thread generadorClientes = Thread.ofPlatform().name("puerta").start(() -> {
            Random random = new Random();
            for (int i = 1; i <= CLIENTES; i++) {
                final int id = i;
                final TipoPlato plato = TipoPlato.aleatorio(random);

                Thread clienteVirtual = Thread.ofVirtual().name("cliente-" + id).start(() -> {
                    System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                            + " llega y busca mesa...");

                    boolean sentado = false;
                    try {
                        // 1. Espera mesa libre durante un tiempo máximo
                        sentado = mesasLibres.tryAcquire(TIEMPO_ESPERA_MESA_MS, TimeUnit.MILLISECONDS);

                        if (!sentado) {
                            clientesMarchados.incrementAndGet();
                            System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                                    + " no encuentra mesa a tiempo y se va.");
                            return;
                        }

                        clientesAtendidos.incrementAndGet();
                        System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                                + " se sienta (mesas libres: " + mesasLibres.availablePermits() + "/" + NUM_MESAS + ")");

                        // 2. Pide su plato y espera a que el camarero se lo sirva
                        CountDownLatch latchPlato = new CountDownLatch(1);
                        Pedido pedido = new Pedido(id, plato, System.currentTimeMillis(), latchPlato);
                        System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                                + " pide " + plato);
                        colaClientes.put(pedido);

                        latchPlato.await(); // espera hasta recibir el plato

                        // 3. Se lo come
                        System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                                + " come su " + plato + "...");
                        Thread.sleep(TIEMPO_COMER_MS);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        // 4. Libera la mesa y se va
                        if (sentado) {
                            mesasLibres.release();
                            System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                                    + " libera la mesa y se va.");
                        }
                    }
                });

                hilosClientes.add(clienteVirtual);

                try {
                    Thread.sleep(LLEGADA_CLIENTE_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Esperar a que lleguen todos los clientes y a que todos los hilos virtuales terminen
        generadorClientes.join();
        for (Thread c : hilosClientes) {
            c.join();
        }

        // --- Cierre controlado del restaurante (sin System.exit(0)) ---
        System.out.println("\n[" + ahora() + "] Todos los clientes se han ido. Cerrando restaurante...");
        abierto.set(false);

        for (Thread c : hilosCamareros) {
            c.interrupt();
        }
        for (Thread c : hilosCocineros) {
            c.interrupt();
        }
        for (Thread c : hilosCamareros) {
            c.join();
        }
        for (Thread c : hilosCocineros) {
            c.join();
        }

        int atendidos = clientesAtendidos.get();
        System.out.println("\n--- ESTADISTICAS FINALES ---");
        System.out.println("Clientes atendidos: " + atendidos + "/" + CLIENTES
                + " (Se fueron sin mesa: " + clientesMarchados.get() + ")");
        System.out.println("Platos servidos: " + platosServidos.get());
        System.out.printf("Ganancias totales del servicio: %.2f€%n", recaudacionTotal.sum());
        System.out.printf("Tiempo promedio de espera por plato: %.2fs%n",
                (atendidos > 0) ? (esperaTotalMs.get() / 1000.0 / atendidos) : 0.0);
        System.out.println("Mesa llena (veces): " + mesaLlenaCount.get());
        System.out.println("Cocineros esperando (tiempo acumulado): "
                + (cocinerosEsperandoMs.get() / 1000) + "s total");
        System.out.println("Eficiencia: el sistema soporta " + CAMAREROS + " pedidos/s en entrada"
                + " y " + COCINEROS + " platos en paralelo en cocina.");
    }

    private static void servirPlato(int idCamarero, Pedido p) throws InterruptedException {
        Thread.sleep(300); // Tiempo de llevar el plato a la mesa
        esperaTotalMs.addAndGet(System.currentTimeMillis() - p.horaCreacionMs());
        platosServidos.incrementAndGet();
        recaudacionTotal.add(p.plato().precio);

        System.out.printf("[%s] Camarero-%d SIRVE %s a Cliente-%03d (+%.2f€)%n",
                ahora(), idCamarero, p.plato(), p.cliente(), p.plato().precio);

        p.platoServido().countDown(); // Desbloquea al cliente para que empiece a comer
    }
}