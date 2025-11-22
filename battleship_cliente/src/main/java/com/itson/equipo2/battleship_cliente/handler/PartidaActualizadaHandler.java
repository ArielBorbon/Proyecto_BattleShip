/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.PartidaDTO;

/**
 *
 * @author PC Gamer
 */
public class PartidaActualizadaHandler implements IMessageHandler {

    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public PartidaActualizadaHandler(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaActualizada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Cliente: Recibido evento 'PartidaActualizada'. Sincronizando...");

        PartidaDTO dto = gson.fromJson(message.getPayload(), PartidaDTO.class);

        // Actualizamos el modelo con la información que llega del servidor
        partidaModel.sincronizarDatosSala(dto);
    }
}
