package com.itson.equipo2.battleship_cliente.pattern.mediator;

import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.exception.DisparoException;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private DisparoController partidaController;

    public void setPartidaController(DisparoController controller) {
        this.partidaController = controller;
    }
    
    public void disparar(JugadorModel jugador, CoordenadaDTO coord) {
        if (partidaController != null) {
            partidaController.disparar(jugador, coord);
        }
    }
}
