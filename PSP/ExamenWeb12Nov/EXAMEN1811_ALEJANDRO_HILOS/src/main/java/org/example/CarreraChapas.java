package org.example;

import org.example.model.Jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarreraChapas {

//    Se simula una carrera de chapas con N jugadores, cada uno en un hilo. Por rondas,
//    cada jugador lanza su chapa avanzando una distancia aleatoria entre 1 y 20. Los
//    lanzamientos de una ronda se hacen de uno en uno y en orden (jugador 0, 1, 2...).
//    Cuando todos han lanzado, termina la ronda y se muestra la clasificación. Gana quien,
//    al terminar una ronda, haya alcanzado la meta (100); si hay varios, el que esté más
//    lejos. Para q el publico siga la partida hay un juez sacando mensajes a una pizarra,
//    esos mensajes le llegan de una cola de mensajes, que los jugadores al final de cada
//    tirada llenan con su posición. Al final del turno tb se pondrá un mensaje de fin de turno
//    y el juez cuando vea ese mensaje en la cola, asyncronamente pedirá a los jugadores
//    por donde van y mostrará el resumen.
//    Puntos clave:
//  - Los turnos se completan siempre.
//  - La comprobacion de quien gana se hace al final de un turno.
//  - Solo el juez pone cosas en la pizarra(consola)
//  - Los mensaje de de la cola de mensajes son de informacion de jugadores, o el de
//    fin de turno.
//  - Los hilos tendrán que pararse cuando toque para que el programa se cierre.
//  - PISTA: como se puede hacer algo que sirva para sincronizar pasos de n hilos
//    para que ninguno siga antes de que lleguen los demas? Hay un constructor de
//    ese algo que puede ejecutar una accion(runnable) cada vez que se llegan todos
//    a cada paso.

    public void main(String[] args){
        int NUM_JUGADORES = 5;

        int NUM_META= 100;


        List<Thread> threads = new ArrayList<>();


        for (int i = 0; i <= NUM_JUGADORES; i++ ){
            Jugador jugador = new Jugador(i);


            Thread hilo = Thread.ofVirtual().start(()->{
                jugador.tiraDado();
                

            });

        }


    }




}
