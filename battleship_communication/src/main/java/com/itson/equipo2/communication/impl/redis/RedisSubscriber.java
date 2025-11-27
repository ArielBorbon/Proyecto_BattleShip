package com.itson.equipo2.communication.impl.redis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.dto.EventMessage;
import com.itson.equipo2.communication.impl.EventDispatcher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * Implementación de {@code IMessageSubscriber} que utiliza Redis Pub/Sub.
 * <p>
 * La suscripción se realiza en un hilo de ejecución dedicado y utiliza un
 * {@code EventDispatcher} para distribuir los mensajes recibidos a los
 * {@code IMessageHandler}.
 * </p>
 */
public class RedisSubscriber implements IMessageSubscriber {

    /**
     * Pool de conexiones para obtener la conexión dedicada de suscripción.
     */
    private final JedisPool jedisPool;

    /**
     * Servicio de hilos para ejecutar la suscripción de forma asíncrona.
     */
    private final ExecutorService executor;

    /**
     * Serializador JSON.
     */
    private final Gson gson = new Gson();

    /**
     * Referencia al Bus de Eventos para distribuir los mensajes.
     */
    private final EventDispatcher eventDispatcher;

    /**
     * Objeto {@code JedisPubSub} dedicado a la escucha de mensajes.
     */
    private volatile JedisPubSub jedisPubSub;

    /**
     * Bandera atómica para asegurar que solo haya una suscripción activa a la
     * vez.
     */
    private final AtomicBoolean isSubscribed = new AtomicBoolean(false);

    /**
     * Construye el suscriptor.
     *
     * @param jedisPool El pool de conexiones de Redis.
     * @param executor El pool de hilos para el suscriptor.
     * @param eventDispatcher El despachador de eventos (Bus).
     */
    public RedisSubscriber(JedisPool jedisPool, ExecutorService executor, EventDispatcher eventDispatcher) {
        this.jedisPool = jedisPool;
        this.executor = executor;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Inicia la suscripción al canal en un hilo separado.
     *
     * * @param channel El nombre del canal (tema) al que suscribirse.
     */
    @Override
    public void subscribe(String channel) {

        // 1. Verifica si ya está suscrito; si no, establece la bandera en true.
        if (!isSubscribed.compareAndSet(false, true)) {
            return; // Ya estaba suscrito, no hace nada
        }

        // 2. Define el comportamiento al recibir un mensaje
        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String messageJson) {
                try {
                    // Deserializa el JSON a EventMessage
                    EventMessage event = gson.fromJson(messageJson, EventMessage.class);

                    // Delega el mensaje al Bus de Eventos
                    eventDispatcher.dispatch(event);

                } catch (JsonSyntaxException e) {
                    System.err.println("Error: Mensaje no es un EventMessage válido.");
                }
            }
        };

        // 3. Ejecuta la suscripción en un hilo dedicado (tarea de larga duración)
        executor.submit(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                // El método subscribe() es bloqueante
                jedis.subscribe(jedisPubSub, channel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Restablece la bandera si la suscripción termina por cualquier razón
                isSubscribed.set(false);
            }
        });
    }

    /**
     * Cierra la conexión de suscripción de forma segura.
     */
    @Override
    public void unsubscribe() {
        if (jedisPubSub != null && isSubscribed.get()) {
            jedisPubSub.unsubscribe();
        }
    }
}
