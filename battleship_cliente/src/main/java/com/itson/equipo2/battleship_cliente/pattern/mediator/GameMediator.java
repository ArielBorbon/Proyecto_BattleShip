package com.itson.equipo2.battleship_cliente.pattern.mediator;

import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private DisparoController partidaController;

    public DisparoController getPartidaController() {
        return partidaController;
    }

    
    
    
    
    public void setPartidaController(DisparoController controller) {
        this.partidaController = controller;
    }
    
    public void disparar(int columna, int fila) {
        if (partidaController != null) {
            partidaController.disparar(columna, fila);
            System.out.println("2");
        }
    }
}
