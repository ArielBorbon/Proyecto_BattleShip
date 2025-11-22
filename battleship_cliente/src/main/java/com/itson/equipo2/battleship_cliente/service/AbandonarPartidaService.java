/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.AbandonarPartidaRequest;

/**
 *
 * @author Alberto Jimenez
 */
public class AbandonarPartidaService {
    
    private final IMessagePublisher publisher;
    private final JugadorModel jugadorModel;
    private final Gson gson = new Gson();

    public AbandonarPartidaService(IMessagePublisher publisher, JugadorModel jugadorModel) {
        this.publisher = publisher;
        this.jugadorModel = jugadorModel;
    }

    public void abandonar() {
        AbandonarPartidaRequest request = new AbandonarPartidaRequest(jugadorModel.getId());
        String payload = gson.toJson(request);
        
        EventMessage message = new EventMessage("AbandonarPartida", payload);
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
        System.out.println("Enviando comando AbandonarPartida...");
    }
    
}
