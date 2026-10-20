package examen;
/*
 PROBLEMA 3 - El Restaurante Concurrente de clase dado como solucion
 Patron Productor-Consumidor con 5 camareros (productores), 3 cocineros
 (consumidores) y 100 clientes (hilos virtuales).
 con los siguientes cambios

 1Pto El cierre del resturante deberá de ser más controlado, y no salir hasta parar a los cocineros y camareros.

  ✅ 0,5 Pto  Tb pon precio a los platos y acumula cuanto ha ganado el restaurente en el servicio.

 2Pto Flujo de un cliente (hilo virtual):
   -> llega
   -> espera mesa libre durante un tiempo(elige el tiempo para que no se vayan todos)
   -> se sienta si la consigue y si no se va
   -> pide
   -> espera el plato
   -> se lo come
   -> libera la mesa -> se va

 ✅ 2Pto Los camareros tienen PRIORIDAD para servir platos: antes de tomar un pedido nuevo
     o de meter uno en cocina, siempre miran si hay platos listos para entregarlos a los clientes.

 2Pto Cambia los clientes por un grupo de clientes.  Flujo de un grupo:
    -> la primera parte hasta que se sientan es igual que un cliente solo
    -> el camarero toma la comanda (un plato por persona)
    -> espera a que le sirvan TODOS los platos
    -> comen
    -> liberan la mesa
    -> se van

*/

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

public class Ejercicio3 {

    enum TipoPlato {
        ENSALADA(2000, 8),
        PASTA(3000, 12),
        PIZZA(4000, 15),
        CARNE(5000, 20);

        final int tiempoMs;
        final double precio;

        TipoPlato(int tiempoMs, double precio) {
            this.tiempoMs = tiempoMs;
            this.precio = precio;
        }

        static examen.Ejercicio3.TipoPlato aleatorio(Random r) {
            return values()[r.nextInt(values().length)];
        }
    }

    static final DoubleAdder gananciasTotales = new DoubleAdder();

    static final AtomicInteger clientesFinalizados = new AtomicInteger();

    record Cliente(int id, TipoPlato plato, long horaLlegada, CompletableFuture<Void> comidaServida) {
    }

    record Pedido(Cliente cliente, long horaCreacionMs) {
    }

    // --- Constantes del enunciado ---
    static final int COCINEROS = 3;
    static final int CAMAREROS = 5;
    static final int CLIENTES = 10;
    static final int CAPACIDAD_MESA = 10;
    static final long LLEGADA_CLIENTE_MS = 500;
    static final long TIEMPO_CAMARERO_MS = 1000;

    // --- Recursos compartidos ---
    static final Semaphore mesasLibres = new Semaphore(CAPACIDAD_MESA);
    static final CountDownLatch clientesAtendidos = new CountDownLatch(CLIENTES);
    // Cola intermedia clientes -> camareros (ilimitada: los clientes nunca se quedan sin atender)
    static final BlockingQueue<Cliente> clientesEsperando = new ArrayBlockingQueue<Cliente>(CLIENTES);
    static final BlockingQueue<Pedido> pedidosParaCocina = new LinkedBlockingQueue<>();
    static final BlockingQueue<Pedido> pedidosTerminados = new LinkedBlockingQueue<>();

    // --- Estadisticas ---
    static final AtomicInteger platosServidos = new AtomicInteger();
    static final AtomicLong esperaTotalMs = new AtomicLong();
    static final AtomicInteger mesaLlenaCount = new AtomicInteger();
    static final AtomicLong cocinerosEsperandoMs = new AtomicLong();

    static String ahora() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RESTAURANTE CONCURRENTE ===");
        System.out.println("Iniciando servicio con " + COCINEROS + " cocineros y "
                + CAMAREROS + " camareros...\n");

