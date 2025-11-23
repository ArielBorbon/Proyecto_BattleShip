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
import redis.clients.jedis.JedisPool;

/**
 *
 * @author skyro
 */
public class Battleship_servidor {

    public static void main(String[] args) {
        System.out.println("Iniciando Battleship Servidor...");

        // 1. Infraestructura
        JedisPool pool = RedisConnection.getJedisPool();
        ExecutorService executor = RedisConnection.getSubscriberExecutor();
        IMessagePublisher publisher = new RedisPublisher(pool);
        Gson gson = new Gson();

        // 2. Repositorio
        IPartidaRepository partidaRepository = new PartidaRepository(null);

        // 3. Servicios de Aplicación
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
                gson,
                partidaRepository
        );

        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(
                partidaRepository,
                publisher
        );


//        eventDispatcher.subscribe(
 //               "CrearPartidaVsIA", 
  //              new CrearPartidaVsIAHandler(crearPartidaService)
  //      );
        
       eventDispatcher.subscribe(
                "AbandonarPartida", 
                new AbandonarPartidaHandler(abandonarService)
        );
       

        
        eventDispatcher.subscribe(
                "PosicionarFlota", new PosicionarNaveHandler(posicionarNaveService));
     
        
        
//        eventDispatcher.subscribe("TurnoTick", aiService);
        
        eventDispatcher.subscribe("DisparoRealizado", new IMessageHandler() {
            @Override
            public boolean canHandle(EventMessage message) { return true; }
            @Override
            public void onMessage(EventMessage message) { 
                // No hacer nada (Silencio) 
            }
        });
        

    //    posicionarNaveService.setCrearPartidaVsIAService(crearPartidaService);

        eventDispatcher.subscribe("RealizarDisparo", new RealizarDisparoHandler(disparoService));
       // eventDispatcher.subscribe("CrearPartidaVsIA", new CrearPartidaVsIAHandler(crearPartidaService));
        eventDispatcher.subscribe("RegistrarJugador", new RegistrarJugadorHandler(registrarJugadorService));
        eventDispatcher.subscribe("PosicionarFlota", new PosicionarNaveHandler(posicionarNaveService));

        eventDispatcher.subscribe("SolicitarInicioPosicionamiento", new IMessageHandler() {
            @Override
            public boolean canHandle(EventMessage message) {
                return "SolicitarInicioPosicionamiento".equals(message.getEventType());
            }

            @Override
            public void onMessage(EventMessage message) {
                System.out.println("Servidor: El Host inició la partida. Notificando a todos...");
                // Rebotamos la señal como un evento público
                EventMessage eventoInicio = new EventMessage("InicioPosicionamiento", "GO");
                publisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoInicio);
            }
        });

        // 5. Suscripción a Redis (Entrada de comandos)
        IMessageSubscriber commandSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        commandSubscriber.subscribe(BrokerConfig.CHANNEL_COMANDOS);

        // (Opcional) El servidor también puede escuchar eventos si es necesario para log o depuración
        IMessageSubscriber eventSubscriber = new RedisSubscriber(pool, executor, eventDispatcher);
        eventSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);

        System.out.println("************************************************************");
        System.out.println("Battleship Servidor LISTO para recibir jugadores.");
        System.out.println("Esperando comandos en: '" + BrokerConfig.CHANNEL_COMANDOS + "'");
        System.out.println("************************************************************");
    }
}
