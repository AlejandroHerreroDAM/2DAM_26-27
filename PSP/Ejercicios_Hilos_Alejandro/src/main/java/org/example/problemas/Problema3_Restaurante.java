package org.example.problemas;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * Restaurante con los clientes esperando en una mesa a comer, con cola de espera de clientes para sentarse en la mesa,
 * y conteo de ganancias en el restaurante por día, así como media de tiempo de espera de los clientes.
 * Los camareros piden pedidos y sirven platos a los clientes.
 */

public class Problema3_Restaurante {

    enum TipoPlato {
        ENSALADA(2000, 8),
        PASTA(3000, 12),
        PIZZA(4000, 15),
        CARNE(5000, 20);

        private final int tiempoMs;
        private final double precio;

        TipoPlato(int tiempoMs, double precio) {
            this.tiempoMs = tiempoMs;
            this.precio = precio;
        }

        static TipoPlato getRandom(Random random) {
            return values()[random.nextInt(values().length)];
        }
    }

    static final DoubleAdder gananciasTotales = new DoubleAdder();

    static final AtomicLong tiempoEsperaTotalMs = new AtomicLong();

    static final AtomicInteger clientesFinalizados = new AtomicInteger();

    record Cliente(int id, TipoPlato plato, long horaLlegada, CompletableFuture<Void> comidaServida) {}

    record Pedido(Cliente cliente) {}

    //RECURSOS COMPARTIDOS

    static final int COCINEROS = 3;
    static final int CAMAREROS = 5;
    static final int CLIENTES = 10;
    static final int MESAS = 10;

    static final Semaphore mesasLibres = new Semaphore(MESAS);
    static final CountDownLatch clientesAtendidos = new CountDownLatch(CLIENTES);

    static final BlockingQueue<Cliente> clientesEsperando = new LinkedBlockingQueue<>();
    static final BlockingQueue<Pedido> pedidosParaCocina = new LinkedBlockingQueue<>();
    static final BlockingQueue<Pedido> pedidosTerminados = new LinkedBlockingQueue<>();


    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();

        for (int i = 1; i <= COCINEROS; i++) {
            final int idCocinero = i;

            Thread.ofPlatform().name("Cocinero " + idCocinero).start(()->{
                try{

                    while (true){
                        Pedido comanda = pedidosParaCocina.take();
                        System.out.println("Cocinero " + idCocinero+ " empieza a cocinar "+comanda.cliente().plato()+ " para el cliente "+comanda.cliente().id());

                        Thread.sleep(comanda.cliente().plato().tiempoMs);

                        pedidosTerminados.put(comanda);
                        System.out.println("Cocinero " + idCocinero+ " termina "+comanda.cliente().plato()+ " para el cliente "+comanda.cliente().id());
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            });
        }


        for (int i = 1; i <= CAMAREROS; i++) {
            final int idCamarero = i;

            Thread.ofPlatform().name("Camarero "+idCamarero).start(()-> {

                try {
                    while (true) {
                        Pedido platoListo = pedidosTerminados.poll();

                        if (platoListo != null) {
                            System.out.println("Camarero "+ idCamarero + " sirve "+platoListo.cliente().plato()+ " a cliente "+ platoListo.cliente().id());
                            platoListo.cliente().comidaServida().complete(null);

                            continue;
                        }

                        Cliente clienteSinAtender = clientesEsperando.poll();

                        if (clienteSinAtender != null) {
                            System.out.println("Camarero "+idCamarero+ " atiende a cliente "+clienteSinAtender.id());

                            Thread.sleep(1000);

                            pedidosParaCocina.put(new Pedido(clienteSinAtender));

                            continue;
                        }

                        Thread.sleep(50);

                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            });
        }



        for (int i = 1; i <= CLIENTES; i++) {

            final int idCliente = i;

            Thread.ofVirtual().name("Cliente " + idCliente).start(()-> {
                try{
                    mesasLibres.acquire();

                    TipoPlato plato = TipoPlato.getRandom(random);

                    CompletableFuture<Void> busca = new CompletableFuture<>();
                    Cliente clienteActual = new Cliente(idCliente,plato,System.currentTimeMillis(),busca);

                    System.out.println("Cliente "+ idCliente + " se sienta y espera para pedir.");
                    clientesEsperando.put(clienteActual);

                    clienteActual.comidaServida().join();

                    System.out.println("Cliente "+ idCliente + " empieza a comer su "+clienteActual.plato());
                    Thread.sleep(3000 + random.nextInt(3000));

                    gananciasTotales.add(clienteActual.plato().precio);

                    long tiempoEspera = System.currentTimeMillis() - clienteActual.horaLlegada();
                    tiempoEsperaTotalMs.addAndGet(tiempoEspera);

                    clientesFinalizados.incrementAndGet();
                    System.out.println("Cliente "+ idCliente + " termina, paga "+ clienteActual.plato().precio + "€ y se va.");

                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally {
                    mesasLibres.release();
                    clientesAtendidos.countDown();
                }

            });

        }

        clientesAtendidos.await();

        System.out.println("---FIN DEL PROGRAMA---");
        System.out.println("Ganancias totales: "+gananciasTotales.sum()+"€");
        System.out.println("Tiempo medio de espera: "+(tiempoEsperaTotalMs.get()/CLIENTES)+" ms");

    }




}
