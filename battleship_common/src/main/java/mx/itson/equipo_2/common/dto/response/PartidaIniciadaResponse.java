/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author PC Gamer
 */
public class PartidaIniciadaResponse implements Serializable {
    private String partidaId;
    private JugadorDTO jugador1; // Humano
    private JugadorDTO jugador2; // IA
    private EstadoPartida estado;
    private String turnoActual; // ID del jugador en turno

    public PartidaIniciadaResponse(String partidaId, JugadorDTO jugador1, JugadorDTO jugador2, EstadoPartida estado, String turnoActual) {
        this.partidaId = partidaId;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estado = estado;
        this.turnoActual = turnoActual;
    }

    public String getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(String partidaId) {
        this.partidaId = partidaId;
    }

    public JugadorDTO getJugador1() {
        return jugador1;
    }

    public void setJugador1(JugadorDTO jugador1) {
        this.jugador1 = jugador1;
    }

    public JugadorDTO getJugador2() {
        return jugador2;
    }

    public void setJugador2(JugadorDTO jugador2) {
        this.jugador2 = jugador2;
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
    
    public JugadorDTO getYo(String miId) {
        return this.jugador1.getId().equals(miId) ? jugador1 : jugador2;
    }
    
    public JugadorDTO getEnemigo(String miId) {
        return this.jugador1.getId().equals(miId) ? jugador2 : jugador1;
    }
}
