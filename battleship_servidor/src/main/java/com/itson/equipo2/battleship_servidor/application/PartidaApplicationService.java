/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Nave;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.domain.service.CrearPartidaVsIAService;
import com.itson.equipo2.battleship_servidor.domain.service.PartidaTimerService;
import com.itson.equipo2.battleship_servidor.domain.service.RealizarDisparoService;
import com.itson.equipo2.battleship_servidor.handler.CrearPartidaVsIAHandler;
import com.itson.equipo2.battleship_servidor.handler.RealizarDisparoHandler;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.EventDispatcher;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import com.itson.equipo2.battleship_servidor.infrastructure.service.AIService;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author Cricri
 */
public class PartidaApplicationService {

    // Esta clase ya no implementa IMessageHandler,
    // ahora los Handlers específicos lo hacen.

    public PartidaApplicationService(
            IPartidaRepository partidaRepository,
            IMessagePublisher eventPublisher,
            Gson gson,
            AIService aiService,
            EventDispatcher eventDispatcher // El router principal
    ) {
        // 1. Crear el Timer Service
        PartidaTimerService timerService = new PartidaTimerService();
        
        // 2. Crear el Servicio de "Realizar Disparo"
        RealizarDisparoService disparoService = new RealizarDisparoService(
                partidaRepository, 
                eventPublisher, 
                timerService
        );
        
        // 3. Crear el Servicio de "Crear Partida"
        CrearPartidaVsIAService crearPartidaService = new CrearPartidaVsIAService(
                partidaRepository, 
                eventPublisher, 
                timerService, 
                gson
        );
        
        // --- REGISTRAR LOS HANDLERS EN EL DISPATCHER ---
        // (Como te dijo tu compañero, "registrarlos")
        
        // Cuando llegue un evento "RealizarDisparo", 
        // el dispatcher llamará a este handler
        eventDispatcher.subscribe(
                "RealizarDisparo", 
                new RealizarDisparoHandler(disparoService)
        );

        // Cuando llegue un evento "CrearPartidaVsIA",
        // el dispatcher llamará a este handler
        eventDispatcher.subscribe(
                "CrearPartidaVsIA", 
                new CrearPartidaVsIAHandler(crearPartidaService)
        );
        
        // Registrar el AIService para que escuche eventos
        eventDispatcher.subscribe("TurnoTick", aiService);
    }
}
