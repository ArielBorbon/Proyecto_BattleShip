/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.service.RealizarDisparoService;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.message.EventMessage;

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
