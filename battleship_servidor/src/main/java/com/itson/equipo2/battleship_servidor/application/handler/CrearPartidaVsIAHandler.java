/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.CrearPartidaVsIAService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import com.itson.equipo2.communication.dto.EventMessage;

public class CrearPartidaVsIAHandler implements IMessageHandler {

    private final CrearPartidaVsIAService service;
    private final Gson gson = new Gson();

    public CrearPartidaVsIAHandler(CrearPartidaVsIAService service) {
        this.service = service;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "CrearPartidaVsIA".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        CrearPartidaVsIARequest req = gson.fromJson(message.getPayload(), CrearPartidaVsIARequest.class);
        service.execute(req);
    }
}