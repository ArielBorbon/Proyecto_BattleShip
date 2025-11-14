/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
 *
 * @author skyro
 */
public enum ColorJugador {

    ROJO("#FF0000"),
    AZUL("#0000FF"),
    VERDE("#00FF00"),
    AMARILLO("#FFFF00"),
    NARANJA("#FFA500"),
    MORADO("#800080");

    private String color;

    private ColorJugador(String color) {
        this.color = color;
    }
    
    

    public String getColor() {
        return color;
    }

}
