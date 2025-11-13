/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.communication.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.dto.EventMessage;
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


    @Override
    public void subscribe(String channel) { 

        if (!isSubscribed.compareAndSet(false, true)) {
            return;
        }

        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String messageJson) {
                try {
                    EventMessage event = gson.fromJson(messageJson, EventMessage.class);

                    eventDispatcher.dispatch(event);        //un grito al bus para que haga sus cambios

                } catch (JsonSyntaxException e) {
                    System.err.println("Error: Mensaje no es un EventMessage válido.");
                }
            }
        };

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
