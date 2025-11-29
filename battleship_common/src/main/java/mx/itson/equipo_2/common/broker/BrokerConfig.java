/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.broker;

/**
 * Clase de configuración que contiene las constantes para la comunicación del
 * Message Broker (ej. nombres de canales o tópicos).
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class BrokerConfig {

    /**
     * Canal utilizado para enviar comandos de juego.
     */
    public static final String CHANNEL_COMANDOS = "battleship:comandos";

    /**
     * Canal utilizado para difundir eventos del juego.
     */
    public static final String CHANNEL_EVENTOS = "battleship:eventos";
}
