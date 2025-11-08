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

        // 1. Serializar el mensaje
        try {
            jsonMessage = gson.toJson(message);
        } catch (Exception e) {
            System.err.println("Error al serializar mensaje: " + e.getMessage());
            return; // No se puede publicar si no se puede serializar
        }

        // 2. Usar 'try-with-resources' para tomar y devolver una conexión
        //    del pool automáticamente.
        try (Jedis jedis = jedisPool.getResource()) {

            // 3. Publicar el mensaje
            jedis.publish(channel, jsonMessage);

            System.out.println("Mensaje publicado en '" + channel + "': " + jsonMessage);

        } catch (JedisConnectionException e) {
            // Error común: no se pudo conectar o se perdió la conexión
            System.err.println("Error publicando en Redis (Canal: " + channel + "): " + e.getMessage());
            // Aquí podrías implementar una lógica de reintento si quisieras
        } catch (Exception e) {
            // Otro error inesperado
            System.err.println("Error inesperado al publicar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
