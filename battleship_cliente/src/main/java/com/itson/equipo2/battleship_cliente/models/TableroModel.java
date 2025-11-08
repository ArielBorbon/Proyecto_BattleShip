/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

/**
 *
 * @author skyro
 */
public class TableroModel {

    private CeldaModel[][] celdas;

    public TableroModel() {
    }

    public TableroModel(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(CeldaModel[][] celdas) {
        this.celdas = celdas;
    }

    public CeldaModel getCelda(int fila, int columna) {
        return celdas[fila][columna];
    }
    @Override
    public String toString() {
        return "TableroModel{" + "celdas=" + celdas + '}';
    }

}
