/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 * Clase DTO que representa el estado completo de una partida de Battleship.
 * Contiene la información de los jugadores y el estado actual del juego.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class PartidaDTO {

    private JugadorDTO jugadorEnTurno;
    private JugadorDTO jugador1;
    private JugadorDTO jugador2;
    private EstadoPartida estado;

    /**
     * Constructor vacío por defecto.
     */
    public PartidaDTO() {
    }

    /**
     * Constructor para inicializar una partida con todos sus componentes.
     *
     * @param jugadorEnTurno El jugador que posee el turno actual.
     * @param jugador1 El primer jugador (anfitrión).
     * @param jugador2 El segundo jugador (retador o IA).
     * @param estado El estado actual de la partida (ej. JUGANDO, FINALIZADA).
     */
    public PartidaDTO(JugadorDTO jugadorEnTurno, JugadorDTO jugador1, JugadorDTO jugador2, EstadoPartida estado) {
        this.jugadorEnTurno = jugadorEnTurno;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estado = estado;
    }

    /**
     * Obtiene el jugador que tiene el turno activo.
     *
     * @return JugadorDTO con el jugador en turno.
     */
    public JugadorDTO getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    /**
     * Establece el jugador que tiene el turno activo.
     *
     * @param jugadorEnTurno JugadorDTO a establecer.
     */
    public void setJugadorEnTurno(JugadorDTO jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
    }

    /**
     * Obtiene el jugador 1.
     *
     * @return JugadorDTO del jugador 1.
     */
    public JugadorDTO getJugador1() {
        return jugador1;
    }

    /**
     * Establece el jugador 1.
     *
     * @param jugador1 JugadorDTO a establecer.
     */
    public void setJugador1(JugadorDTO jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * Obtiene el jugador 2.
     *
     * @return JugadorDTO del jugador 2.
     */
    public JugadorDTO getJugador2() {
        return jugador2;
    }

    /**
     * Establece el jugador 2.
     *
     * @param jugador2 JugadorDTO a establecer.
     */
    public void setJugador2(JugadorDTO jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * Obtiene el estado general de la partida.
     *
     * @return Enum EstadoPartida.
     */
    public EstadoPartida getEstado() {
        return estado;
    }

    /**
     * Establece el estado general de la partida.
     *
     * @param estado Enum EstadoPartida.
     */
    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    /**
     * Representación en cadena de la partida.
     *
     * @return String con los detalles de la partida.
     */
    @Override
    public String toString() {
        return "PartidaDTO{" + "jugadorEnTurno=" + jugadorEnTurno + ", jugador1=" + jugador1 + ", jugador2=" + jugador2 + ", estado=" + estado + '}';
    }
}
