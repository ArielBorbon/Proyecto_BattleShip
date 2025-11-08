/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;
import java.util.UUID;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author CISCO
 */
public class ResultadoDisparoDTO implements Serializable{
 
    private CoordenadaDTO coordenada;
    private ResultadoDisparo resultado;
    private UUID jugadorId; // El jugador que realizó el disparo

   
    public ResultadoDisparoDTO() {
    }

    public ResultadoDisparoDTO(CoordenadaDTO coordenada, ResultadoDisparo resultado, UUID jugadorId) {
        this.coordenada = coordenada;
        this.resultado = resultado;
        this.jugadorId = jugadorId;
    }

  
    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoDisparo resultado) {
        this.resultado = resultado;
    }

    public UUID getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(UUID jugadorId) {
        this.jugadorId = jugadorId;
    }
}
