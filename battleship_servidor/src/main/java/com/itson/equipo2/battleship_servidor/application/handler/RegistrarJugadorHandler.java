/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.handler;
import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.service.RegistrarJugadorService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;
/**
 *
 * @author Cricri
 */
public class RegistrarJugadorHandler implements IMessageHandler {
    private final RegistrarJugadorService service;
    private final Gson gson = new Gson();

    public RegistrarJugadorHandler(RegistrarJugadorService service) {
        this.service = service;
    }

    @Override
    public boolean canHandle(EventMessage message) {
      
        return "RegistrarJugador".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        
        System.out.println("Servidor: Recibido comando 'RegistrarJugador'");
        RegistrarJugadorRequest req = gson.fromJson(message.getPayload(), RegistrarJugadorRequest.class);
        
        
        service.execute(req);
    }
}
