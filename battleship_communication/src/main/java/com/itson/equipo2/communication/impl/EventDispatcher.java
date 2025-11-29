package com.itson.equipo2.communication.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 * El Bus de Eventos (Event Dispatcher) del sistema.
 *
 * Utiliza el patrón **Singleton** para garantizar una única instancia
 * centralizada que gestiona el mapeo entre tipos de eventos y los Handlers
 * interesados. Actúa como intermediario para desacoplar el recibo de mensajes
 * de su procesamiento.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class EventDispatcher {

    private static EventDispatcher instance;

    /**
     * Mapa concurrente que almacena la lista de Handlers para cada tipo de
     * evento. Llave: String (eventType), Valor: List<IMessageHandler>
     */
    private final Map<String, List<IMessageHandler>> suscriptores = new ConcurrentHashMap<>();

    /**
     * Constructor privado para el patrón Singleton.
     */
    private EventDispatcher() {
    }

    /**
     * Obtiene la instancia única y sincronizada del EventDispatcher.
     *
     * @return La instancia global del Bus de Eventos.
     */
    public static synchronized EventDispatcher getInstance() {
        if (instance == null) {
            instance = new EventDispatcher();
        }
        return instance;
    }

    /**
     * Registra un Handler para un tipo de evento específico. Si no existe una
     * lista para ese tipo de evento, se crea una nueva.
     *
     * @param eventType El tipo de evento al que se suscribe.
     * @param handler El {@code IMessageHandler} que procesará el evento.
     */
    public void subscribe(String eventType, IMessageHandler handler) {
        // Usa computeIfAbsent para crear una nueva lista si el eventType no existe.
        suscriptores.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        System.out.println("Nuevo handler registrado para el evento: " + eventType);
    }

    /**
     * Despacha el mensaje de evento a todos los Handlers registrados para su
     * tipo.
     *
     * Este método es invocado típicamente por el {@code RedisSubscriber} al
     * recibir un mensaje desde la red.
     *
     * @param event El mensaje de evento a distribuir.
     */
    public void dispatch(EventMessage event) {
        String tipo = event.getEventType();
        List<IMessageHandler> handlers = suscriptores.get(tipo);

        if (handlers != null && !handlers.isEmpty()) {
            for (IMessageHandler handler : handlers) {
                try {
                    // Entregamos el mensaje al interesado
                    handler.onMessage(event);
                } catch (Exception e) {
                    System.err.println("Error en handler para " + tipo + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            // Caso donde se recibe un evento pero no hay handlers registrados.
            // System.out.println("Alerta: Se recibió '" + tipo + "' pero nadie lo está escuchando.");
        }
    }
}
