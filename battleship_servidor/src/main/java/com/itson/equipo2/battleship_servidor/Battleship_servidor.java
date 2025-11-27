/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.equipo2.battleship_servidor;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.application.handler.*;
import com.itson.equipo2.battleship_servidor.application.service.*;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.persistence.PartidaRepository;

// --- NUEVOS IMPORTS DE COMUNICACIÓN ---
import com.itson.equipo2.communication.broker.ICommunicationProvider;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.broker.IMessageSubscriber;
import com.itson.equipo2.communication.dto.EventMessage;
import com.itson.equipo2.communication.impl.EventDispatcher;
// Única referencia concreta a Redis (La Fábrica)
import com.itson.equipo2.communication.impl.redis.RedisProvider;

import mx.itson.equipo_2.common.broker.BrokerConfig;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author skyro
 */
public class Battleship_servidor {

    public static void main(String[] args) {
        System.out.println("Iniciando Battleship Servidor (Arquitectura Desacoplada)...");

        // -----------------------------------------------------------
        // 1. INFRAESTRUCTURA DE COMUNICACIÓN
        // -----------------------------------------------------------
        // Singleton del Bus de Eventos
        EventDispatcher eventDispatcher = EventDispatcher.getInstance();

        // Pool de Hilos (Independiente de la tecnología de red)
        ExecutorService executor = Executors.newCachedThreadPool();

        // INYECCIÓN DE DEPENDENCIA:
        // Aquí decidimos que el servidor usará REDIS.
        // El Provider encapsula JedisPool, RedisConnection, etc.
        ICommunicationProvider provider = new RedisProvider(eventDispatcher, executor);

        // Obtenemos el Publicador a través del contrato
        IMessagePublisher publisher = provider.getPublisher();

        Gson gson = new Gson();

        // -----------------------------------------------------------
        // 2. REPOSITORIO
        // -----------------------------------------------------------
        IPartidaRepository partidaRepository = new PartidaRepository(null);

        // -----------------------------------------------------------
        // 3. SERVICIOS DE APLICACIÓN
        // -----------------------------------------------------------
        PartidaTimerService timerService = new PartidaTimerService();

        RealizarDisparoService disparoService = new RealizarDisparoService(
                partidaRepository,
                publisher,
                timerService
        );

        AbandonarPartidaService abandonarService = new AbandonarPartidaService(
                partidaRepository,
                publisher,
                timerService
        );

        UnirJugadorService registrarJugadorService = new UnirJugadorService(
                publisher,
                gson,
                partidaRepository
        );

        PosicionarNaveService posicionarNaveService = new PosicionarNaveService(
                partidaRepository,
                publisher
        );
        posicionarNaveService.setPartidaTimerService(timerService);

        // -----------------------------------------------------------
        // 4. CONFIGURACIÓN DE EVENTOS (HANDLERS)
        // -----------------------------------------------------------
        eventDispatcher.subscribe("AbandonarPartida", new AbandonarPartidaHandler(abandonarService));
        eventDispatcher.subscribe("PosicionarFlota", new PosicionarNaveHandler(posicionarNaveService));
        eventDispatcher.subscribe("RealizarDisparo", new RealizarDisparoHandler(disparoService));
        eventDispatcher.subscribe("RegistrarJugador", new UnirJugadorHandler(registrarJugadorService));

        // Lógica para iniciar la partida cuando el host lo indica
        eventDispatcher.subscribe("SolicitarInicioPosicionamiento", new IMessageHandler() {
            @Override
            public boolean canHandle(EventMessage message) {
                return "SolicitarInicioPosicionamiento".equals(message.getEventType());
            }

            @Override
            public void onMessage(EventMessage message) {
                System.out.println("Servidor: El Host inició la partida. Notificando a todos...");
                // Rebotamos la señal como un evento público a todos los clientes
                EventMessage eventoInicio = new EventMessage("InicioPosicionamiento", "GO");
                publisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoInicio);
            }
        });

        // -----------------------------------------------------------
        // 5. INICIO DE SUSCRIPCIONES (ARRANQUE)
        // -----------------------------------------------------------
        // Suscripción al canal de COMANDOS (Lo que los clientes piden al servidor)
        IMessageSubscriber commandSubscriber = provider.getSubscriber();
        if (commandSubscriber != null) {
            commandSubscriber.subscribe(BrokerConfig.CHANNEL_COMANDOS);
        }

        // Suscripción al canal de EVENTOS (Opcional, para logs o monitoreo)
        IMessageSubscriber eventSubscriber = provider.getSubscriber();
        if (eventSubscriber != null) {
            eventSubscriber.subscribe(BrokerConfig.CHANNEL_EVENTOS);
        }

        System.out.println("************************************************************");
        System.out.println("Battleship Servidor LISTO (Provider: Redis).");
        System.out.println("Esperando comandos en: '" + BrokerConfig.CHANNEL_COMANDOS + "'");
        System.out.println("************************************************************");

        // Shutdown Hook para cerrar recursos al detener el servidor
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando servidor...");
            provider.close();
        }));
    }
}
