/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import java.util.List;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 * Clase DTO que representa un disparo realizado y sus consecuencias inmediatas.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class DisparoDTO {

    private ResultadoDisparo resultado;
    private CoordenadaDTO coordenada;

    private List<CoordenadaDTO> coordenadasBarcoHundido;

    /**
     * Constructor vacío por defecto.
     */
    public DisparoDTO() {
    }

    /**
     * Constructor básico para un disparo que no hunde un barco.
     *
     * @param resultado Resultado del disparo (AGUA o TOCADO).
     * @param coordenada Coordenada donde se efectuó el disparo.
     */
    public DisparoDTO(ResultadoDisparo resultado, CoordenadaDTO coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
        this.coordenadasBarcoHundido = null;
    }

    /**
     * Constructor completo para un disparo que hunde un barco.
     *
     * @param resultado Resultado del disparo (HUNDIDO).
     * @param coordenada Coordenada del impacto final.
     * @param coordenadasBarcoHundido Lista de coordenadas que ocupaba el barco
     * hundido.
     */
    public DisparoDTO(ResultadoDisparo resultado, CoordenadaDTO coordenada, List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.resultado = resultado;
        this.coordenada = coordenada;
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    /**
     * Obtiene el resultado del disparo.
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
     * Obtiene las coordenadas del barco hundido, si aplica.
     *
     * @return Lista de coordenadas o null.
     */
    public List<CoordenadaDTO> getCoordenadasBarcoHundido() {
        return coordenadasBarcoHundido;
    }

    /**
     * Establece las coordenadas del barco hundido.
     *
     * @param coordenadasBarcoHundido Lista de coordenadas.
     */
    public void setCoordenadasBarcoHundido(List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    /**
     * Representación en cadena del disparo.
     *
     * @return String con el resultado y coordenada.
     */
    @Override
    public String toString() {
        return "DisparoDTO{" + "resultado=" + resultado + ", coordenada=" + coordenada + '}';
    }
}
