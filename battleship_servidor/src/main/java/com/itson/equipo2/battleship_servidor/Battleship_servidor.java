/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.PartidaApplicationService;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.repository.PartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConnection;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisPublisher;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisSubscriber;
import com.itson.equipo2.battleship_servidor.infrastructure.service.AIService;
import java.util.concurrent.ExecutorService;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.broker.IMessageSubscriber;
import redis.clients.jedis.JedisPool;


/**
 *
 * @author skyro
 */
public class Battleship_servidor {

    public static void main(String[] args) {
        System.out.println("Iniciando Battleship Servidor...");

        // 1. Inicializar Conexión a Redis
        JedisPool pool = RedisConnection.getJedisPool();
        ExecutorService executor = RedisConnection.getSubscriberExecutor();
        
        // 2. Inicializar Componentes del Servidor
        IMessagePublisher publisher = new RedisPublisher(pool);
        
        // Creamos el repositorio de partida (Singleton)
        // Se inicializa con null, la partida se creará con el primer comando.
        IPartidaRepository partidaRepository = new PartidaRepository(null); 
        
        Gson gson = new Gson();
        AIService aiService = new AIService();

        // 3. Crear el Servicio de Aplicación (que actúa como nuestro Handler principal)
        IMessageHandler commandHandler = new PartidaApplicationService(
                partidaRepository, 
                publisher, 
                gson, 
                aiService
        );

        // 4. Crear el Suscriptor
        IMessageSubscriber subscriber = new RedisSubscriber(pool, executor);

        // 5. Suscribir el servicio al canal de COMANDOS
        subscriber.subscribe(RedisConfig.CHANNEL_COMANDOS, commandHandler);
        
        System.out.println("************************************************************");
        System.out.println("Battleship Servidor listo.");
        System.out.println("Escuchando comandos en el canal: '" + RedisConfig.CHANNEL_COMANDOS + "'");
        System.out.println("************************************************************");
    }
}
