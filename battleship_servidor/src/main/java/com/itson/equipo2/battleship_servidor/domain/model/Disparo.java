/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author skyro
 */
public class Disparo {

    private final Coordenada coordenada;
    private final ResultadoDisparo resultado;

    public Disparo(Coordenada coordenada, ResultadoDisparo resultado) {
        this.coordenada = coordenada;
        this.resultado = resultado;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return "Disparo{" + "coordenada=" + coordenada + ", resultado=" + resultado + '}';
    }

}
