/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class CeldaModel {

    private final CoordenadaModel coordenada;

    private EstadoCelda estadoDisparo; // NO_DISPARADA, DISPARADA

    private TipoNave tipoNave; // Qué nave hay aquí (null si es agua)
    private EstadoNave estadoNave; // Cuál es su estado (INTACTO, AVERIADO, HUNDIDO)

    // Constructor limpio
    public CeldaModel(int fila, int col) {
        this.coordenada = new CoordenadaModel(fila, col);
        this.estadoDisparo = EstadoCelda.NO_DISPARADA;
        this.tipoNave = null;
        this.estadoNave = null;
    }

    // --- Getters para la Vista (onChange) ---
    public EstadoCelda getEstado() {
        return estadoDisparo;
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public boolean tieneNave() {
        return tipoNave != null; // Se calcula en lugar de almacenarse
    }

    // --- Setters para el Controlador (lógica de 'agregarNave') ---
    public void setTipoNave(TipoNave tipoNave) {
        this.tipoNave = tipoNave;
    }

    public void setEstadoNave(EstadoNave estadoNave) {
        this.estadoNave = estadoNave;
    }

    // Getters y Setters de Disparo (para el futuro)
    public EstadoCelda getEstadoDisparo() {
        return estadoDisparo;
    }

    public void setEstadoDisparo(EstadoCelda estadoDisparo) {
        this.estadoDisparo = estadoDisparo;
    }

    public EstadoNave getEstadoNave() {
        return estadoNave;
    }

    public CoordenadaModel getCoordenada() {
        return coordenada;
    }

}