        // --- Cocineros (consumidores) ---
        for (int i = 1; i <= COCINEROS; i++) {
            final int id = i;
            Thread.ofPlatform().name("cocinero-" + id).start(() -> {
                try {
                    while (true) {
                        long t0 = System.currentTimeMillis();
                        Pedido comanda = pedidosParaCocina.take();// espera si no hay pedidos
                        cocinerosEsperandoMs.addAndGet(System.currentTimeMillis() - t0);

                        Thread.sleep(comanda.cliente().plato().tiempoMs); // cocina el plato
                        esperaTotalMs.addAndGet(System.currentTimeMillis() - comanda.horaCreacionMs());
                        platosServidos.incrementAndGet();

                        pedidosTerminados.put(comanda);
                        System.out.println("[" + ahora() + "] Cocinero-" + id + " termina "
                                + comanda.cliente().plato() + " para Cliente-" + comanda.cliente().id);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // cierre del restaurante
                }
            });
        }

        // --- Camareros (productores) ---

        for (int i = 1; i <= CAMAREROS; i++) {
            final int id = i;
            Thread.ofPlatform().name("camarero-" + id).start(() -> {
                try {
                    while (true) {
                        Pedido platoListo = pedidosTerminados.poll();

                        if (platoListo != null) {
                            System.out.println("Camarero " + id + " sirve " + platoListo.cliente().plato() + " a cliente " + platoListo.cliente().id());
                            platoListo.cliente().comidaServida().complete(null);
                            continue;
                        }
                        Cliente clienteSinAtender = clientesEsperando.poll();

                        if (clienteSinAtender != null) {
                            System.out.println("Camarero " + id + " atiende a cliente " + clienteSinAtender.id());

                            Thread.sleep(TIEMPO_CAMARERO_MS); //Toma el pedido

                            pedidosParaCocina.put(new Pedido(clienteSinAtender, System.currentTimeMillis()));

                            continue;
                        }

                        Thread.sleep(50);


                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // --- Clientes (hilos virtuales, llegan cada 500 ms) ---
        Thread generadorClientes = Thread.ofPlatform().name("puerta").start(() -> {
            for (int i = 1; i <= CLIENTES; i++) {
                final int id = i;
                try {
                    mesasLibres.acquire();
                    TipoPlato plato = TipoPlato.aleatorio(new Random());
                    CompletableFuture<Void> busca = new CompletableFuture<>();
                    Cliente clienteActual = new Cliente(id, plato, System.currentTimeMillis(), busca);

                    Thread.sleep(LLEGADA_CLIENTE_MS);
                    clientesEsperando.put(clienteActual);

                    System.out.println("Cliente " + id + " se sienta y espera para pedir.");


                    System.out.println("[" + ahora() + "] Cliente-" + String.format("%03d", id)
                            + " pide " + plato);

                    clienteActual.comidaServida().join();

                    System.out.println("Cliente " + id + " empieza a comer su " + clienteActual.plato());
                    Thread.sleep(3000);

                    gananciasTotales.add(clienteActual.plato().precio);

                    clientesFinalizados.incrementAndGet();
                    System.out.println("Cliente " + id + " termina, paga " + clienteActual.plato().precio + "€ y se va.");


                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mesasLibres.release();
                    clientesAtendidos.countDown();
                }
            }
        });

        clientesAtendidos.await(); // el restaurante no cierra hasta atender a los 100
        generadorClientes.join();

        System.out.println("\n--- ESTADISTICAS FINALES ---");
        System.out.println("Clientes atendidos: " + CLIENTES + "/" + CLIENTES + " ✅");
        System.out.println("Platos servidos: " + platosServidos.get());
        System.out.println("Tiempo promedio de espera: "
                + (esperaTotalMs.get() / 1000.0 / CLIENTES) + "s");
        System.out.println("Mesa llena (veces): " + mesaLlenaCount.get());
        System.out.println("Cocineros esperando (tiempo acumulado): "
                + (cocinerosEsperandoMs.get() / 1000) + "s total");
        System.out.println("Eficiencia: el sistema soporta " + CAMAREROS + " pedidos/s en entrada"
                + " y " + COCINEROS + " platos en paralelo en cocina.");
        System.out.println("Ganancias totales: " + gananciasTotales.sum() + "€");
        System.exit(0); // cerrar los hilos camareros/cocineros (bucle infinito por disenyo)
    }

}
