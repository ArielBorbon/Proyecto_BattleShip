/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.util.List;
import mx.itson.equipo_2.common.dto.NaveDTO;

/**
 * Clase DTO utilizada para enviar la configuración inicial de la flota de un
 * jugador. Contiene la lista de naves y sus posiciones.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class PosicionarFlotaRequest {

    private String jugadorId;
    private List<NaveDTO> naves;

    /**
     * Constructor vacío por defecto.
     */
    public PosicionarFlotaRequest() {
    }

    /**
     * Constructor para inicializar la solicitud de posicionamiento.
     *
     * @param jugadorId Identificador del jugador dueño de la flota.
     * @param naves Lista de objetos NaveDTO con sus posiciones y orientaciones.
     */
    public PosicionarFlotaRequest(String jugadorId, List<NaveDTO> naves) {
        this.jugadorId = jugadorId;
        this.naves = naves;
    }

    /**
     * Obtiene el ID del jugador.
     *
     * * @return String con el ID.
     */
    public String getJugadorId() {
        return jugadorId;
    }

    /**
     * Establece el ID del jugador.
     *
     * @param jugadorId String con el ID.
     */
    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    /**
     * Obtiene la lista de naves configuradas.
     *
     * @return Lista de NaveDTO.
     */
    public List<NaveDTO> getNaves() {
        return naves;
    }

    /**
     * Establece la lista de naves.
     *
     * @param naves Lista de NaveDTO.
     */
    public void setNaves(List<NaveDTO> naves) {
        this.naves = naves;
    }

    /**
     * Representación en cadena del objeto.
     *
     * * @return String con el estado del objeto.
     */
    @Override
    public String toString() {
        return "PosicionarFlotaRequest{" + "jugadorId=" + jugadorId + ", naves=" + naves + '}';
    }
}
