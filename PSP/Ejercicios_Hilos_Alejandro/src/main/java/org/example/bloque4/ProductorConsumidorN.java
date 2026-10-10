package org.example.bloque4;

public class ProductorConsumidorN {

    private int buffer = 0;

    public synchronized void producir(int valor) throws InterruptedException {
        while (buffer != 0){
            wait();
        }
        buffer = valor;
        System.out.println("Producido "+valor);
        notifyAll();
    }

    public synchronized void consumir() throws InterruptedException {
        while (buffer == 0){
            wait();
        }
        int valor = buffer;
        System.out.println("Consumido "+valor);
        buffer = 0;
        notifyAll();
    }


    public static void main(String[] args) {
        ProductorConsumidorN pc = new ProductorConsumidorN();

        Thread hiloProductor = new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    pc.producir(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread hiloConsumidor = new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    pc.consumir();
                } catch (InterruptedException e) {}
            }
        });
        hiloProductor.start();
        hiloConsumidor.start();



    }

}
