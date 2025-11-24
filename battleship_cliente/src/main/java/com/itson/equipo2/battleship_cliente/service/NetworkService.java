/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import java.util.concurrent.ExecutorService;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Alberto Jimenez
 */
public class NetworkService {

    // Guardamos el suscriptor actual para poder apagarlo
    private IMessageSubscriber currentSubscriber;

    // Dependencias necesarias para crear uno nuevo
    private EventDispatcher eventDispatcher;
    private ExecutorService executor;

    // Método inicial para arrancar en localhost (llamado desde Main)
    public void inicializar(EventDispatcher dispatcher, ExecutorService exec) {
        this.eventDispatcher = dispatcher;
        this.executor = exec;
        iniciarSuscripcion(); // Arranca en localhost por defecto
    }

    public void conectarAServidor(String ip) throws Exception {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP vacía");
        }

        System.out.println("NetworkService: Cambiando objetivo a " + ip + "...");

        // 1. Cambiar la configuración global
        RedisConnection.setHost(ip);

        // 2. Probar si existe (Ping)
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            if (!"PONG".equals(jedis.ping())) {
                throw new Exception("Sin PONG");
            }
            System.out.println("Ping exitoso.");
        }

        // 3. ¡LA CLAVE! REINICIAR EL SUSCRIPTOR EN LA NUEVA IP
        reiniciarSuscripcion();
    }

    private void reiniciarSuscripcion() {
        // Si hay uno viejo escuchando, lo matamos
        if (currentSubscriber != null) {
            System.out.println("Cerrando suscriptor anterior...");
            try {
                currentSubscriber.unsubscribe();
            } catch (Exception e) {
                /* Ignorar error al cerrar */ }
            currentSubscriber = null;
        }
        // Creamos uno nuevo (que tomará la nueva IP de RedisConnection)
        iniciarSuscripcion();
    }

    private void iniciarSuscripcion() {
        if (eventDispatcher != null && executor != null) {
            currentSubscriber = new RedisSubscriber(
                    RedisConnection.getJedisPool(), // <-- Toma la IP actualizada
                    executor,
                    eventDispatcher
            );
            currentSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);
            System.out.println("Suscriptor reiniciado y escuchando eventos.");
        }
    }
}
