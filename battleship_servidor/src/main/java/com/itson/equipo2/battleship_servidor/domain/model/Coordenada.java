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

    private int columna;
    private int fila;

    public Coordenada(int columna, int fila) {
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
        return "Coordenada{" + "columna=" + columna + ", fila=" + fila + '}';
    }

}
