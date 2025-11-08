/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author skyro
 */
public class RedisConnection {

    private static JedisPool jedisPool;
    private static ExecutorService subscriberExecutor;

    // Constructor privado para evitar que alguien haga 'new RedisConnection()'
    private RedisConnection() {
    }

    /**
     * Configuración del pool de conexiones.
     */
    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        // ... (resto de tu configuración de pool) ...
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    /**
     * Obtiene la instancia única del Pool de Jedis (Lazy-loaded y thread-safe).
     *
     * @return El JedisPool.
     */
    public static synchronized JedisPool getJedisPool() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = buildPoolConfig();

            // ¡AQUÍ ESTÁ EL CAMBIO! Lee desde RedisConfig
            jedisPool = new JedisPool(
                    poolConfig,
                    RedisConfig.REDIS_HOST,
                    RedisConfig.REDIS_PORT
            // , RedisConfig.REDIS_PASSWORD (si tuvieras)
            );
        }
        return jedisPool;
    }

    /**
     * Obtiene la instancia única del Pool de Hilos para suscriptores.
     *
     * @return El ExecutorService.
     */
    public static synchronized ExecutorService getSubscriberExecutor() {
        if (subscriberExecutor == null) {
            subscriberExecutor = Executors.newCachedThreadPool();
        }
        return subscriberExecutor;
    }

    /**
     * Cierra todos los pools. Debe llamarse al apagar el servidor.
     */
    public static void shutdown() {
        System.out.println("Cerrando pools de Redis y Hilos...");
        if (subscriberExecutor != null) {
            subscriberExecutor.shutdown();
        }
        if (jedisPool != null) {
            jedisPool.close();
        }
        System.out.println("Pools cerrados.");
    }
}
