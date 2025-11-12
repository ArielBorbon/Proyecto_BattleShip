/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.PartidaApplicationService;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.repository.PartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.service.RealizarDisparoService;
import com.itson.equipo2.battleship_servidor.handler.RealizarDisparoHandler;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.EventDispatcher;
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

        // 2. Inicializar Componentes de Infraestructura
        IMessagePublisher publisher = new RedisPublisher(pool);
        IPartidaRepository partidaRepository = new PartidaRepository(null); // Repositorio Singleton
        Gson gson = new Gson();
        AIService aiService = new AIService(partidaRepository, publisher);

        // 3. Crear el Dispatcher (El "Router" de eventos)
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        
        // 4. Crear el Servicio de Aplicación (que registra los handlers)
        // Esta llamada "conecta" todo
        new PartidaApplicationService(
                partidaRepository,
                publisher,
                gson,
                aiService,
                eventDispatcher
        );

        // 5. Iniciar el Suscriptor de Comandos del Cliente
        IMessageSubscriber commandSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        commandSubscriber.subscribe(RedisConfig.CHANNEL_COMANDOS);// El handler se ignora, usa el dispatcher
        
        // 6. Iniciar el Suscriptor de Eventos (para la IA)
        IMessageSubscriber eventSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        eventSubscriber.subscribe(RedisConfig.CHANNEL_EVENTOS);// El handler se ignora, usa el dispatcher

        System.out.println("************************************************************");
        System.out.println("Battleship Servidor listo.");
        System.out.println("Escuchando comandos en: '" + RedisConfig.CHANNEL_COMANDOS + "'");
        System.out.println("Escuchando eventos en:  '" + RedisConfig.CHANNEL_EVENTOS + "'");
        System.out.println("************************************************************");
    }
}
