/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

import java.awt.Color;

/**
 *
 * @author skyro
 */
public enum ColorJugador {

    ROJO(Color.RED),
    AZUL(Color.BLUE),
    VERDE(Color.GREEN),
    AMARILLO(Color.YELLOW),
    NARANJA(Color.ORANGE),
    MORADO(Color.MAGENTA);

    private Color color;

    private ColorJugador(Color color) {
        this.color = color;
    }
    
    

    public Color getColor() {
        return color;
    }

}
