/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author PC Gamer
 */
public class DisparoRealizadoHandler implements IMessageHandler {

    private final Gson gson = new Gson();
    private final ViewController viewController;
    private final PartidaModel partidaModel;

    public DisparoRealizadoHandler(ViewController viewController, PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
        this.viewController = viewController;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "DisparoRealizado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {

        ResultadoDisparoReponse response = gson.fromJson(message.getPayload(), ResultadoDisparoReponse.class);
        partidaModel.procesarResultadoDisparo(response);

        if (partidaModel.getYo().getId().equals(response.getJugadorId())) {
            viewController.mostrarResultadoDisparo(response.getResultado());
        }

    }
}
