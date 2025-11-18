/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.AbandonarPartidaRequest;
import mx.itson.equipo_2.common.dto.response.PartidaFinalizadaResponse;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author Alberto Jimenez
 */
public class AbandonarPartidaService {
    
    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher publisher;
    private final PartidaTimerService timerService;
    private final Gson gson = new Gson();

    public AbandonarPartidaService(IPartidaRepository repo, IMessagePublisher pub, PartidaTimerService timer) {
        this.partidaRepository = repo;
        this.publisher = pub;
        this.timerService = timer;
    }

    public void procesarAbandono(AbandonarPartidaRequest request) {
        timerService.cancelCurrentTimer();
        Partida partida = partidaRepository.getPartida();
        
        // 1. Finalizar en el modelo (Igual que antes)
        partida.finalizarPartida(request.getJugadorId());
        partidaRepository.guardar(partida);
        
        PartidaFinalizadaResponse response = new PartidaFinalizadaResponse(
            partida.getTurnoActual(), // El ganador (que se calculó en finalizarPartida)
            "El oponente ha abandonado la partida." // El motivo
        );

        // Publicamos el evento "PartidaFinalizada"
        publisher.publish(
            BrokerConfig.CHANNEL_EVENTOS, 
            new EventMessage("PartidaFinalizada", new Gson().toJson(response))
        );
    }
    
}
