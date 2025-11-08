/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author CISCO
 */
public class PosicionarNaveRequest {

    private String jugadorId;
    private CoordenadaDTO[] coordenadas;
    private TipoNave tipoNave;

    public PosicionarNaveRequest() {
    }

    public PosicionarNaveRequest(String jugadorId, CoordenadaDTO[] coordenadas, TipoNave tipoNave) {
        this.jugadorId = jugadorId;
        this.coordenadas = coordenadas;
        this.tipoNave = tipoNave;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public CoordenadaDTO[] getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(CoordenadaDTO[] coordenadas) {
        this.coordenadas = coordenadas;
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public void setTipoNave(TipoNave tipoNave) {
        this.tipoNave = tipoNave;
    }

    @Override
    public String toString() {
        return "PosicionarNaveRequest{" + "jugadorId=" + jugadorId + ", coordenadas=" + coordenadas + ", tipoNave=" + tipoNave + '}';
    }

}
