/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author Cricri
 */
public class Nave {
   private final TipoNave tipo;
    private final int longitud;
    private int impactosRecibidos;

    public Nave(TipoNave tipo) {
        this.tipo = tipo;
        this.longitud = tipo.getTamanio();
        this.impactosRecibidos = 0;
    }

    
    public void recibirImpacto() {
        this.impactosRecibidos++;
    }

   
    public boolean estaHundida() {
        return this.impactosRecibidos >= this.longitud;
    }

 
    public TipoNave getTipo() { return tipo; }
    public int getLongitud() { return longitud; }
    public int getImpactosRecibidos() { return impactosRecibidos; }
}
