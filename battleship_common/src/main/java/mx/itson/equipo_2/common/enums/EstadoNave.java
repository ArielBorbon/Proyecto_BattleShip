/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mx.itson.equipo_2.common.enums;

/**
 * Enumerador que indica la condición de salud de una nave.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public enum EstadoNave {
    /**
     * La nave ha recibido impactos pero sigue a flote.
     */
    AVERIADO,
    /**
     * Todas las casillas de la nave han sido impactadas.
     */
    HUNDIDO,
    /**
     * La nave no ha recibido ningún impacto.
     */
    SIN_DANIOS
}
