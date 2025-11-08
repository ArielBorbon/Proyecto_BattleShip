/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;
import java.util.UUID;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;

/**
 *
 * @author CISCO
 */
public class RealizarDisparoRequest implements Serializable {

    private String jugadorId;
    private CoordenadaDTO coordenada;

    public RealizarDisparoRequest() {
    }

    public RealizarDisparoRequest(String jugadorId, CoordenadaDTO coordenada) {
        this.jugadorId = jugadorId;
        this.coordenada = coordenada;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    @Override
    public String toString() {
        return "RealizarDisparoRequest{" + "jugadorId=" + jugadorId + ", coordenada=" + coordenada + '}';
    }

}
