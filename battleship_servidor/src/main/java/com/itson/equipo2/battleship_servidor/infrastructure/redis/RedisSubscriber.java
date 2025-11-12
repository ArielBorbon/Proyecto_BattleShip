/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessageSubscriber;
import mx.itson.equipo_2.common.message.EventMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author skyro
 */
public class RedisSubscriber implements IMessageSubscriber {

    private final JedisPool jedisPool;
    private final ExecutorService executor;
    private final Gson gson = new Gson();
    private final EventDispatcher eventDispatcher; // Referencia al Bus

    private volatile JedisPubSub jedisPubSub;
    private final AtomicBoolean isSubscribed = new AtomicBoolean(false);

    public RedisSubscriber(JedisPool jedisPool, ExecutorService executor, EventDispatcher eventDispatcher) {
        this.jedisPool = jedisPool;
        this.executor = executor;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * En esta versión "tonta", ignoramos el argumento 'handler' porque
     * delegaremos TODO al InternalEventBus. Simplemente nos suscribimos al
     * canal de Redis.
     */
    @Override
    public void subscribe(String channel) { // Nota: Ya no pide IMessageHandler

        if (!isSubscribed.compareAndSet(false, true)) {
            return;
        }

        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String messageJson) {
                try {
                    // 1. Solo deserializamos el "Sobre"
                    EventMessage event = gson.fromJson(messageJson, EventMessage.class);

                    // 2. ¡AQUÍ ESTÁ LA CLAVE! 
                    // El suscriptor no piensa. Solo le grita al Bus: "¡Llegó esto!"
                    eventDispatcher.dispatch(event);

                } catch (JsonSyntaxException e) {
                    System.err.println("Error: Mensaje no es un EventMessage válido.");
                }
            }
        };

        // Tarea bloqueante en hilo separado
        executor.submit(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(jedisPubSub, channel);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isSubscribed.set(false);
            }
        });
    }

    @Override
    public void unsubscribe() {
        if (jedisPubSub != null && isSubscribed.get()) {
            jedisPubSub.unsubscribe();
        }
    }
}
