/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.handler.AbandonarPartidaHandler;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.persistence.PartidaRepository;
import com.itson.equipo2.battleship_servidor.application.service.CrearPartidaVsIAService;
import com.itson.equipo2.battleship_servidor.application.service.PartidaTimerService;
import com.itson.equipo2.battleship_servidor.application.service.RealizarDisparoService;
import com.itson.equipo2.battleship_servidor.application.handler.CrearPartidaVsIAHandler;
import com.itson.equipo2.battleship_servidor.application.handler.PosicionarNaveHandler;
import com.itson.equipo2.battleship_servidor.application.handler.RealizarDisparoHandler;
import com.itson.equipo2.battleship_servidor.application.handler.RegistrarJugadorHandler;
import com.itson.equipo2.battleship_servidor.application.service.AbandonarPartidaService;
import com.itson.equipo2.battleship_servidor.application.service.PosicionarNaveService;
import com.itson.equipo2.battleship_servidor.application.service.RegistrarJugadorService;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.service.AIService;
import com.itson.equipo2.communication.broker.IMessageHandler;
import java.util.concurrent.ExecutorService;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.dto.EventMessage;
import com.itson.equipo2.communication.impl.EventDispatcher;
import com.itson.equipo2.communication.impl.RedisConnection;
import com.itson.equipo2.communication.impl.RedisPublisher;
import com.itson.equipo2.communication.impl.RedisSubscriber;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.enums.ColorJugador;
import mx.itson.equipo_2.common.enums.EstadoJugador;
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

        Jugador jugador = new Jugador("JUGADOR_1", "Jonh Doe", ColorJugador.AZUL);
        Jugador jugador2 = new Jugador("JUGADOR_IA_01", "IA", ColorJugador.ROJO);
        jugador2.setEstado(EstadoJugador.POSICIONANDO);
      
        IMessagePublisher publisher = new RedisPublisher(pool);
        IPartidaRepository partidaRepository = new PartidaRepository(new Partida(jugador)); 
        partidaRepository.getPartida().unirseAPartida(jugador2, "JUGADOR_1");
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
                
        AbandonarPartidaService abandonarService = new AbandonarPartidaService(
                partidaRepository, 
                publisher, 
                timerService
        );
    
        RegistrarJugadorService registrarJugadorService = new RegistrarJugadorService(
                publisher, 
                gson
        );
        
        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(
                partidaRepository, 
                publisher
        );
        
        posicionarNaveService.setCrearPartidaVsIAService(crearPartidaService);

    
        eventDispatcher.subscribe(
                "RealizarDisparo", 
                new RealizarDisparoHandler(disparoService)
        );

        eventDispatcher.subscribe(
                "CrearPartidaVsIA", 
                new CrearPartidaVsIAHandler(crearPartidaService)
        );
        
       eventDispatcher.subscribe(
                "AbandonarPartida", 
                new AbandonarPartidaHandler(abandonarService)
        );
       
        eventDispatcher.subscribe(
                "RegistrarJugador", 
                new RegistrarJugadorHandler(registrarJugadorService)
        );
        
        eventDispatcher.subscribe(
                "PosicionarFlota", new PosicionarNaveHandler(posicionarNaveService));
     
        
        
        eventDispatcher.subscribe("TurnoTick", aiService);
        
        eventDispatcher.subscribe("DisparoRealizado", new IMessageHandler() {
            @Override
            public boolean canHandle(EventMessage message) { return true; }
            @Override
            public void onMessage(EventMessage message) { 
                // No hacer nada (Silencio) 
            }
        });
        
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
