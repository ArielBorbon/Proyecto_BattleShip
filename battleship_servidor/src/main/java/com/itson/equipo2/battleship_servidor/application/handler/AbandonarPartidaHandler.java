/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.AbandonarPartidaService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.request.AbandonarPartidaRequest;

/**
 *
 * @author Alberto Jimenez
 */
public class AbandonarPartidaHandler implements IMessageHandler{
    
    private final AbandonarPartidaService service;
    private final Gson gson = new Gson();

    public AbandonarPartidaHandler(AbandonarPartidaService service) {
        this.service = service;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "AbandonarPartida".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Servidor: Recibido comando AbandonarPartida.");
        try {
            AbandonarPartidaRequest request = gson.fromJson(message.getPayload(), AbandonarPartidaRequest.class);
            service.procesarAbandono(request);
        } catch (Exception e) {
            System.err.println("Error procesando abandono: " + e.getMessage());
        }
    }
}
