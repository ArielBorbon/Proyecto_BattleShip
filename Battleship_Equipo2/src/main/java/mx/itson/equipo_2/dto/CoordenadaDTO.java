/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.dto;

/**
 *
 * @author skyro
 */
public class CoordenadaDTO {

    private int fila;
    private int columna;

    public CoordenadaDTO() {
    }

    public CoordenadaDTO(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        return "CoordenadaDTO{" + "fila=" + fila + ", columna=" + columna + '}';
    }
}
