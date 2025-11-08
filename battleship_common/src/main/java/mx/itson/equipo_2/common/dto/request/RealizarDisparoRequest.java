/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

/**
 *
 * @author CISCO
 */
public class RealizarDisparoRequest {

    private int columna;
    private int fila;

    public RealizarDisparoRequest() {
    }

    public RealizarDisparoRequest(int columna, int fila) {
        this.columna = columna;
        this.fila = fila;
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

    @Override
    public String toString() {
        return "RealizarDisparoRequest{" + "columna=" + columna + ", fila=" + fila + '}';
    }

}
