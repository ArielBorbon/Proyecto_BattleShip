/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 * Clase DTO de respuesta enviada al comenzar una partida. Contiene el estado
 * inicial, los jugadores involucrados y de quién es el turno.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class PartidaIniciadaResponse implements Serializable {

    private String partidaId;
    private JugadorDTO jugador1; // Humano
    private JugadorDTO jugador2; // IA
    private EstadoPartida estado;
    private String turnoActual; // ID del jugador en turno

    /**
     * Constructor para inicializar los datos de la partida iniciada.
     *
     * @param partidaId Identificador único de la partida.
     * @param jugador1 Objeto DTO del primer jugador (usualmente el humano).
     * @param jugador2 Objeto DTO del segundo jugador (usualmente la IA).
     * @param estado Estado actual de la partida.
     * @param turnoActual ID del jugador que tiene el primer turno.
     */
    public PartidaIniciadaResponse(String partidaId, JugadorDTO jugador1, JugadorDTO jugador2, EstadoPartida estado, String turnoActual) {
        this.partidaId = partidaId;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estado = estado;
        this.turnoActual = turnoActual;
    }

    /**
     * Obtiene el ID de la partida.
     *
     * @return String con el ID.
     */
    public String getPartidaId() {
        return partidaId;
    }

    /**
     * Establece el ID de la partida.
     *
     * @param partidaId String con el ID.
     */
    public void setPartidaId(String partidaId) {
        this.partidaId = partidaId;
    }

    /**
     * Obtiene el DTO del jugador 1.
     *
     * @return JugadorDTO del jugador 1.
     */
    public JugadorDTO getJugador1() {
        return jugador1;
    }

    /**
     * Establece el DTO del jugador 1.
     *
     * @param jugador1 JugadorDTO.
     */
    public void setJugador1(JugadorDTO jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * Obtiene el DTO del jugador 2.
     *
     * @return JugadorDTO del jugador 2.
     */
    public JugadorDTO getJugador2() {
        return jugador2;
    }

    /**
     * Establece el DTO del jugador 2.
     *
     * @param jugador2 JugadorDTO.
     */
    public void setJugador2(JugadorDTO jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * Obtiene el estado de la partida.
     *
     * @return Enum EstadoPartida.
     */
    public EstadoPartida getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la partida.
     *
     * @param estado Enum EstadoPartida.
     */
    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el ID del jugador que tiene el turno actual.
     *
     * @return String con el ID.
     */
    public String getTurnoActual() {
        return turnoActual;
    }

    /**
     * Establece el ID del jugador que tiene el turno actual.
     *
     * @param turnoActual String con el ID.
     */
    public void setTurnoActual(String turnoActual) {
        this.turnoActual = turnoActual;
    }

    /**
     * Método utilitario para obtener el objeto JugadorDTO propio basándose en
     * un ID.
     *
     * @param miId El ID del jugador que solicita la información.
     * @return El JugadorDTO que corresponde al ID proporcionado.
     */
    public JugadorDTO getYo(String miId) {
        return this.jugador1.getId().equals(miId) ? jugador1 : jugador2;
    }

    /**
     * Método utilitario para obtener el objeto JugadorDTO del oponente
     * basándose en el ID propio.
     *
     * @param miId El ID del jugador que solicita la información.
     * @return El JugadorDTO del contrincante.
     */
    public JugadorDTO getEnemigo(String miId) {
        return this.jugador1.getId().equals(miId) ? jugador2 : jugador1;
    }
}
