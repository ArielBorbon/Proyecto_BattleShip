/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

/**
 * Clase DTO simple que representa una posición (fila, columna) en el tablero.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class CoordenadaDTO {

    private int fila;
    private int columna;

    /**
     * Constructor vacío por defecto.
     */
    public CoordenadaDTO() {
    }

    /**
     * Constructor para inicializar una coordenada.
     *
     * @param fila Índice de la fila (generalmente 0-9).
     * @param columna Índice de la columna (generalmente 0-9).
     */
    public CoordenadaDTO(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Obtiene el índice de la fila.
     *
     * @return Entero con la fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Establece el índice de la fila.
     *
     * @param fila Entero con la fila.
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Obtiene el índice de la columna.
     *
     * @return Entero con la columna.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece el índice de la columna.
     *
     * @param columna Entero con la columna.
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Representación en cadena de la coordenada.
     *
     * @return String en formato {fila=X, columna=Y}.
     */
    @Override
    public String toString() {
        return "CoordenadaDTO{" + "fila=" + fila + ", columna=" + columna + '}';
    }
}
