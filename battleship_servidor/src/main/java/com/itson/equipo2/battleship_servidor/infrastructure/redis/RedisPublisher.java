/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.google.gson.Gson;
import java.lang.System.Logger.Level;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.message.EventMessage;
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

            jedis.publish(channel, jsonMessage);

            System.out.println("Mensaje publicado en '" + channel + "': " + jsonMessage);

        } catch (JedisConnectionException e) {
            System.err.println("Error publicando en Redis (Canal: " + channel + "): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al publicar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
