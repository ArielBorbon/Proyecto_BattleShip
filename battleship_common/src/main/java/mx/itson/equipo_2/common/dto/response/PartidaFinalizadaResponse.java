/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;

/**
 *
 * @author Alberto Jimenez
 */
public class PartidaFinalizadaResponse implements Serializable {

    private String ganadorId;
    private String motivo;

    public PartidaFinalizadaResponse() {
    }

    public PartidaFinalizadaResponse(String ganadorId, String motivo) {
        this.ganadorId = ganadorId;
        this.motivo = motivo;
    }

    public String getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(String ganadorId) {
        this.ganadorId = ganadorId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
