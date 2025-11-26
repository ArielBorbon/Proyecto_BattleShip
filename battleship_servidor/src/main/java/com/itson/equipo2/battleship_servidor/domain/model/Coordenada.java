/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

/**
 *
 * @author skyro
 */
public class Coordenada {

    private final int fila;
    private final int columna;

    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    @Override
    public String toString() {
        return "Coordenada{" + "fila=" + fila + ", columna=" + columna + '}';
    }

}
