/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itson.equipo2.battleship_servidor.application.PartidaApplicationService;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessageSubscriber;
import mx.itson.equipo_2.common.message.EventMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


/**
 *
 * @author Cricri
 */
public class RedisSubscriber implements IMessageSubscriber{
    
    private Jedis jedis;
    private JedisPubSub jedisPubSub;
    private final Gson gson = new Gson(); 

    public RedisSubscriber() {
    }

    @Override
    public void subscribe(String channel, IMessageHandler handler) {
        new Thread(() -> {
            try {
                jedis = new Jedis(RedisConfig.REDIS_HOST, RedisConfig.REDIS_PORT);
                
                jedisPubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("Comando recibido en canal '" + channel + "': " + message);
                        try {
                      
                            EventMessage eventMessage = gson.fromJson(message, EventMessage.class);
                            handler.onMessage(eventMessage);
                        } catch (JsonSyntaxException e) {
                            System.err.println("Error al parsear mensaje JSON: " + e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error inesperado al manejar mensaje: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };

                System.out.println("Suscribiendo al canal: " + channel);
                jedis.subscribe(jedisPubSub, channel);
                
            } catch (Exception e) {
                System.err.println("Error en la suscripción a Redis: " + e.getMessage());
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }).start();
    }

    @Override
    public void unsubscribe(String channel) {
        if (jedisPubSub != null && jedisPubSub.isSubscribed()) {
            jedisPubSub.unsubscribe();
        }
        if (jedis != null) {
            jedis.close();
        }
    }
}
