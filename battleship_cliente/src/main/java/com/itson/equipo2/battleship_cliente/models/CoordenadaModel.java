/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

/**
 *
 * @author skyro
 */
public class CoordenadaModel {

    private final int columna;
    private final int fila;

    public CoordenadaModel(int fila, int columna) {
        this.columna = columna;
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    @Override
    public String toString() {
        return "CoordenadaModel{" + "columna=" + columna + ", fila=" + fila + '}';
    }

}
