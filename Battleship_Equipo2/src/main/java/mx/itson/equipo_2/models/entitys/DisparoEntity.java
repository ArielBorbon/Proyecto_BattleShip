/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;

import mx.itson.equipo_2.models.enums.ResultadoDisparo;



/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class DisparoEntity {
  
    private ResultadoDisparo resultado;
    private CoordenadaEntity coordenada;

    public DisparoEntity() {
    }
    

    public DisparoEntity(ResultadoDisparo resultado, CoordenadaEntity coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public CoordenadaEntity getCoordenada() {
        return coordenada;
    }

    @Override
    public String toString() {
        return "DisparoEntity{" + "resultado=" + resultado + ", coordenada=" + coordenada + '}';
    }
    
    
    
}


