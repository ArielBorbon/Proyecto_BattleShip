
package mx.itson.equipo_2.patterns.mediator;

import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private PartidaController controller;

    public void setController(PartidaController controller) {
        this.controller = controller;
    }

    public void notificarDisparo(Jugador jugador, Coordenada coordenada) {
        if (controller != null) {
            controller.solicitarDisparo(jugador, coordenada);
        }
    }
}
