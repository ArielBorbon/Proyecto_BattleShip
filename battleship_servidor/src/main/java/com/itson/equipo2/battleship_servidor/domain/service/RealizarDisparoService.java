/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.persistence.PartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.ErrorResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.message.EventMessage;

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
        try {
            // 0. DETENER EL TIMER
            partidaTimerService.cancelCurrentTimer();
            
            // 1. OBTENER DATOS
            Partida partida = partidaRepository.getPartida(); // Usando la versión "singleton"
            if (partida == null) {
                throw new Exception("Error: No se encontró la partida en el repositorio.");
            }

            // 2. DELEGAR AL DOMINIO
            ResultadoDisparoReponse resultadoResponse = partida.realizarDisparo(
                    request.getJugadorId(),
                    request.getCoordenada()
            );

            // 3. GUARDAR ESTADO
            partidaRepository.guardar(partida);

            // 4. PUBLICAR RESPUESTA
            EventMessage eventoResultado = new EventMessage(
                    "DisparoRealizado",
                    gson.toJson(resultadoResponse)
            );
            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, eventoResultado);
            System.out.println("Disparo procesado. Resultado: " + resultadoResponse);

            // 5. REINICIAR EL TIMER
            if (partida.getEstado() == EstadoPartida.EN_BATALLA) {
                 partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
            }

        } catch (IllegalStateException e) {
            // Error esperado (disparo fuera de turno)
            System.out.println("INFO: Disparo rechazado (fuera de turno): " + e.getMessage());
            // Opcional: publicar un evento de error leve
            ErrorResponse error = new ErrorResponse(e.getMessage());
            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, new EventMessage("ErrorDisparo", gson.toJson(error)));
            
            // ¡Importante! Reiniciar el timer aunque el disparo haya fallado
            // para que el turno del jugador actual no se pierda.
             Partida partida = partidaRepository.getPartida();
             if (partida != null && partida.getEstado() == EstadoPartida.EN_BATALLA) {
                 partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
             }

        } catch (Exception e) {
            // Errores inesperados
            System.err.println("Error INESPERADO procesando el disparo: " + e.getMessage());
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse("Error inesperado en el servidor: " + e.getMessage());
            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, new EventMessage("ErrorGrave", gson.toJson(error)));
        }
    }
}
    

