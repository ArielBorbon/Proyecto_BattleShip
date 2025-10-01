
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
        
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada x = (Coordenada) o;
        return fila == x.fila && columna == x.columna;
    }
    
        @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }
    
    

    @Override
    public String toString() {
        return "(" + fila + "," + columna + ")";
    }
    
    
    
}

