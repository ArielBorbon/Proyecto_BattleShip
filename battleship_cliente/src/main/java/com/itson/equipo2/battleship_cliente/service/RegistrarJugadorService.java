
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;
import mx.itson.equipo_2.common.enums.AccionPartida;

/**
 *
 * @author Cricri
 */
public class RegistrarJugadorService {

    private final IMessagePublisher publisher;
    private final Gson gson = new Gson();

    public RegistrarJugadorService(IMessagePublisher publisher) {
        this.publisher = publisher;
    }

    public void registrar(String nombre, AccionPartida accion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return;
        }

        RegistrarJugadorRequest req = new RegistrarJugadorRequest(nombre, accion);
        
        String payload = gson.toJson(req);
        EventMessage message = new EventMessage("RegistrarJugador", payload);

        System.out.println("Cliente: Enviando comando 'RegistrarJugador' [" + accion + "] con nombre: " + nombre);
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
    }
}