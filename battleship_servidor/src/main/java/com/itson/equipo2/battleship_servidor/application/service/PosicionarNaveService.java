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
import com.itson.equipo2.battleship_servidor.domain.model.Tablero;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import java.util.List;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.CrearPartidaVsIARequest;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;
import mx.itson.equipo_2.common.dto.response.NavesPosicionadasResponse;
import mx.itson.equipo_2.common.enums.EstadoJugador;

/**
 *
 * @author skyro
 */
public class PosicionarNaveService {

    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final Gson gson = new Gson();

    private CrearPartidaVsIAService crearPartidaVsIAService;

    public PosicionarNaveService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
    }

    public void posicionarNaves(PosicionarFlotaRequest request) {

        // obtiene la partida en curso
        Partida partida = partidaRepository.getPartida();

        // obtiene el jugador filtrado por su ID
        Jugador jugador = partida.getJugadorById(request.getJugadorId());

        // mapea las NaveDTO a nave
        List<Nave> naves = request.getNaves().stream().map((n) -> 
                new Nave(
                        n.getTipo(),
                        // Coordenada inicial de la nave...
                        new Coordenada(
                                n.getCoordenadaInicial().getFila(), 
                                n.getCoordenadaInicial().getColumna()), 
                        n.getOrientacion()
                )).toList();

        System.out.println("Total de barcos: " + naves.size());
//        int n = 1;
//        for (Nave nave : naves) {
//            partida.posicionarNaves(jugador.getId(), naves);
//            System.out.println("---------------");
//            System.out.println("Nave posicionada:" + n);
//            System.out.println("Tipo: " + nave.getTipo());
//            System.out.println("Coordendas: " + nave.getCoordenadas().toString());
//            System.out.println("---------------");
//            n++;
//        }

        partida.posicionarNaves(jugador.getId(), naves);
        jugador.setEstado(EstadoJugador.LISTO);

        NavesPosicionadasResponse response = new NavesPosicionadasResponse(request.getJugadorId());
        String payload = gson.toJson(response);

        EventMessage message = new EventMessage("NavesPosicionadas", payload);
        eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, message);

        if (partida.getEnemigo(jugador.getId()).getEstado() == EstadoJugador.LISTO) {
            crearPartidaVsIAService.execute(request.getNaves());
        }
    }

    public void setCrearPartidaVsIAService(CrearPartidaVsIAService crearPartidaVsIAService) {
        this.crearPartidaVsIAService = crearPartidaVsIAService;
    }

}
