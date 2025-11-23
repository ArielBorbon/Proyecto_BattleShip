/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.itson.equipo2.communication.impl.RedisConnection;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Alberto Jimenez
 */
public class NetworkService {
    
    public void conectarAServidor(String ip) throws Exception {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("La IP no puede estar vacía.");
        }

        // 1. Configurar IP
        RedisConnection.setHost(ip);

        // 2. Probar conexión (Ping)
        try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
            String respuesta = jedis.ping();
            if (!"PONG".equals(respuesta)) {
                throw new Exception("Sin respuesta del servidor.");
            }
            System.out.println("Conexión exitosa a " + ip);
        }
    }
    
}
