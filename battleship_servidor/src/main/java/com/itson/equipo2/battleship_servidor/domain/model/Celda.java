/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import mx.itson.equipo_2.common.enums.EstadoCelda;

/**
 *
 * @author Cricri
 */
public class Celda {

    private Coordenada coordenada;
    private EstadoCelda estado;
    private Nave nave;

    public Celda(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.estado = EstadoCelda.NO_DISPARADA;
        this.nave = null;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public Nave getNave() {
        return nave;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }
}
