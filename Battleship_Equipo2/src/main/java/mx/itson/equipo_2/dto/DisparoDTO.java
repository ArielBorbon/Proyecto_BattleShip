/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

import mx.itson.equipo_2.models.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public class DisparoDTO {
    
    private ResultadoDisparo resultado;
    private CoordenadaDTO coordenada;

    public DisparoDTO() {
    }

    public DisparoDTO(ResultadoDisparo resultado, CoordenadaDTO coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
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
    
    
}
