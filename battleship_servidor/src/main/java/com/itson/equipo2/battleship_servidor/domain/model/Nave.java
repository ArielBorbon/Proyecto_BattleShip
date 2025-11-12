/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.OrientacionNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author Cricri
 */
public class Nave {
   private final TipoNave tipo;
    private final int longitud;
    private int impactosRecibidos;
    
    
    private List<CoordenadaDTO> coordenadas; // Guardar las coordenadas
    private OrientacionNave orientacion;

    public Nave(TipoNave tipo) {
        this.tipo = tipo;
        this.longitud = tipo.getTamanio();
        this.impactosRecibidos = 0;
    }

    public Nave(TipoNave tipo, int longitud) {
        this.tipo = tipo;
        this.longitud = longitud;
    }

    public List<CoordenadaDTO> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<CoordenadaDTO> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public OrientacionNave getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(OrientacionNave orientacion) {
        this.orientacion = orientacion;
    }
    
    
    
    
    // Constructor para el posicionamiento
    public Nave(TipoNave tipo, List<CoordenadaDTO> coordenadas, OrientacionNave orientacion) {
        this.tipo = tipo;
        this.longitud = tipo.getTamanio();
        this.impactosRecibidos = 0;
        this.coordenadas = coordenadas;
        this.orientacion = orientacion;
    }
    

    
public void recibirImpacto() {
        if (impactosRecibidos < longitud) {
            this.impactosRecibidos++;
        }
    }

    public void setImpactosRecibidos(int impactosRecibidos) {
        this.impactosRecibidos = impactosRecibidos;
    }

   
    public boolean estaHundida() {
        return this.impactosRecibidos >= this.longitud;
    }

 
    public TipoNave getTipo() { return tipo; }
    public int getLongitud() { return longitud; }
    public int getImpactosRecibidos() { return impactosRecibidos; }
}
