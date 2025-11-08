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
public class RealizarDisparoRequest  implements Serializable{

    private UUID partidaId;
    private UUID jugadorId;
    private CoordenadaDTO coordenada;

    public RealizarDisparoRequest() {
    }

    public RealizarDisparoRequest(UUID partidaId, UUID jugadorId, CoordenadaDTO coordenada) {
        this.partidaId = partidaId;
        this.jugadorId = jugadorId;
        this.coordenada = coordenada;
    }

    
    public UUID getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(UUID partidaId) {
        this.partidaId = partidaId;
    }

    public UUID getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(UUID jugadorId) {
        this.jugadorId = jugadorId;
    }

    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

}
