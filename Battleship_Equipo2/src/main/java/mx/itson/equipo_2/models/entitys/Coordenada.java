/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;

import java.util.Objects;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class Coordenada {
    
    private int fila;
    private int columna;

    public Coordenada() {
    }

    
    public Coordenada(int fila, int columna) {
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
    public boolean equals(Object o) {
        // 1. Comprueba si es el mismo objeto en memoria
        if (this == o) return true;
        // 2. Comprueba si el otro objeto es nulo o de una clase diferente
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Convierte el objeto y compara los campos
        Coordenada that = (Coordenada) o;
        return fila == that.fila && columna == that.columna;
    }
    
        @Override
    public int hashCode() {
        // Genera un número único basado en los valores de fila y columna
        return Objects.hash(fila, columna);
    }
    
    

    @Override
    public String toString() {
        return "(" + fila + "," + columna + ")";
    }
    
    
    
}

