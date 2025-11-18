/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
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

    private TipoNave(int tamanio, int cantidadInicial) {
        this.tamanio = tamanio;
        this.cantidadInicial = cantidadInicial;
    }

    public int getTamanio() {
        return tamanio;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }

}
