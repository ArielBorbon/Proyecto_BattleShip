/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

import java.util.List;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public class DisparoDTO {

    private ResultadoDisparo resultado;
    private CoordenadaDTO coordenada;

    private List<CoordenadaDTO> coordenadasBarcoHundido;

    public DisparoDTO() {
    }

    public DisparoDTO(ResultadoDisparo resultado, CoordenadaDTO coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
        this.coordenadasBarcoHundido = null;
    }

    public DisparoDTO(ResultadoDisparo resultado, CoordenadaDTO coordenada, List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.resultado = resultado;
        this.coordenada = coordenada;
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoDisparo resultado) {
        this.resultado = resultado;
    }

    public CoordenadaDTO getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(CoordenadaDTO coordenada) {
        this.coordenada = coordenada;
    }

    @Override
    public String toString() {
        return "DisparoDTO{" + "resultado=" + resultado + ", coordenada=" + coordenada + '}';
    }

    public List<CoordenadaDTO> getCoordenadasBarcoHundido() {
        return coordenadasBarcoHundido;
    }

    public void setCoordenadasBarcoHundido(List<CoordenadaDTO> coordenadasBarcoHundido) {
        this.coordenadasBarcoHundido = coordenadasBarcoHundido;
    }

    
    
    
    
}
