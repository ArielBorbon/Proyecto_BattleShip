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
 * Clase DTO que encapsula el resultado de un disparo realizado. Informa si
 * acertó, si hundió un barco y el estado consecuente del juego.
 *
 * * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class ResultadoDisparoReponse implements Serializable {

    private CoordenadaDTO coordenada;
    private ResultadoDisparo resultado;
    private String jugadorId; // El jugador que realizó el disparo

    private List<CoordenadaDTO> coordenadasBarcoHundido;
    private String turnoActual;
    private EstadoPartida estadoPartida;

    /**
     * Constructor completo con todos los detalles del disparo y actualización
     * de estado.
     *
     * @param coordenada Coordenada donde se disparó.
     * @param resultado Enum indicando AGUA, IMPACTO o HUNDIDO.
     * @param jugadorId ID del jugador que disparó.
     * @param coordenadasBarcoHundido Lista de coordenadas del barco si fue
     * hundido (null si no).
     * @param turnoActual ID del jugador que tiene el siguiente turno.
     * @param estadoPartida Estado actual de la partida.
     */
    public ResultadoDisparoReponse(CoordenadaDTO coordenada, ResultadoDisparo resultado, String jugadorId, List<CoordenadaDTO> coordenadasBarcoHundido, String turnoActual, EstadoPartida estadoPartida) {
        this.coordenada = coordenada;
        this.resultado = resultado;
        this.jugadorId = jugadorId;
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
        this.turnoActual = turnoActual;
        this.estadoPartida = estadoPartida;
    }

    /**
     * Constructor vacío por defecto.
     */
    public ResultadoDisparoReponse() {
    }

    /**
     * Constructor simplificado para resultados inmediatos sin cambios de estado
     * globales complejos.
     *
     * @param coordenada Coordenada del disparo.
     * @param resultado Resultado del disparo.
     * @param jugadorId ID del tirador.
     */
    public ResultadoDisparoReponse(CoordenadaDTO coordenada, ResultadoDisparo resultado, String jugadorId) {
        this.coordenada = coordenada;
        this.resultado = resultado;
        this.jugadorId = jugadorId;
    }

    /**
     * Obtiene las coordenadas del barco hundido, si aplica.
     *
     * @return Lista de CoordenadaDTO o null.
     */
    public List<CoordenadaDTO> getCoordenadasBarcoHundido() {
        return coordenadasBarcoHundido;
    }

    /**
     * Establece las coordenadas del barco hundido.
     *
     * @param coordenadasBarcoHundido Lista de CoordenadaDTO.
     */
    public void setCoordenadasBarcoHundido(List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    /**
     * Obtiene el ID del turno actual.
     *
     * @return String con el ID.
     */
    public String getTurnoActual() {
        return turnoActual;
    }

    /**
     * Establece el ID del turno actual.
     *
     * @param turnoActual String con el ID.
     */
    public void setTurnoActual(String turnoActual) {
        this.turnoActual = turnoActual;
    }

    /**
     * Obtiene el estado de la partida tras el disparo.
     *
     * @return Enum EstadoPartida.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Establece el estado de la partida.
     *
     * @param estadoPartida Enum EstadoPartida.
     */
    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    /**
     * Obtiene la coordenada del disparo.
     *
     * @return Objeto CoordenadaDTO.
     */
    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    /**
     * Establece la coordenada del disparo.
     *
     * @param coordenada Objeto CoordenadaDTO.
     */
    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    /**
     * Obtiene el resultado del disparo (Agua, Tocado, Hundido).
     *
     * @return Enum ResultadoDisparo.
     */
    public ResultadoDisparo getResultado() {
        return resultado;
    }

    /**
     * Establece el resultado del disparo.
     *
     * @param resultado Enum ResultadoDisparo.
     */
    public void setResultado(ResultadoDisparo resultado) {
        this.resultado = resultado;
    }

    /**
     * Obtiene el ID del jugador que disparó.
     *
     * @return String con el ID.
     */
    public String getJugadorId() {
        return jugadorId;
    }

    /**
     * Establece el ID del jugador que disparó.
     *
     * @param jugadorId String con el ID.
     */
    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Representación en cadena del resultado.
     *
     * @return String con los detalles del disparo y el juego.
     */
    @Override
    public String toString() {
        return "ResultadoDisparoReponse{" + "coordenada=" + coordenada + ", resultado=" + resultado + ", jugadorId=" + jugadorId + ", coordenadasBarcoHundido=" + coordenadasBarcoHundido + ", turnoActual=" + turnoActual + ", estadoPartida=" + estadoPartida + '}';
    }
}
