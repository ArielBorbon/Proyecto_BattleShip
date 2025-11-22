/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
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

    private EstadoPartida estado;
    private String turnoActual;
    public static final int DURACION_TURNO = 30;
    private int tiempoRestante;

    public Partida(Jugador jugador1) {
        this.id = UUID.randomUUID();
        this.jugador1 = jugador1;
        this.estado = EstadoPartida.CONFIGURACION;
    }

    public void unirseAPartida(Jugador jugador2, String idTurnoInicial) {
        if (this.estado == EstadoPartida.CONFIGURACION) {
            this.jugador2 = jugador2;
            this.estado = EstadoPartida.EN_BATALLA;
            this.turnoActual = idTurnoInicial;
            iniciarTurno();
        } else {
            throw new IllegalStateException("No se puede unir a una partida que no esté en 'CONFIGURACION'.");
        }
    }

    public ResultadoDisparoReponse realizarDisparo(String jugadorId, CoordenadaDTO coordenada) {

        if (this.estado != EstadoPartida.EN_BATALLA) {
            throw new IllegalStateException("No se puede disparar en una partida que no está 'EN_BATALLA'. Estado actual: " + this.estado);
        }

        if (!this.turnoActual.equals(jugadorId)) {
            throw new IllegalStateException("No es el turno del jugador " + jugadorId);
        }

        Tablero tableroOponente;
        if (jugadorId.equals(jugador1.getId())) {
            tableroOponente = this.getJugador2().getTablero();
        } else {
            tableroOponente = this.getJugador1().getTablero();
        }

        ResultadoDisparo resultado = tableroOponente.recibirDisparo(coordenada);
        List<Coordenada> coordsHundidas = null;

        if (resultado == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO) {
            Nave naveHundida = tableroOponente.getCelda(coordenada.getFila(), coordenada.getColumna()).getNave();
            coordsHundidas = (naveHundida != null) ? naveHundida.getCoordenadas() : new ArrayList<>();
        }

        if (resultado == ResultadoDisparo.AGUA) {
            cambiarTurno();
        } else {
            iniciarTurno(); // <-- AÑADIR
        }

        if (tableroOponente.todasNavesHundidas()) {
            this.estado = EstadoPartida.FINALIZADA;
            System.out.println("¡Partida FINALIZADA! Ganador: " + jugadorId);
        }

        List<CoordenadaDTO> coords = coordsHundidas == null
                ? List.of()
                : coordsHundidas.stream()
                        .map(c -> new CoordenadaDTO(c.getFila(), c.getColumna()))
                        .toList();

        ResultadoDisparoReponse response = new ResultadoDisparoReponse();
        response.setJugadorId(jugadorId);
        response.setCoordenada(coordenada);
        response.setResultado(resultado);
        response.setCoordenadasBarcoHundido(coords);
        response.setTurnoActual(this.turnoActual);
        response.setEstadoPartida(this.estado);

        return response;
    }

    public void cambiarTurno() {
        System.out.println(turnoActual);
        if (this.turnoActual.equals(jugador1.getId())) {
            this.turnoActual = jugador2.getId();
        } else {
            this.turnoActual = jugador1.getId();
        }
        System.out.println(turnoActual);
        iniciarTurno();
        System.out.println("Turno cambiado a: " + this.turnoActual);
    }

    public void posicionarNaves(String jugadorId, List<Nave> naves) {
        Tablero tablero = (jugadorId.equals(jugador1.getId())) ? jugador1.getTablero() : jugador2.getTablero();
        if (tablero == null) {
            return;
        }

        int n = 1;
        for (Nave nave : naves) {
            tablero.posicionarNave(nave);
            System.out.println("---------------");
            System.out.println("Nave posicionada:" + n);
            System.out.println("Tipo: " + nave.getTipo());
            System.out.println("Coordendas: " + nave.getCoordenadas().toString());
            System.out.println("---------------");
        }
    }
    
    public void finalizarPartida(String jugadorQueAbandonaId) {
        if (this.estado == EstadoPartida.FINALIZADA) {
            return;
        }

        System.out.println("Procesando abandono para: " + jugadorQueAbandonaId);
        this.estado = EstadoPartida.FINALIZADA;
        
        if (this.jugador1 != null && this.jugador1.getId().equals(jugadorQueAbandonaId)) {
            if (this.jugador2 != null) this.turnoActual = this.jugador2.getId();
        } else if (this.jugador2 != null && this.jugador2.getId().equals(jugadorQueAbandonaId)) {
            this.turnoActual = this.jugador1.getId();
        }
        
        System.out.println("Partida FINALIZADA por abandono. Ganador: " + this.turnoActual);
    }

    public Jugador getEnemigo(String miId) {
        return jugador1.getId().equals(miId) ? jugador2 : jugador1;
    }
    public Jugador getJugadorById(String id) {
        return this.jugador1.getId().equals(id) ? jugador1 : jugador2;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * Reinicia el contador de tiempo a 30 segundos.
     */
    public void iniciarTurno() {
        this.tiempoRestante = DURACION_TURNO;
    }

    /**
     * Decrementa el tiempo en 1 segundo y devuelve el valor restante.
     *
     * @return el tiempo restante.
     */
    public int decrementarYObtenerTiempo() {
        if (this.tiempoRestante > 0) {
            this.tiempoRestante--;
        }
        return this.tiempoRestante;
    }

    public int getTiempoRestante() {
        return this.tiempoRestante;
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
