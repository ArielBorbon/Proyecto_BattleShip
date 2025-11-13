/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.communication.broker;

import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author skyro
 */
public interface IMessageHandler {
    
    boolean canHandle(EventMessage message);
    void onMessage(EventMessage message);
}
