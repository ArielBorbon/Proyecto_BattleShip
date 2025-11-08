
package com.itson.equipo2.battleship_cliente.pattern.strategy;

import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
/**
 *
 * @author PC Gamer
 */
public class StrategyTurnoHumano implements StrategyTurno {

    @Override
    public void ejecutarTurno(PartidaModel partidaModel, DisparoController partidaController) {
      
        System.out.println("Turno del jugador humano. Esperando acción...");
    }
}
