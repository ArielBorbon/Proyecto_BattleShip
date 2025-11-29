/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.response;

/**
 * Clase DTO de respuesta para confirmar que un jugador ha terminado de posicionar sus naves.
 * * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class NavesPosicionadasResponse {

    private final String idJugador;

    /**
     * Constructor que confirma el posicionamiento de las naves.
     * @param idJugador ID del jugador que ha finalizado el posicionamiento.
     */
    public NavesPosicionadasResponse(String idJugador) {
        this.idJugador = idJugador;
    }

    // Nota: Como la variable es final y no hay getter explícito en el código original, 
    // se asume que su uso es interno o se accede mediante reflexión/serialización,
    // pero idealmente debería tener un getter público.
}