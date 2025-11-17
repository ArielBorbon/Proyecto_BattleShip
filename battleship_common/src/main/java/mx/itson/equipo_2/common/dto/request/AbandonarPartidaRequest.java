/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;

/**
 *
 * @author Alberto Jimenez
 */
public class AbandonarPartidaRequest implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String jugadorId;

    public AbandonarPartidaRequest() {
    }

    public AbandonarPartidaRequest(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }
}
