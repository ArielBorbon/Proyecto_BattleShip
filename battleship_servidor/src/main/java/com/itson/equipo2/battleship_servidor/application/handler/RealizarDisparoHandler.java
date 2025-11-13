/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.RealizarDisparoService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author skyro
 */
public class RealizarDisparoHandler implements IMessageHandler {

    private final RealizarDisparoService service;
    private final Gson gson = new Gson();

    public RealizarDisparoHandler(RealizarDisparoService service) {
        this.service = service;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "RealizarDisparo".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        RealizarDisparoRequest req = gson.fromJson(message.getPayload(), RealizarDisparoRequest.class);
        service.realizarDisparo(req);
    }
}