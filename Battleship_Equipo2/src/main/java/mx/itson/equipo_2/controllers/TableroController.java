
package mx.itson.equipo_2.controllers;

import mx.itson.equipo_2.models.*;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.views.DispararView;
/**
 *
 * @author skyro
 */
public class TableroController {
   


    private final DispararView view;
    private final TableroModel miTablero;
    private final TableroModel tableroEnemigo;
    private final PartidaModel partida;
    private final Jugador jugador;

    public TableroController(DispararView view, TableroModel miTablero, TableroModel tableroEnemigo,
                             PartidaModel partida, Jugador jugador) {
        this.view = view;
        this.miTablero = miTablero;
        this.tableroEnemigo = tableroEnemigo;
        this.partida = partida;
        this.jugador = jugador;

        
        this.view.setJugador(jugador);
        this.view.setTableros(miTablero, tableroEnemigo);

 
        this.miTablero.addObserver(view);
        this.tableroEnemigo.addObserver(view);
        this.partida.addObserver(view);

        
        this.view.setListenerDisparo(coord -> realizarDisparo(coord));
    }

    public void realizarDisparo(Coordenada coord) {
        try {
            ResultadoDisparo resultado = partida.realizarDisparo(jugador, coord);
            System.out.println("Disparo de " + jugador.getNombre() + " en " + coord + " -> " + resultado);
        } catch (Exception ex) {
            view.mostrarError(ex.getMessage());
        }
    }
}


