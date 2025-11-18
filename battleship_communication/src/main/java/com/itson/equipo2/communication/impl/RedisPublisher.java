/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.communication.impl;

import com.google.gson.Gson;
import java.lang.System.Logger.Level;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author skyro
 */
public class RedisPublisher implements IMessagePublisher {

    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    /**
     * Construye el publicador inyectando el pool de conexiones.
     *
     * @param jedisPool El pool de conexiones de Redis.
     */
    public RedisPublisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void publish(String channel, EventMessage message) {
        String jsonMessage;

        try {
            jsonMessage = gson.toJson(message);
        } catch (Exception e) {
            System.err.println("Error al serializar mensaje: " + e.getMessage());
            return; 
        }

        try (Jedis jedis = jedisPool.getResource()) {

            // 3. Publicar el mensaje
            jedis.publish(channel, jsonMessage);

//            System.out.println("Mensaje publicado en '" + channel + "': " + jsonMessage);

        } catch (JedisConnectionException e) {
            System.err.println("Error publicando en Redis (Canal: " + channel + "): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al publicar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
