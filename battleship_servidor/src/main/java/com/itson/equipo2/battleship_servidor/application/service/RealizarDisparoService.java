/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.ErrorResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.response.PartidaFinalizadaResponse;

/**
 *
 * @author skyro
 */
public class RealizarDisparoService {

    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final PartidaTimerService partidaTimerService;
    private final Gson gson = new Gson();

    public RealizarDisparoService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher, PartidaTimerService partidaTimerService) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
        this.partidaTimerService = partidaTimerService;
    }

    public void realizarDisparo(RealizarDisparoRequest request) {

        System.out.println("DISPARO PARA PROCESAR EN:" + request.getCoordenada().toString());
        try {
            partidaTimerService.cancelCurrentTimer();

            Partida partida = partidaRepository.getPartida();
            if (partida == null) {
                throw new Exception("Error: No se encontró la partida en el repositorio.");
            }

            // 2. DELEGAR AL DOMINIO
            ResultadoDisparoReponse response = partida.realizarDisparo(
                    request.getJugadorId(),
                    request.getCoordenada()
            );

            // Llamar a todas las partes del dominio interesadas en este evento...
            CoordenadaDTO coordenada = request.getCoordenada();
            partida.getJugadorById(request.getJugadorId()).addDisparo(coordenada.getFila(), coordenada.getColumna(), response.getResultado());

            // 3. GUARDAR ESTADO
            partidaRepository.guardar(partida);

            // 4. PUBLICAR RESPUESTA
            EventMessage eventoResultado = new EventMessage(
                    "DisparoRealizado",
                    gson.toJson(response)
            );
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoResultado);
            System.out.println("Disparo procesado. Resultado: " + response);

            if (partida.getEstado() == EstadoPartida.EN_BATALLA) {
                partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
            }

            // 5. VERIFICAR SI LA PARTIDA TERMINÓ (Lógica Nueva)
            if (partida.getEstado() == EstadoPartida.FINALIZADA) {

                System.out.println("¡Juego terminado por disparo! Ganador: " + request.getJugadorId());

                // Creamos el evento específico que pidió el comentario
                PartidaFinalizadaResponse finResponse = new PartidaFinalizadaResponse(
                        request.getJugadorId(), // El que disparó es el ganador
                        "Todas las naves enemigas fueron destruidas."
                );

                EventMessage eventoFin = new EventMessage(
                        "PartidaFinalizada",
                        gson.toJson(finResponse)
                );

                // Publicamos el evento de finalización
                eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoFin);

            } else if (partida.getEstado() == EstadoPartida.EN_BATALLA) {
                // Si NO terminó, reiniciamos el timer
                partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
            }

        } catch (IllegalStateException e) {
            System.out.println("INFO: Disparo rechazado (fuera de turno): " + e.getMessage());
            ErrorResponse error = new ErrorResponse(e.getMessage());
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, new EventMessage("ErrorDisparo", gson.toJson(error)));

            Partida partida = partidaRepository.getPartida();
            if (partida != null && partida.getEstado() == EstadoPartida.EN_BATALLA) {
                partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
            }

        } catch (Exception e) {
            System.err.println("Error INESPERADO procesando el disparo: " + e.getMessage());
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse("Error inesperado en el servidor: " + e.getMessage());
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, new EventMessage("ErrorGrave", gson.toJson(error)));
        }
    }
}
