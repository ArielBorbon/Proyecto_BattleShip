/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.util.List;
import mx.itson.equipo_2.common.dto.NaveDTO;

/**
 *
 * @author CISCO
 */
public class PosicionarFlotaRequest { // Nota el nuevo nombre

    private String jugadorId;
    private List<NaveDTO> naves; // <-- ¡El cambio clave! Una lista de naves.

    public PosicionarFlotaRequest() {
    }

    public PosicionarFlotaRequest(String jugadorId, List<NaveDTO> naves) {
        this.jugadorId = jugadorId;
        this.naves = naves;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public List<NaveDTO> getNaves() {
        return naves;
    }

    public void setNaves(List<NaveDTO> naves) {
        this.naves = naves;
    }

    @Override
    public String toString() {
        return "PosicionarFlotaRequest{" + "jugadorId=" + jugadorId + ", naves=" + naves + '}';
    }
}
