/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author skyro
 */
public class NavesPosicionadasHandler implements IMessageHandler {
    
    private final Gson gson = new Gson();
    private final ViewController viewController;

    public NavesPosicionadasHandler(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "NavesPosicionadas".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        
        viewController.cambiarPantalla("esperandoPosicionamiento");
    }
    
    
}
