/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Nave;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.message.EventMessage;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Cricri
 */
public class CrearPartidaVsIAService {

    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final PartidaTimerService partidaTimerService;
    private final Gson gson;
    private final String IA_PLAYER_ID = "JUGADOR_IA_01"; // ID Fijo para la IA

    public CrearPartidaVsIAService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher, PartidaTimerService partidaTimerService, Gson gson) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
        this.partidaTimerService = partidaTimerService;
        this.gson = gson;
    }

    public void execute(CrearPartidaVsIARequest request) {
        try {
            //Crear Jugadores
            Jugador jugadorHumano = new Jugador(request.getJugadorHumanoId(), "Humano");
            Jugador jugadorIA = new Jugador(IA_PLAYER_ID, "IA");

            //Crear Partida
            Partida partida = new Partida(jugadorHumano);

            //Posicionar Naves
            List<Nave> navesHumano = request.getNavesHumano().stream()
                    .map(dto -> new Nave(dto.getTipo(), dto.getCoordenadas(), dto.getOrientacion()))
                    .collect(Collectors.toList());
            partida.posicionarNaves(jugadorHumano.getId(), navesHumano);

            List<Nave> navesIA = request.getNavesIA().stream()
                    .map(dto -> new Nave(dto.getTipo(), dto.getCoordenadas(), dto.getOrientacion()))
                    .collect(Collectors.toList());

            //Unir IA y posicionar sus naves
            partida.unirseAPartida(jugadorIA, jugadorHumano.getId());
            partida.posicionarNaves(jugadorIA.getId(), navesIA);

            //Guardar
            partidaRepository.guardar(partida);
            System.out.println("Partida Vs IA creada. Turno de: " + partida.getTurnoActual());

            //Preparar respuesta
            JugadorDTO j1DTO = new JugadorDTO(jugadorHumano.getId(), "Humano", null, null);
            JugadorDTO j2DTO = new JugadorDTO(jugadorIA.getId(), "IA", null, null);

            PartidaIniciadaResponse response = new PartidaIniciadaResponse(
                    partida.getId().toString(),
                    j1DTO,
                    j2DTO,
                    partida.getEstado(),
                    partida.getTurnoActual()
            );

            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS,
                    new EventMessage("PartidaIniciada", gson.toJson(response)));
            
            partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);

        } catch (Exception e) {
            System.err.println("Error al crear partida vs IA: " + e.getMessage());
            EventMessage errorMsg = new EventMessage("ErrorCrearPartida", gson.toJson(e.getMessage()));
            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, errorMsg);
        }
    }
}