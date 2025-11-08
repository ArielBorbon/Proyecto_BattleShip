/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.communication.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.ErrorResponse;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class ExceptionHandler implements IMessageHandler {

    private final ViewController ViewController;
    private final Gson gson = new Gson();

    public ExceptionHandler(ViewController ViewController) {
        this.ViewController = ViewController;
    }
    
    @Override
    public void onMessage(EventMessage message) {
        ErrorResponse error = gson.fromJson(message.getPayload(), ErrorResponse.class);
        ViewController.mostrarError(error.getMessage());
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "EXCEPTION".equals(message.getEventType());
    }
    
}
