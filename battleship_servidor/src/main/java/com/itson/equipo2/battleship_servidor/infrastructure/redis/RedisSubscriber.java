/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.itson.equipo2.battleship_servidor.application.PartidaApplicationService;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessageSubscriber;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


/**
 *
 * @author Cricri
 */
public class RedisSubscriber implements IMessageSubscriber{
    
    private Jedis jedis;
    private JedisPubSub jedisPubSub;

    
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
                        
                     
                        handler.onMessage(channel, message);
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
