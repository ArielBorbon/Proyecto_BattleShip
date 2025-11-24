package com.itson.equipo2.battleship_cliente.pattern.mediator;

import com.itson.equipo2.battleship_cliente.controllers.AbandonarController;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private DisparoController disparoController;
    private AbandonarController abandonarController;
    
    public DisparoController getPartidaController() {
        return disparoController;
    }

    public AbandonarController getAbandonarController() {
        return abandonarController;
    }

    public void setAbandonarController(AbandonarController abandonarController) {
        this.abandonarController = abandonarController;
    }
    
    public void disparar(int fila, int columna) {
        if (disparoController != null) {
            disparoController.disparar(fila, columna);
        }
    }
    
    public void abandonarPartida(){
        if (abandonarController != null) {
            abandonarController.abandonar();
        }
    }
    
    public void setPartidaController(DisparoController controller) {
        this.disparoController = controller;
    }
}
