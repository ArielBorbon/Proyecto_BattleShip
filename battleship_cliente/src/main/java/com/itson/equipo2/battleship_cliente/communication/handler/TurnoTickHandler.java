/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.communication.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.utils.AppContext;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class TurnoTickHandler implements IMessageHandler {

    private final Gson gson = new Gson();

    @Override
    public boolean canHandle(EventMessage message) {
        return "TurnoTick".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        try {
            TurnoTickResponse response = gson.fromJson(message.getPayload(), TurnoTickResponse.class);
            PartidaModel model = AppContext.getPartidaModel();
            
            // Actualizar el estado local
            model.setTurnoDe(response.getJugadorEnTurnoId());
            model.setSegundosRestantes(response.getTiempoRestante());
            
            // Notificar a la vista (DispararView) para que se repinte
            model.notifyObservers();
            
        } catch (Exception e) {
            System.err.println("Error en TurnoTickHandler: " + e.getMessage());
        }
    }
}
