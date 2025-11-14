/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class DisparoRealizadoHandler implements IMessageHandler {

    private final Gson gson = new Gson();
    private final PartidaModel partidaModel;

    public DisparoRealizadoHandler(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "DisparoRealizado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Handler 'DisparoRealizado' activado.");

        // 1. Deserializar
        ResultadoDisparoReponse response = gson.fromJson(message.getPayload(), ResultadoDisparoReponse.class);

        // 2. DELEGAR (Una sola línea)
        // El Handler dice: "Modelo, llegó este resultado, actualízate tú mismo".
        partidaModel.procesarResultadoDisparo(response);
    }
}
