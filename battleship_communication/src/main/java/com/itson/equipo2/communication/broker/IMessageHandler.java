package com.itson.equipo2.communication.broker;

import com.itson.equipo2.communication.dto.EventMessage;

/**
 * Interfaz para los manejadores de mensajes (Handlers).
 * <p>
 * Representa la lógica de aplicación que reacciona a un mensaje recibido.
 * </p>
 */
public interface IMessageHandler {

    /**
     * Determina si el Handler es capaz de procesar el mensaje dado (basado en
     * el {@code eventType}).
     *
     * @param message El mensaje de evento.
     * @return {@code true} si el Handler debe procesar el mensaje.
     */
    boolean canHandle(EventMessage message);

    /**
     * Ejecuta la lógica de aplicación en respuesta al mensaje.
     *
     * @param message El mensaje de evento recibido.
     */
    void onMessage(EventMessage message);
}
