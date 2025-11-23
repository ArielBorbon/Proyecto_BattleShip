package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;
import mx.itson.equipo_2.common.enums.AccionPartida;
import mx.itson.equipo_2.common.enums.ColorJugador;

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

    public void registrar(String nombre, ColorJugador color, AccionPartida accion, String jugadorId) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return;
        }
        RegistrarJugadorRequest req = new RegistrarJugadorRequest(nombre, accion, color, jugadorId);
        String payload = gson.toJson(req);
        EventMessage message = new EventMessage("RegistrarJugador", payload);

        System.out.println("Cliente: Enviando comando 'RegistrarJugador' [" + accion + "] con color: " + color + " e ID: " + jugadorId);
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
    }
    
    
    public void configurarRed(String ipHost) {
        if (ipHost != null && !ipHost.trim().isEmpty()) {
            try {
                com.itson.equipo2.communication.impl.RedisConnection.setHost(ipHost);
                
                com.itson.equipo2.communication.impl.RedisConnection.getJedisPool(); 
                System.out.println("Servicio: Red configurada a " + ipHost);
            } catch (Exception e) {
                System.err.println("Error configurando red: " + e.getMessage());
            }
        }
    }
    
 
}

