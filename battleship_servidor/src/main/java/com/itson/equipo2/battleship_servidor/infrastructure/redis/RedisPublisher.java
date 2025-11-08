/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.google.gson.Gson;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.message.EventMessage;
import redis.clients.jedis.Jedis;


/**
 *
 * @author Cricri
 */
public class RedisPublisher implements IMessagePublisher {

    private final Gson gson;
    private final Jedis jedis;

    public RedisPublisher(Gson gson) {
        this.gson = gson;
       
        this.jedis = new Jedis(RedisConfig.REDIS_HOST, RedisConfig.REDIS_PORT);
    }

    @Override
    public void publish(String channel, EventMessage message) {
        try {
          
            String jsonMessage = gson.toJson(message);
            
            System.out.println("Publicando evento en canal '" + channel + "': " + jsonMessage);
            
        
            jedis.publish(channel, jsonMessage);
            
        } catch (Exception e) {
            System.err.println("Error al publicar en Redis: " + e.getMessage());
        }
    }
    
  
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
