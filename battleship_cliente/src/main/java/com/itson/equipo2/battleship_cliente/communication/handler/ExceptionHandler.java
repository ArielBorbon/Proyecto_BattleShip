/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.communication.handler;

import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class ExceptionHandler implements IMessageHandler {

    @Override
    public void onMessage(EventMessage message) {
        if (true) {
            
        }
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "EXCEPTION".equals(message.getEventType());
    }
    
}
