/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Partida;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class PartidaModel {

    private final Partida partida;
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final TableroModel tableroModel1;
    private final TableroModel tableroModel2;

    public PartidaModel(Jugador j1, Jugador j2) {
        this.partida = new Partida(j1, j2);

        // --- MODIFICADO: Inicialización directa ---
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.tableroModel1 = new TableroModel(j1.getTablero());
        this.tableroModel2 = new TableroModel(j2.getTablero());
    }

    public boolean verificarTurno(Jugador j) {
        return partida.getJugadorEnTurno().equals(j);
    }

    public void cambiarTurno() {
        partida.cambiarTurno();
    }

    // --- MODIFICADO: Devuelve la entidad Jugador ---
    public Jugador obtenerOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? jugador2 : jugador1;
    }

    // --- NUEVO: Método para obtener el TableroModel del oponente ---
    public TableroModel obtenerTableroOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? tableroModel2 : tableroModel1;
    }

    public ResultadoDisparo realizarDisparo(Jugador atacante, Coordenada coord) {
        if (!verificarTurno(atacante)) {
            throw new IllegalStateException("No es el turno de " + atacante.getNombre());
        }

        // --- MODIFICADO: Obtener el TableroModel del oponente ---
        TableroModel oponenteTablero = obtenerTableroOponente(atacante);

        if (!oponenteTablero.validarCoordenada(coord)) {
            throw new IllegalArgumentException("Coordenada inválida: " + coord);
        }

        ResultadoDisparo resultado = oponenteTablero.recibirDisparo(coord);

        // --- MODIFICADO: Registrar el disparo directamente en el jugador ---
        Disparo disparo = new Disparo(resultado, coord);
        atacante.agregarDisparo(disparo); // Mucho más simple

        if (resultado == ResultadoDisparo.AGUA) {
            cambiarTurno();
        }

        return resultado;
    }

    public boolean partidaFinalizada() {
      
        return tableroModel1.todasNavesHundidas() || tableroModel2.todasNavesHundidas();
        
    }

    public Jugador getJugadorEnTurno() {
        return partida.getJugadorEnTurno();
    }

    public Partida getPartida() {
        return partida;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public TableroModel getTableroModel1() {
        return tableroModel1;
    }

    public TableroModel getTableroModel2() {
        return tableroModel2;
    }

}
