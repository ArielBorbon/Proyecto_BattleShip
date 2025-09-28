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
 * @author skyro,Jose Aguilar
 */
public class PartidaModel {
  
    private final Partida partida;
    private final JugadorModel jugador1;
    private final JugadorModel jugador2;

    public PartidaModel(Jugador j1, Jugador j2) {
        this.partida = new Partida(j1, j2);
        this.jugador1 = new JugadorModel(j1);
        this.jugador2 = new JugadorModel(j2);
    }

    public boolean verificarTurno(Jugador j) {
        return partida.getJugadorEnTurno().equals(j);
    }

    public void cambiarTurno() {
        partida.cambiarTurno();
    }

 
    public JugadorModel obtenerOponente(Jugador atacante) {
        return atacante.equals(jugador1.getJugador()) ? jugador2 : jugador1;
    }

    public ResultadoDisparo realizarDisparo(Jugador atacante, Coordenada coord) {
        if (!verificarTurno(atacante)) {
            throw new IllegalStateException("No es el turno de " + atacante.getNombre());
        }

        JugadorModel oponente = obtenerOponente(atacante);

        if (!oponente.getTableroModel().validarCoordenada(coord)) {
            throw new IllegalArgumentException("Coordenada inválida: " + coord);
        }

       
        ResultadoDisparo resultado = oponente.getTableroModel().recibirDisparo(coord);

        
        Disparo disparo = new Disparo(resultado, coord);
        if (atacante.equals(jugador1.getJugador())) {
            jugador1.registrarDisparo(disparo);
        } else {
            jugador2.registrarDisparo(disparo);
        }


        if (resultado == ResultadoDisparo.AGUA) {
            cambiarTurno();
        }

        return resultado;
    }

    public boolean partidaFinalizada() {
        return jugador1.getTableroModel().todasNavesHundidas() ||
               jugador2.getTableroModel().todasNavesHundidas();
    }
}
