/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import java.util.UUID;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.PartidaDTO;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;
import mx.itson.equipo_2.common.dto.response.ErrorResponse;
import mx.itson.equipo_2.common.enums.AccionPartida;
import mx.itson.equipo_2.common.enums.ColorJugador;
import mx.itson.equipo_2.common.enums.EstadoJugador;

public class RegistrarJugadorService {

    private final IMessagePublisher eventPublisher;
    private final Gson gson;
    private final IPartidaRepository partidaRepository;

    public RegistrarJugadorService(IMessagePublisher eventPublisher, Gson gson, IPartidaRepository partidaRepository) {
        this.eventPublisher = eventPublisher;
        this.gson = gson;
        this.partidaRepository = partidaRepository;
    }

    public void execute(RegistrarJugadorRequest request) {
        try {
            if (request.getAccion() == null) {
                sendError("Error: No se especificó si Crear o Unirse a la partida.");
                return;
            }

            Partida partidaActual = partidaRepository.getPartida();

            // VALIDACIONES DE LÓGICA
            if (request.getAccion() == AccionPartida.CREAR && partidaActual != null) {
                sendError("Ya existe una partida creada. Intenta unirte.");
                return;
            }

            if (request.getAccion() == AccionPartida.CREAR && partidaActual != null) {
                sendError("Ya existe una partida creada. Intenta unirte.");
                return;
            }
            if (request.getAccion() == AccionPartida.UNIRSE && partidaActual == null) {
                sendError("No hay partida para unirse. Crea una primero.");
                return;
            }
            if (request.getAccion() == AccionPartida.UNIRSE && partidaActual != null && partidaActual.getJugador2() != null) {
                sendError("La sala está llena.");
                return;
            }

            // 2. PROCESAMIENTO
            String nuevoId = UUID.randomUUID().toString();

            // USAMOS EL COLOR DEL REQUEST
            ColorJugador colorAsignado = request.getColor() != null ? request.getColor() : ColorJugador.AZUL;

            Jugador nuevoJugador;

            if (request.getAccion() == AccionPartida.CREAR) {

                nuevoJugador = new Jugador(nuevoId, request.getNombre(), colorAsignado);
                nuevoJugador.setEstado(EstadoJugador.POSICIONANDO);

                Partida nuevaPartida = new Partida(nuevoJugador);
                partidaRepository.guardar(nuevaPartida);
                System.out.println("Partida CREADA por: " + request.getNombre() + " con color " + colorAsignado);

            } else {

                nuevoJugador = new Jugador(nuevoId, request.getNombre(), colorAsignado);
                nuevoJugador.setEstado(EstadoJugador.POSICIONANDO);

                partidaActual.setJugador2(nuevoJugador);
                partidaRepository.guardar(partidaActual);
                System.out.println("Jugador UNIDO: " + request.getNombre() + " con color " + colorAsignado);
            }

            JugadorDTO jugadorDto = new JugadorDTO();
            jugadorDto.setId(nuevoId);
            jugadorDto.setNombre(request.getNombre());
            jugadorDto.setColor(colorAsignado);

            EventMessage eventoPrivado = new EventMessage("JugadorRegistrado", gson.toJson(jugadorDto));
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoPrivado);

            broadcastEstadoSala();

        } catch (Exception e) {
            System.err.println("Error en RegistrarJugadorService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void broadcastEstadoSala() {
        Partida partida = partidaRepository.getPartida();
        if (partida == null) {
            return;
        }

        // Mapear J1
        JugadorDTO j1DTO = new JugadorDTO(partida.getJugador1().getId(), partida.getJugador1().getNombre(), partida.getJugador1().getColor(), null, null);

        // Mapear J2 (puede ser null)
        JugadorDTO j2DTO = null;
        if (partida.getJugador2() != null) {
            j2DTO = new JugadorDTO(partida.getJugador2().getId(), partida.getJugador2().getNombre(), partida.getJugador2().getColor(), null, null);
        }

        // DTO Global de Sala
        PartidaDTO partidaDTO = new PartidaDTO(null, j1DTO, j2DTO, partida.getEstado());

        EventMessage eventoPublico = new EventMessage("PartidaActualizada", gson.toJson(partidaDTO));
        eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, eventoPublico);
    }

    private void sendError(String msg) {
        System.out.println("Enviando error: " + msg);
        ErrorResponse error = new ErrorResponse(msg);
        EventMessage message = new EventMessage("EXCEPTION", gson.toJson(error));
        eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, message);
    }
}
