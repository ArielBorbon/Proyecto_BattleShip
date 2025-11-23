
package com.itson.equipo2.battleship_cliente.service;

import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;

/**
 *
 * @author PC Gamer
 */
public class SalaService {

    private final IMessagePublisher publisher;

    public SalaService(IMessagePublisher publisher) {
        this.publisher = publisher;
    }

    public void enviarInicioJuego() {
        System.out.println("Cliente: Host solicitando inicio de juego...");
        
        EventMessage msg = new EventMessage("SolicitarInicioPosicionamiento", "GO");
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, msg);
    }
}