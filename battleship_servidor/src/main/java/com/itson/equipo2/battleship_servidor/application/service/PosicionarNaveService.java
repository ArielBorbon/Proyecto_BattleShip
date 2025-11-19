package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Coordenada;
import com.itson.equipo2.battleship_servidor.domain.model.Jugador;
import com.itson.equipo2.battleship_servidor.domain.model.Nave;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import java.util.List;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;
import mx.itson.equipo_2.common.dto.response.NavesPosicionadasResponse;
import mx.itson.equipo_2.common.enums.EstadoJugador;

/**
 * Servicio de aplicación encargado de procesar la solicitud de posicionamiento
 * de naves, actualizar el estado de la partida y notificar al Broker.
 */
public class PosicionarNaveService {

    /**
     * Repositorio para acceder y modificar el objeto Partida en curso.
     */
    private final IPartidaRepository partidaRepository;

    /**
     * Publicador de eventos para enviar notificaciones al Broker.
     */
    private final IMessagePublisher eventPublisher;

    /**
     * Instancia de Gson para serialización.
     */
    private final Gson gson = new Gson();

    /**
     * Dependencia de otro servicio para iniciar el juego si ambos jugadores
     * están listos.
     */
    private CrearPartidaVsIAService crearPartidaVsIAService;

    // --- CONSTRUCTOR ---
    /**
     * Inicializa el servicio de posicionamiento con sus dependencias.
     *
     * @param partidaRepository El repositorio de la partida.
     * @param eventPublisher El publicador de mensajes del Broker.
     */
    public PosicionarNaveService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
    }

    // --- MÉTODOS PÚBLICOS ---
    /**
     * Procesa la solicitud de posicionamiento de naves de un jugador.
     *
     * @param request Solicitud que contiene el ID del jugador y la lista de
     * {@code NaveDTO}.
     */
    public void posicionarNaves(PosicionarFlotaRequest request) {

        // 1. Obtiene la partida y el jugador
        Partida partida = partidaRepository.getPartida();
        Jugador jugador = partida.getJugadorById(request.getJugadorId());

        // 2. Mapea los DTOs de Naves a los objetos del Dominio (Nave)
        List<Nave> naves = request.getNaves().stream().map((n)
                -> new Nave(
                        n.getTipo(),
                        // Mapea el DTO de Coordenada a la Coordenada del Dominio
                        new Coordenada(
                                n.getCoordenadaInicial().getFila(),
                                n.getCoordenadaInicial().getColumna()),
                        n.getOrientacion()
                )).toList();

        // 3. Aplica la lógica de dominio
        partida.posicionarNaves(jugador.getId(), naves);
        jugador.setEstado(EstadoJugador.LISTO); // Marca al jugador como listo para el juego

        // 4. Notifica al cliente que este jugador ha posicionado su flota
        NavesPosicionadasResponse response = new NavesPosicionadasResponse(request.getJugadorId());
        String payload = gson.toJson(response);
        EventMessage message = new EventMessage("NavesPosicionadas", payload);
        eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, message);

        // 5. Verifica si el otro jugador (enemigo) también está listo
        if (partida.getEnemigo(jugador.getId()).getEstado() == EstadoJugador.LISTO) {
            // Si ambos están listos, se inicia la siguiente fase del juego (PartidaVsIA)
            if (crearPartidaVsIAService != null) {
                crearPartidaVsIAService.execute(request.getNaves());
            }
        }
    }

    /**
     * Setter para inyectar el servicio de inicio de partida.
     *
     * @param crearPartidaVsIAService El servicio para iniciar la lógica del
     * juego.
     */
    public void setCrearPartidaVsIAService(CrearPartidaVsIAService crearPartidaVsIAService) {
        this.crearPartidaVsIAService = crearPartidaVsIAService;
    }

}
