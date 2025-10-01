
package mx.itson.equipo_2.patterns.Strategy;

import mx.itson.equipo_2.controller.PartidaController;
import mx.itson.equipo_2.models.PartidaModel;

/**
 *
 * @author PC Gamer
 */
public class StrategyTurnoHumano implements StrategyTurno {

    @Override
    public void ejecutarTurno(PartidaModel partidaModel, PartidaController partidaController) {
      
        System.out.println("Turno del jugador humano. Esperando acción...");
    }
}
