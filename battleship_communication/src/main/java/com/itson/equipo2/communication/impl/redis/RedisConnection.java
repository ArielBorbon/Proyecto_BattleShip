package com.itson.equipo2.communication.impl.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Clase utilitaria para gestionar la conexión y los recursos del pool de Redis.
 * Implementa el patrón Singleton para {@code JedisPool} y {@code ExecutorService}.
 */
public class RedisConnection {

    private static JedisPool jedisPool;
    private static ExecutorService subscriberExecutor;
    
    private static String currentHost = "localhost"; 
    private static final int REDIS_PORT = 6379;

    /** Constructor privado para evitar la instanciación externa. */
    private RedisConnection() {
    }

    /**
     * Configuración del pool de conexiones para optimizar el rendimiento y la concurrencia.
     *
     * @return El objeto {@code JedisPoolConfig} configurado.
     */
    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Configuración para el manejo eficiente de hilos
        poolConfig.setMaxTotal(128); // Máximo de conexiones totales
        poolConfig.setMaxIdle(128);  // Máximo de conexiones inactivas
        poolConfig.setMinIdle(16);   // Mínimo de conexiones inactivas
        poolConfig.setTestOnBorrow(true); // Verificar la conexión al tomar del pool
        poolConfig.setBlockWhenExhausted(true); // Bloquear si el pool está lleno
        return poolConfig;
    }

    /**
     * Obtiene la instancia única del Pool de Jedis (Lazy-loaded y thread-safe).
     *
     * @return El {@code JedisPool} configurado.
     */
    public static synchronized JedisPool getJedisPool() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = buildPoolConfig();

            // Crea el pool utilizando la configuración y las constantes de conexión
            jedisPool = new JedisPool(
                    poolConfig,
                    currentHost, // <-- Aquí entra la IP que escribas
                    REDIS_PORT
            );
        }
        return jedisPool;
    }

    /**
     * Obtiene la instancia única del Pool de Hilos para suscriptores.
     * <p>
     * Se usa un {@code CachedThreadPool} ya que las operaciones de suscripción
     * suelen ser de larga duración.
     * </p>
     *
     * @return El {@code ExecutorService} para los hilos de suscripción.
     */
    public static synchronized ExecutorService getSubscriberExecutor() {
        if (subscriberExecutor == null) {
            subscriberExecutor = Executors.newCachedThreadPool();
        }
        return subscriberExecutor;
    }
    
    
    
    public static synchronized void setHost(String newHost) {
        System.out.println("Cambiando Host de Redis a: " + newHost);
        currentHost = newHost;
        
        // Si ya había una conexión, la matamos para que se cree la nueva
        if (jedisPool != null && !jedisPool.isClosed()) {
            try {
                jedisPool.close();
            } catch (Exception e) { /* Ignorar error de cierre */ }
            jedisPool = null; 
        }
    }

    /**
     * Cierra y libera todos los recursos de los pools.
     * Debe llamarse al finalizar la aplicación para evitar fugas de memoria y recursos.
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