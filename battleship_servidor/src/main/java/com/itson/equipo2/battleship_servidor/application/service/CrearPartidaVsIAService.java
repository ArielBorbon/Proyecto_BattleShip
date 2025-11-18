/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Coordenada;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Nave;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import com.itson.equipo2.communication.dto.EventMessage;
import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.NaveDTO;
import mx.itson.equipo_2.common.enums.ColorJugador;

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

    public void execute(List<NaveDTO> naves) {
        try {
            Partida partida = partidaRepository.getPartida();
            
            //Crear Jugadores
            Jugador jugadorHumano = partida.getJugadorById("JUGADOR_1");
            Jugador jugadorIA = partida.getJugadorById(IA_PLAYER_ID);

            System.out.println("Partida Vs IA creada. Turno de: " + partida.getTurnoActual());

            //Preparar respuesta
            JugadorDTO j1DTO = new JugadorDTO(jugadorHumano.getId(), jugadorHumano.getNombre(), jugadorHumano.getColor(), null, null);
            JugadorDTO j2DTO = new JugadorDTO(jugadorIA.getId(), jugadorIA.getNombre(), jugadorIA.getColor(), null, null);

            PartidaIniciadaResponse response = new PartidaIniciadaResponse(
                    partida.getId().toString(),
                    j1DTO,
                    j2DTO,
                    partida.getEstado(),
                    partida.getTurnoActual()
            );

            // 7. Publicar evento de éxito
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS,
                    new EventMessage("PartidaIniciada", gson.toJson(response)));

            partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);

        } catch (Exception e) {
            System.err.println("Error al crear partida vs IA: " + e.getMessage());
            e.printStackTrace();
            EventMessage errorMsg = new EventMessage("ErrorCrearPartida", gson.toJson(e.getMessage()));
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, errorMsg);
        }
    }
}
