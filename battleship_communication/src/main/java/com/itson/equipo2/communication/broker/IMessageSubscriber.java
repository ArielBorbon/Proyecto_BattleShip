package com.itson.equipo2.communication.broker;

/**
 * Interfaz que define la capacidad de suscribirse y desuscribirse a canales.
 */
public interface IMessageSubscriber {
 
    /**
     * Inicia el proceso de escucha y suscripción a un canal de mensajes.
     *
     * @param channel El nombre del canal (tema) al que suscribirse.
     */
    void subscribe(String channel);

    /**
     * Finaliza la suscripción al canal.
     */
    void unsubscribe();
}