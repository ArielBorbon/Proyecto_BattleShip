/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;

/**
 *
 * @author PC Gamer
 */

public class TurnoTickResponse implements Serializable {

    private String jugadorEnTurnoId;
    private int tiempoRestante;

    public TurnoTickResponse(String jugadorEnTurnoId, int tiempoRestante) {
        this.jugadorEnTurnoId = jugadorEnTurnoId;
        this.tiempoRestante = tiempoRestante;
    }

    public String getJugadorEnTurnoId() {
        return jugadorEnTurnoId;
    }

    public void setJugadorEnTurnoId(String jugadorEnTurnoId) {
        this.jugadorEnTurnoId = jugadorEnTurnoId;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
}
