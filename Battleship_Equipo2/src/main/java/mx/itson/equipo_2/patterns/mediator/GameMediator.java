
package mx.itson.equipo_2.patterns.mediator;

import mx.itson.equipo_2.controller.PartidaController;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.exception.DisparoException;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private PartidaController partidaController;

    public void setPartidaController(PartidaController controller) {
        this.partidaController = controller;
    }
    
    public void disparar(Jugador jugador, CoordenadaDTO coord) throws DisparoException {
        if (partidaController != null) {
            partidaController.disparar(jugador, coord);
        }
    }
}
