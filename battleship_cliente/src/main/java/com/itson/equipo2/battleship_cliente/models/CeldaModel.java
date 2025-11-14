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

    private int columna;
    private int fila;
    private boolean tieneNave;

    private EstadoCelda estadoDisparo; // NO_DISPARADA, DISPARADA

    // --- ESTOS SON LOS CAMBIOS ---
    private TipoNave tipoNave; // Qué nave hay aquí (null si es agua)
    private EstadoNave estadoNave; // Cuál es su estado (null si es agua)

    public CeldaModel() {
    }

    public CeldaModel(int fila, int col, boolean tieneNave, EstadoCelda estado) {
        // (Este constructor se vuelve obsoleto, pero lo dejamos por ahora)
        this.fila = fila;
        this.columna = col;
        this.estadoDisparo = estado;
        this.tipoNave = null;
        this.estadoNave = null;
    }

    // Constructor limpio (el que TableroModel debe usar)
    public CeldaModel(int fila, int col) {
        this.fila = fila;
        this.columna = col;
        this.estadoDisparo = EstadoCelda.NO_DISPARADA;
        this.tipoNave = null;
        this.estadoNave = null;
    }

    // --- Getters para la Vista ---
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public EstadoCelda getEstado() {
        return estadoDisparo;
    } // Renombrado para claridad

    public EstadoNave getEstadoNave() {
        return estadoNave;
    } // <-- ¡EL GETTER CLAVE!

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    // Método 'isTieneNave()' ahora es "derivado" (calculado)
    public boolean isTieneNave() {
        return tipoNave != null;
    }

    // --- Setters para el Modelo ---
    public void setEstado(EstadoCelda estado) {
        this.estadoDisparo = estado;
    }

    public void setTipoNave(TipoNave tipo) {
        this.tipoNave = tipo;
    }

    public void setEstadoNave(EstadoNave estado) {
        this.estadoNave = estado;
    } // <-- ¡EL SETTER CLAVE!
}
