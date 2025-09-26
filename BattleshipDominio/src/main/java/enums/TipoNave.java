/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public enum TipoNave {
    PORTA_AVIONES(5),
    CRUCERO(4),
    SUBMARINO(3),
    BARCO(2);

    private final int tamanio;

    TipoNave(int tamanio) {
        this.tamanio = tamanio;
    }

    public int getTamanio() {
        return tamanio;
    }
}
