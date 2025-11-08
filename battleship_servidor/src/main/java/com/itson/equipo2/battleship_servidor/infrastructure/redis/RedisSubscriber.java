/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.redis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessageSubscriber;
import mx.itson.equipo_2.common.message.EventMessage;
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
    private final ExecutorService executor; // <-- COSA NUEVA: Pool de hilos
    private final Gson gson = new Gson();

    // volatile para asegurar visibilidad entre hilos
    private volatile JedisPubSub jedisPubSub;

    // Para controlar el estado de forma segura entre hilos
    private final AtomicBoolean isSubscribed = new AtomicBoolean(false);

    /**
     * Constructor que utiliza Inyección de Dependencias.
     *
     * @param jedisPool El pool de conexiones Jedis.
     * @param executor El servicio ejecutor para correr la tarea de suscripción.
     */
    public RedisSubscriber(JedisPool jedisPool, ExecutorService executor) {
        this.jedisPool = jedisPool;
        this.executor = executor;
    }

    @Override
    public void subscribe(String channel, IMessageHandler handler) {
        // 1. Control de estado: Evita suscribirse dos veces con la misma instancia.
        if (!isSubscribed.compareAndSet(false, true)) {
            System.err.println("Esta instancia ya está suscrita o en proceso. Cancele la suscripción primero.");
            return;
        }

        // 2. Se crea el manejador de mensajes PubSub
        this.jedisPubSub = new JedisPubSub() {
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

        // 3. Se envía la tarea de suscripción (que es bloqueante) al pool de hilos.
        executor.submit(() -> {
            // 4. <-- COSA NUEVA: try-with-resources.
            // Pide una conexión al pool y se asegura de cerrarla (devolverla al pool)
            // automáticamente cuando el bloque termina.
            try (Jedis jedis = jedisPool.getResource()) {

                System.out.println("Suscribiendo al canal: " + channel);
                // 5. Esta llamada es BLOQUEANTE. El hilo se quedará aquí
                // hasta que se llame a jedisPubSub.unsubscribe().
                jedis.subscribe(jedisPubSub, channel);

            } catch (JedisConnectionException e) {
                // Captura errores si el pool no puede dar una conexión o si se pierde
                System.err.println("Error en la conexión con Redis: " + e.getMessage());
            } catch (Exception e) {
                // Captura otros errores inesperados
                System.err.println("Error en la tarea de suscripción: " + e.getMessage());
            } finally {
                // 6. Pase lo que pase, reseteamos el estado para poder reutilizar
                // o saber que ya no está activo.
                isSubscribed.set(false);
                System.out.println("Suscripción al canal " + channel + " finalizada.");
            }
        });
    }

    @Override
    public void unsubscribe(String channel) {
        // 7. Se comprueba si el PubSub existe y está suscrito.
        if (jedisPubSub != null && isSubscribed.get()) {
            try {
                // 8. Esta llamada es SEGURA.
                // Le dice al hilo que está en jedis.subscribe() que se detenga.
                jedisPubSub.unsubscribe();
            } catch (Exception e) {
                System.err.println("Error al intentar cancelar la suscripción: " + e.getMessage());
            }
        }

        // 9. ¡YA NO CERRAMOS LA CONEXIÓN AQUÍ!
        // El bloque try-with-resources en el hilo de 'subscribe'
        // se encargará de eso automáticamente cuando 'jedis.subscribe()' termine.
        // Esto elimina la "race condition".
    }
}
