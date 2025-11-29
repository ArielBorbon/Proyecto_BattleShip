package com.itson.equipo2.communication.impl.redis;

import com.google.gson.Gson;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Implementación de {@code IMessagePublisher} que utiliza Redis Pub/Sub para la
 * publicación.
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116
 * @author Sebastián Bórquez Huerta 00000252115
 * @author Alberto Jiménez García 00000252595
 * @author José Eduardo Aguilar García 00000252049
 * @author José Luis Islas Molina 00000252574
 */
public class RedisPublisher implements IMessagePublisher {

    /**
     * Pool de conexiones para obtener recursos de conexión a Redis.
     */
//    private final JedisPool jedisPool;
    /**
     * Serializador JSON para convertir el mensaje a String.
     */
    private final Gson gson = new Gson();

    /**
     * Construye el publicador inyectando el pool de conexiones.
     *
     * @param jedisPool El pool de conexiones de Redis.
     */
    public RedisPublisher(JedisPool jedisPool) {
    }

    /**
     * Serializa el mensaje y lo publica en el canal de Redis.
     *
     * @param channel El nombre del canal (tema).
     * @param message El objeto {@code EventMessage} a publicar.
     */
    @Override
    public void publish(String channel, EventMessage message) {
        String jsonMessage;
        try {
            jsonMessage = gson.toJson(message);
        } catch (Exception e) {
            System.err.println("Error al serializar mensaje: " + e.getMessage());
            return;
        }

        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            jedis.publish(channel, jsonMessage);
        } catch (JedisConnectionException e) {
            System.err.println("Error publicando en Redis (Canal: " + channel + "): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al publicar mensaje: " + e.getMessage());
        }
    }

}
