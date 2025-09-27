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
public class Disparo {
  
    private ResultadoDisparo resultado;
    private Coordenada coordenada;

    public Disparo() {
    }
    

    public Disparo(ResultadoDisparo resultado, Coordenada coordenada) {
        this.resultado = resultado;
        this.coordenada = coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    @Override
    public String toString() {
        return "DisparoEntity{" + "resultado=" + resultado + ", coordenada=" + coordenada + '}';
    }
    
    
    
}


