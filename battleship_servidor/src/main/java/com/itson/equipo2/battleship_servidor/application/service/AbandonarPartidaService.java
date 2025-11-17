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
        
        // 1. Finalizar en el modelo
        partida.finalizarPartida(request.getJugadorId());
        partidaRepository.guardar(partida);

        // 2. Avisar al cliente que el estado ahora es FINALIZADA
        TurnoTickResponse response = new TurnoTickResponse(
            partida.getTurnoActual(), 
            0, 
            partida.getEstado()
        );
        publisher.publish(BrokerConfig.CHANNEL_EVENTOS, new EventMessage("TurnoTick", new Gson().toJson(response)));
    }
    
}
