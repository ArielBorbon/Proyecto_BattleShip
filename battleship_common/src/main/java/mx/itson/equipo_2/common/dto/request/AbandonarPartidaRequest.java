/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;

/**
 * Clase DTO (Data Transfer Object) que representa la solicitud de un jugador
 * para abandonar una partida en curso. Implementa Serializable para facilitar
 * su transporte a través de la red.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class AbandonarPartidaRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String jugadorId;

    /**
     * Constructor vacío por defecto.
     */
    public AbandonarPartidaRequest() {
    }

    /**
     * Constructor que inicializa la solicitud con el ID del jugador.
     *
     * @param jugadorId El identificador único del jugador que abandona.
     */
    public AbandonarPartidaRequest(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Obtiene el ID del jugador.
     *
     * @return El identificador del jugador.
     */
    public String getJugadorId() {
        return jugadorId;
    }

    /**
     * Establece el ID del jugador.
     *
     * @param jugadorId El identificador del jugador a establecer.
     */
    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }
}
