/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;

/**
 * Clase DTO que encapsula la información necesaria para realizar un disparo en
 * el juego de Battleship. Contiene quién dispara y a dónde dispara.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class RealizarDisparoRequest implements Serializable {

    private String jugadorId;
    private CoordenadaDTO coordenada;

    /**
     * Constructor vacío por defecto.
     */
    public RealizarDisparoRequest() {
    }

    /**
     * Constructor completo para inicializar la solicitud de disparo.
     *
     * @param jugadorId Identificador del jugador que realiza el disparo.
     * @param coordenada Objeto DTO con las coordenadas (X, Y) del disparo.
     */
    public RealizarDisparoRequest(String jugadorId, CoordenadaDTO coordenada) {
        this.jugadorId = jugadorId;
        this.coordenada = coordenada;
    }

    /**
     * Obtiene el ID del jugador que dispara.
     *
     * @return El ID del jugador.
     */
    public String getJugadorId() {
        return jugadorId;
    }

    /**
     * Establece el ID del jugador que dispara.
     *
     * @param jugadorId El ID del jugador.
     */
    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Obtiene las coordenadas del objetivo.
     *
     * @return Objeto CoordenadaDTO con la posición.
     */
    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    /**
     * Establece las coordenadas del objetivo.
     *
     * @param coordenada Objeto CoordenadaDTO con la posición.
     */
    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    /**
     * Representación en cadena del objeto.
     *
     * * @return String con los valores de jugadorId y coordenada.
     */
    @Override
    public String toString() {
        return "RealizarDisparoRequest{" + "jugadorId=" + jugadorId + ", coordenada=" + coordenada + '}';
    }
}
