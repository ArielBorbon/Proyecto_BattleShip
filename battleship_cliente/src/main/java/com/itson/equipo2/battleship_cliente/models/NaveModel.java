/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author skyro
 */
public class NaveModel {

    private final TipoNave tipo;
    private final int columna;
    private final int fila;
    private final boolean esHorizontal;

    public NaveModel(TipoNave tipo, int fila, int columna, boolean esHorizontal) {
        this.tipo = tipo;
        this.columna = columna;
        this.fila = fila;
        this.esHorizontal = esHorizontal;
    }

    // Getters
    public TipoNave getTipo() {
        return tipo;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public boolean isEsHorizontal() {
        return esHorizontal;
    }
}
