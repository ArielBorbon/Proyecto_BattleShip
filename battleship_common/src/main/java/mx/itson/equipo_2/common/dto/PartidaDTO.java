/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author CISCO
 */
public class PartidaDTO {

    private JugadorDTO jugadorEnTurno;
    private JugadorDTO jugador1;
    private JugadorDTO jugador2;
    private EstadoPartida estado;

    public PartidaDTO() {
    }

    public PartidaDTO(JugadorDTO jugadorEnTurno, JugadorDTO jugador1, JugadorDTO jugador2, EstadoPartida estado) {
        this.jugadorEnTurno = jugadorEnTurno;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estado = estado;
    }

    public JugadorDTO getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    public void setJugadorEnTurno(JugadorDTO jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
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

    @Override
    public String toString() {
        return "PartidaDTO{" + "jugadorEnTurno=" + jugadorEnTurno + ", jugador1=" + jugador1 + ", jugador2=" + jugador2 + ", estado=" + estado + '}';
    }

}
