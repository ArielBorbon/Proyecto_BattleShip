
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.AbandonarPartidaService;
import com.itson.equipo2.battleship_cliente.service.SalaService;
import com.itson.equipo2.communication.impl.redis.RedisConfig;

/**
 *
 * @author PC Gamer
 */
public class SalaController {

    private final SalaService salaService;
    private final VistaController viewController;
    private final AbandonarPartidaService abandonarService; 
    private final PartidaModel partidaModel;

    public SalaController(SalaService salaService, VistaController viewController, AbandonarPartidaService abandonarService, PartidaModel partidaModel) {
        this.salaService = salaService;
        this.viewController = viewController;
        this.abandonarService = abandonarService;
        this.partidaModel = partidaModel;
    }

    public void iniciarJuego() {
        salaService.enviarInicioJuego();
    }

    public void volverAlMenu() {
        viewController.cambiarPantalla("menu");
    }

    public void volverAlLobby() {
        if (partidaModel.getYo() != null && partidaModel.getYo().getId() != null) {
            System.out.println("Enviando petición de abandonar sala...");
            abandonarService.abandonarPartida(partidaModel.getYo().getId());
        }

        partidaModel.reiniciarPartida();

        viewController.cambiarPantalla("lobby");
    }
    
    public String getIpServidorConectada() {
        String ip = partidaModel.getIpServidor();

        return (ip != null) ? ip : "localhost";
    }
}
