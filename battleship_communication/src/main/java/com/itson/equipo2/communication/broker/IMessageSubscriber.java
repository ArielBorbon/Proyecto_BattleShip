package com.itson.equipo2.communication.broker;

/**
 * Interfaz que define la capacidad de suscribirse y desuscribirse a canales.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
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
