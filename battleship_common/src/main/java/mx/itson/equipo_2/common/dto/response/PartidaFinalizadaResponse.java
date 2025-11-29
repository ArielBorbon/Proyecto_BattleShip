/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;

/**
 * Clase DTO de respuesta que notifica que una partida ha finalizado. Contiene
 * la información sobre quién ganó y la razón del fin del juego.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class PartidaFinalizadaResponse implements Serializable {

    private String ganadorId;
    private String motivo;

    /**
     * Constructor vacío por defecto.
     */
    public PartidaFinalizadaResponse() {
    }

    /**
     * Constructor completo para informar el fin de la partida.
     *
     * @param ganadorId ID del jugador que ha ganado.
     * @param motivo Descripción o razón de la victoria (ej. "Rendición", "Flota
     * destruida").
     */
    public PartidaFinalizadaResponse(String ganadorId, String motivo) {
        this.ganadorId = ganadorId;
        this.motivo = motivo;
    }

    /**
     * Obtiene el ID del ganador.
     *
     * @return String con el ID.
     */
    public String getGanadorId() {
        return ganadorId;
    }

    /**
     * Establece el ID del ganador.
     *
     * @param ganadorId String con el ID.
     */
    public void setGanadorId(String ganadorId) {
        this.ganadorId = ganadorId;
    }

    /**
     * Obtiene el motivo del fin de la partida.
     *
     * @return String con el motivo.
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Establece el motivo del fin de la partida.
     *
     * @param motivo String con el motivo.
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
