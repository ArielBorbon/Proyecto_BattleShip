/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class EventDispatcher {
    
    private static EventDispatcher instance;

    private final Map<String, List<IMessageHandler>> suscriptores = new ConcurrentHashMap<>();
    
    private EventDispatcher() {
    }
    
    
    public static synchronized  EventDispatcher getInstance() {
        if (instance == null) {
            instance = new EventDispatcher();
        }
        return instance;
    }
    
    public void subscribe(String eventType, IMessageHandler handler) {
        suscriptores.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        System.out.println("Nuevo handler registrado para el evento: " + eventType);
    }

    /**
     * El RedisSubscriber usa esto para entregar el mensaje.
     * El Bus busca a los interesados y les pasa el mensaje.
     */
    public void dispatch(EventMessage event) {
        String tipo = event.getEventType();
        List<IMessageHandler> handlers = suscriptores.get(tipo);

        if (handlers != null && !handlers.isEmpty()) {
            for (IMessageHandler handler : handlers) {
                try {
                    handler.onMessage(event);
                } catch (Exception e) {
                    System.err.println("Error en handler para " + tipo + ": " + e.getMessage());
                }
            }
        } else {
            System.out.println("Alerta: Se recibió '" + tipo + "' pero nadie lo está escuchando.");
        }
    }
}
