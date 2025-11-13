/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.persistence.PartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.service.CrearPartidaVsIAService;
import com.itson.equipo2.battleship_servidor.domain.service.PartidaTimerService;
import com.itson.equipo2.battleship_servidor.domain.service.RealizarDisparoService;
import com.itson.equipo2.battleship_servidor.application.handler.CrearPartidaVsIAHandler;
import com.itson.equipo2.battleship_servidor.application.handler.RealizarDisparoHandler;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.EventDispatcher;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConnection;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisPublisher;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisSubscriber;
import com.itson.equipo2.battleship_servidor.domain.service.AIService;
import java.util.concurrent.ExecutorService;
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

        //Conexin a Redis
        JedisPool pool = RedisConnection.getJedisPool();
        ExecutorService executor = RedisConnection.getSubscriberExecutor();

        //Inicializar Componentes de Infraestructura
        IMessagePublisher publisher = new RedisPublisher(pool);
        IPartidaRepository partidaRepository = new PartidaRepository(null); // Repositorio Singleton
        Gson gson = new Gson();
        AIService aiService = new AIService(partidaRepository, publisher);

        // crear el router """" de los eventos
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        
        PartidaTimerService timerService = new PartidaTimerService();
        
        // 2. creamos el servicio de los balazos
        RealizarDisparoService disparoService = new RealizarDisparoService(
                partidaRepository, 
                publisher, 
                timerService
        );
        
        //creamos el evento de la partida como tal
        CrearPartidaVsIAService crearPartidaService = new CrearPartidaVsIAService(
                partidaRepository, 
                publisher, 
                timerService, 
                gson
        );
                
        eventDispatcher.subscribe(
                "RealizarDisparo", 
                new RealizarDisparoHandler(disparoService)
        );

        eventDispatcher.subscribe(
                "CrearPartidaVsIA", 
                new CrearPartidaVsIAHandler(crearPartidaService)
        );
        
        // Registrar el AIService para que escuche los eventos
        eventDispatcher.subscribe("TurnoTick", aiService);

        //Iniciar el Suscriptor de Comandos del Cliente
        IMessageSubscriber commandSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        commandSubscriber.subscribe(RedisConfig.CHANNEL_COMANDOS);
        
        //Iniciar el Suscriptor de Eventos (IA)
        IMessageSubscriber eventSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        eventSubscriber.subscribe(RedisConfig.CHANNEL_EVENTOS);

        System.out.println("************************************************************");
        System.out.println("Battleship Servidor listo.");
        System.out.println("Escuchando comandos en: '" + RedisConfig.CHANNEL_COMANDOS + "'");
        System.out.println("Escuchando eventos en:  '" + RedisConfig.CHANNEL_EVENTOS + "'");
        System.out.println("************************************************************");
    }
}
