/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.PosicionarNaveService;
import com.itson.equipo2.battleship_servidor.application.service.RealizarDisparoService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;

/**
 *
 * @author skyro
 */
public class PosicionarNaveHandler implements IMessageHandler {

    private final PosicionarNaveService service;
    private final Gson gson = new Gson();

    public PosicionarNaveHandler(PosicionarNaveService service) {
        this.service = service;
    }
    
    @Override
    public boolean canHandle(EventMessage message) {
        return "PosicionarFlota".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        PosicionarFlotaRequest req = gson.fromJson(message.getPayload(), PosicionarFlotaRequest.class);
        service.posicionarNaves(req);
    }
    
}
