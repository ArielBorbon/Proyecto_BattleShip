/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.persistence.PartidaRepository;
import com.itson.equipo2.battleship_servidor.application.service.CrearPartidaVsIAService;
import com.itson.equipo2.battleship_servidor.application.service.PartidaTimerService;
import com.itson.equipo2.battleship_servidor.application.service.RealizarDisparoService;
import com.itson.equipo2.battleship_servidor.application.handler.CrearPartidaVsIAHandler;
import com.itson.equipo2.battleship_servidor.application.handler.RealizarDisparoHandler;
import com.itson.equipo2.battleship_servidor.application.handler.RegistrarJugadorHandler;
import com.itson.equipo2.battleship_servidor.application.service.RegistrarJugadorService;
import com.itson.equipo2.battleship_servidor.domain.service.AIService;
import java.util.concurrent.ExecutorService;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConfig;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author skyro
 */
public class Battleship_servidor {

    public static void main(String[] args) {
        System.out.println("Iniciando Battleship Servidor...");

    
        JedisPool pool = RedisConnection.getJedisPool();
        ExecutorService executor = RedisConnection.getSubscriberExecutor();

      
        IMessagePublisher publisher = new RedisPublisher(pool);
        IPartidaRepository partidaRepository = new PartidaRepository(null); 
        Gson gson = new Gson();
        AIService aiService = new AIService(partidaRepository, publisher);

        
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        
        PartidaTimerService timerService = new PartidaTimerService();
        
       
        RealizarDisparoService disparoService = new RealizarDisparoService(
                partidaRepository, 
                publisher, 
                timerService
        );
        
        
        CrearPartidaVsIAService crearPartidaService = new CrearPartidaVsIAService(
                partidaRepository, 
                publisher, 
                timerService, 
                gson
        );
                
     
    
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(
                publisher, 
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
        
       
       
        eventDispatcher.subscribe(
                "RegistrarJugador", 
                new RegistrarJugadorHandler(registrarJugadorService)
        );
     
        
        
        eventDispatcher.subscribe("TurnoTick", aiService);

        
        IMessageSubscriber commandSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        commandSubscriber.subscribe(BrokerConfig.CHANNEL_COMANDOS);

        
     
        IMessageSubscriber eventSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        eventSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);


        System.out.println("************************************************************");
        System.out.println("Battleship Servidor listo.");
        System.out.println("Escuchando comandos en: '" + BrokerConfig.CHANNEL_COMANDOS + "'");
        System.out.println("Escuchando eventos en:  '" + BrokerConfig.CHANNEL_EVENTOS + "'");
        System.out.println("************************************************************");
    }
}
