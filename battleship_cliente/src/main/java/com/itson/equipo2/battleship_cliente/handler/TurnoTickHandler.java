/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class TurnoTickHandler implements IMessageHandler {

    private final Gson gson = new Gson();

    // 1. Declaramos la dependencia como final
    private final PartidaModel partidaModel;

    // 2. Inyectamos la dependencia por el constructor
    public TurnoTickHandler(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "TurnoTick".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        try {
            // 1. Deserializar el mensaje
            TurnoTickResponse response = gson.fromJson(message.getPayload(), TurnoTickResponse.class);

            // 2. DELEGAR LÓGICA DE NEGOCIO AL MODELO
            //    El modelo se actualizará y notificará a las vistas (Observer)
            partidaModel.actualizarTick(response);

        } catch (Exception e) {
            System.err.println("Error en TurnoTickHandler: " + e.getMessage());
        }
    }
}
