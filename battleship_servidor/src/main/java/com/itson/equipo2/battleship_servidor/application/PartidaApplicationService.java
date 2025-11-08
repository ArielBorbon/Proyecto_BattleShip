/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoDTO;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author Cricri
 */
public class PartidaApplicationService implements IMessageHandler {
  private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final Gson gson;

    public PartidaApplicationService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher, Gson gson) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
        this.gson = gson;
    }

    @Override
    public void onMessage(EventMessage eventMessage) {
        try {
            String tipo = eventMessage.getEventType();

            if (tipo == null) {
                System.err.println("Comando inválido, no contiene 'type': " + eventMessage);
                return;
            }

            System.out.println("Procesando comando tipo: " + tipo);

            switch (tipo) {
                case "RegistrarJugador":
                    System.out.println("Lógica de 'RegistrarJugador' iría aquí.");
                    break;

                case "RealizarDisparo":
                    RealizarDisparoRequest disparoRequest = gson.fromJson(
                            eventMessage.getPayload(),
                            RealizarDisparoRequest.class
                    );
                    procesarDisparo(disparoRequest);
                    break;

                case "PosicionarNave":
                    System.out.println("Lógica de 'PosicionarNave' iría aquí.");
                    break;

                default:
                    System.err.println("Tipo de comando desconocido: " + tipo);
            }

        } catch (JsonSyntaxException e) {
            System.err.println("Error de sintaxis JSON al procesar mensaje: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al manejar comando: " + e.getMessage());
            e.printStackTrace();
        }
    }

   private void procesarDisparo(RealizarDisparoRequest request) {
        try {
            // --- 1. OBTENER DATOS ---
            // (Usando la versión de "una sola partida")
            Partida partida = partidaRepository.getPartida();

            if (partida == null) {
                System.err.println("Error: No se encontró la partida en el repositorio.");
                return;
            }

            // --- 2. DELEGAR AL DOMINIO ---
            ResultadoDisparo resultado = partida.realizarDisparo(
                    request.getJugadorId(),
                    request.getCoordenada()
            );

            // --- 3. GUARDAR ESTADO ---
            partidaRepository.guardar(partida);

            
            ResultadoDisparoDTO resultadoDTO = new ResultadoDisparoDTO(
                    request.getCoordenada(),
                    resultado,
                    request.getJugadorId()
            );

            EventMessage eventoResultado = new EventMessage(
                    "DisparoRealizado", 
                    gson.toJson(resultadoDTO)
            );

            eventPublisher.publish(RedisConfig.CHANNEL_EVENTOS, eventoResultado);

            System.out.println("Disparo procesado. Resultado: " + resultado);

        } catch (Exception e) { 
            System.err.println("Error procesando el disparo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return true;
    }
}