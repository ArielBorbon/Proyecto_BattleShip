/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.itson.equipo2.subscriber;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author skyro
 */
public class Subscriber {
    
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            System.out.println("🔵 Subscriber conectado a Redis");
            System.out.println("Esperando mensajes del canal 'battleship'...");

            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    System.out.println("📩 Mensaje recibido en canal [" + channel + "]: " + message);
                }
            }, "battleship");
        }
    }
}
