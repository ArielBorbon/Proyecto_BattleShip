/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

import java.io.Serializable;
import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author CISCO
 */
public class ResultadoDisparoReponse implements Serializable{
 
    private CoordenadaDTO coordenada;
    private ResultadoDisparo resultado;
    private String jugadorId; // El jugador que realizó el disparo
    
    private List<CoordenadaDTO> coordenadasBarcoHundido; // Añadir si no está
    private String turnoActual; 
    private EstadoPartida estadoPartida;

    public ResultadoDisparoReponse(CoordenadaDTO coordenada, ResultadoDisparo resultado, String jugadorId, List<CoordenadaDTO> coordenadasBarcoHundido, String turnoActual, EstadoPartida estadoPartida) {
        this.coordenada = coordenada;
        this.resultado = resultado;
        this.jugadorId = jugadorId;
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
        this.turnoActual = turnoActual;
        this.estadoPartida = estadoPartida;
    }

   
    
    
    
    public ResultadoDisparoReponse() {
    }

    public ResultadoDisparoReponse(CoordenadaDTO coordenada, ResultadoDisparo resultado, String jugadorId) {
        this.coordenada = coordenada;
        this.resultado = resultado;
        this.jugadorId = jugadorId;
    }

    public List<CoordenadaDTO> getCoordenadasBarcoHundido() {
        return coordenadasBarcoHundido;
    }

    public void setCoordenadasBarcoHundido(List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    public String getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(String turnoActual) {
        this.turnoActual = turnoActual;
    }

    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
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

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }
}
