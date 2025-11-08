/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import mx.itson.equipo_2.common.enums.EstadoCelda;

/**
 *
 * @author skyro
 */
public class CeldaModel {

    private int columna;
    private int fila;
    private boolean tieneNave;
    private EstadoCelda estado;

    public CeldaModel() {
    }

    public CeldaModel(int columna, int fila, boolean tieneNave, EstadoCelda estado) {
        this.columna = columna;
        this.fila = fila;
        this.tieneNave = tieneNave;
        this.estado = estado;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    public boolean isTieneNave() {
        return tieneNave;
    }

    public void setTieneNave(boolean tieneNave) {
        this.tieneNave = tieneNave;
    }

    @Override
    public String toString() {
        return "CeldaModel{" + "columna=" + columna + ", fila=" + fila + ", tieneNave=" + tieneNave + ", estado=" + estado + '}';
    }

}
