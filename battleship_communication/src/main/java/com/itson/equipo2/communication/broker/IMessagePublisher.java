package com.itson.equipo2.communication.broker;

import com.itson.equipo2.communication.dto.EventMessage;

/**
 * Interfaz que define la capacidad de publicar mensajes a un canal específico.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public interface IMessagePublisher {

    /**
     * Publica un mensaje de evento en el canal de comunicación especificado.
     *
     * @param channel El nombre del canal (tema) donde se publicará el mensaje.
     * @param message El objeto {@code EventMessage} a publicar.
     */
    void publish(String channel, EventMessage message);

}
