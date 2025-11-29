/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

import java.awt.Color;

/**
 * Enumerador que define los colores disponibles para los jugadores. Asocia cada
 * opción con un objeto {@link java.awt.Color} para su representación gráfica.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public enum ColorJugador {

    ROJO(Color.RED),
    AZUL(Color.BLUE),
    VERDE(Color.GREEN),
    AMARILLO(Color.YELLOW),
    NARANJA(new Color(252, 144, 3)),
    MORADO(Color.MAGENTA);

    private final Color color;

    /**
     * Constructor del enumerador.
     *
     * @param color Objeto Color de AWT asociado.
     */
    private ColorJugador(Color color) {
        this.color = color;
    }

    /**
     * Obtiene el objeto Color asociado para uso en componentes gráficos.
     *
     * @return Objeto java.awt.Color.
     */
    public Color getColor() {
        return color;
    }
}
