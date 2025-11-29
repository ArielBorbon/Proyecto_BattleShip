/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
 * Enumerador que define los tipos de barcos disponibles en el juego, junto con
 * sus características de tamaño y cantidad permitida.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public enum TipoNave {
    PORTA_AVIONES(4, 2),
    CRUCERO(3, 2),
    SUBMARINO(2, 4),
    BARCO(1, 3);

    private final int tamanio;
    private final int cantidadInicial;

    /**
     * Constructor del tipo de nave.
     *
     * @param tamanio Número de celdas que ocupa la nave.
     * @param cantidadInicial Cantidad de naves de este tipo por jugador.
     */
    private TipoNave(int tamanio, int cantidadInicial) {
        this.tamanio = tamanio;
        this.cantidadInicial = cantidadInicial;
    }

    /**
     * Obtiene el tamaño de la nave.
     *
     * @return Entero con el número de celdas.
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Obtiene la cantidad inicial permitida de este tipo de nave.
     *
     * @return Entero con la cantidad.
     */
    public int getCantidadInicial() {
        return cantidadInicial;
    }
}
