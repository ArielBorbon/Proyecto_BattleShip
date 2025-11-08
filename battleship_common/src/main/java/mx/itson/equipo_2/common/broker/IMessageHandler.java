/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.common.broker;

import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public interface IMessageHandler {
    
    boolean canHandle(EventMessage message);
    void onMessage(EventMessage message);
}
