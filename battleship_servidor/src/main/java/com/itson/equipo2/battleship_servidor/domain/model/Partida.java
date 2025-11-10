/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.UUID;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author Cricri
 */
public class Partida {
 
  private final UUID id;
    private Jugador jugador1;
    private Jugador jugador2;

    private Tablero tableroJugador1;
    private Tablero tableroJugador2;

    private EstadoPartida estado;
    private String turnoActual;  
    public Partida(Jugador jugador1) {
        this.id = UUID.randomUUID();
        this.jugador1 = jugador1;
        this.tableroJugador1 = new Tablero();
        this.estado = EstadoPartida.CONFIGURACION;
    }

    public void unirseAPartida(Jugador jugador2, String idTurnoInicial) {
        if (this.estado == EstadoPartida.CONFIGURACION) {
            this.jugador2 = jugador2;
            this.tableroJugador2 = new Tablero();
            this.estado = EstadoPartida.EN_BATALLA;
            this.turnoActual = idTurnoInicial; 
        } else {
            throw new IllegalStateException("No se puede unir a una partida que no esté en 'CONFIGURACION'.");
        }
    }

    public ResultadoDisparo realizarDisparo(String jugadorId, CoordenadaDTO coordenada) { 
        
        if (this.estado != EstadoPartida.EN_BATALLA) {
            throw new IllegalStateException("No se puede disparar en una partida que no está 'EN_BATALLA'. Estado actual: " + this.estado);
        }


        if (!this.turnoActual.equals(jugadorId)) {
            throw new IllegalStateException("No es el turno del jugador " + jugadorId);
        }

        Tablero tableroOponente;

        if (jugadorId.equals(jugador1.getId())) {
            tableroOponente = this.tableroJugador2;
        } else {
            tableroOponente = this.tableroJugador1;
        }

        ResultadoDisparo resultado = tableroOponente.recibirDisparo(coordenada);

        if (resultado == ResultadoDisparo.AGUA) {
            cambiarTurno();
        }

        if (tableroOponente.todasNavesHundidas()) {
            this.estado = EstadoPartida.FINALIZADA;
            System.out.println("¡Partida FINALIZADA! Ganador: " + jugadorId);
        }

        return resultado;
    }

    private void cambiarTurno() {
        if (this.turnoActual.equals(jugador1.getId())) {
            this.turnoActual = jugador2.getId();
        } else {
            this.turnoActual = jugador1.getId();
        }
        System.out.println("Turno cambiado a: " + this.turnoActual);
    }
   
    public UUID getId() {
        return id;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public Tablero getTableroJugador1() {
        return tableroJugador1;
    }

    public Tablero getTableroJugador2() {
        return tableroJugador2;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public String getTurnoActual() { 
        return turnoActual;
    }

    public void setTurnoActual(String turnoActual) { 
        this.turnoActual = turnoActual;
    }
}
