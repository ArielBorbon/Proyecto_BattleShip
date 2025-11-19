package com.itson.equipo2.communication.broker;

import com.itson.equipo2.communication.dto.EventMessage;

/**
 * Interfaz que define la capacidad de publicar mensajes a un canal específico.
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