/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
 * Enumerador que representa el estado individual de un jugador dentro del ciclo
 * de vida del juego.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public enum EstadoJugador {

    /**
     * El jugador se encuentra colocando sus naves en el tablero.
     */
    POSICIONANDO,
    /**
     * El jugador ha terminado de configurar y espera el inicio.
     */
    LISTO,
    /**
     * El jugador está activamente jugando sus turnos.
     */
    EN_BATALLA,
    /**
     * Estado general de participación en una partida activa.
     */
    EN_PARTIDA;
}
