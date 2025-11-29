/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 * Clase DTO utilizada para enviar actualizaciones periódicas del temporizador
 * del turno. Informa cuánto tiempo le queda al jugador activo.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class TurnoTickResponse implements Serializable {

    private String jugadorEnTurnoId;
    private int tiempoRestante;
    private EstadoPartida estadoPartida;

    /**
     * Constructor para inicializar el tick del turno.
     *
     * @param jugadorEnTurnoId ID del jugador que tiene el turno.
     * @param tiempoRestante Segundos restantes para finalizar el turno.
     */
    public TurnoTickResponse(String jugadorEnTurnoId, int tiempoRestante) {
        this.jugadorEnTurnoId = jugadorEnTurnoId;
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * Obtiene el ID del jugador en turno.
     *
     * @return String con el ID.
     */
    public String getJugadorEnTurnoId() {
        return jugadorEnTurnoId;
    }

    /**
     * Establece el ID del jugador en turno.
     *
     * @param jugadorEnTurnoId String con el ID.
     */
    public void setJugadorEnTurnoId(String jugadorEnTurnoId) {
        this.jugadorEnTurnoId = jugadorEnTurnoId;
    }

    /**
     * Obtiene el tiempo restante del turno.
     *
     * @return Entero con los segundos restantes.
     */
    public int getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Establece el tiempo restante.
     *
     * @param tiempoRestante Entero con los segundos.
     */
    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * Obtiene el estado actual de la partida.
     *
     * @return Enum EstadoPartida.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Establece el estado actual de la partida.
     *
     * @param estadoPartida Enum EstadoPartida.
     */
    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }
}
