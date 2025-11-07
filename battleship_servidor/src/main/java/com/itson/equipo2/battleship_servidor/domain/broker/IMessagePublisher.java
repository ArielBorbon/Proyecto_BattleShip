/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.broker;

import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author Cricri
 */
public interface IMessagePublisher {
    /**
     * Publica un mensaje (evento) en el broker.
     * @param message El mensaje a publicar.
     */
    void publish(EventMessage message);
}
