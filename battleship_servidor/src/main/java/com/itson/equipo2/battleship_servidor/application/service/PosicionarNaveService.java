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
import java.util.stream.Collectors;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.PosicionarFlotaRequest;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.enums.EstadoJugador;
import mx.itson.equipo_2.common.enums.EstadoPartida;

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

    private PartidaTimerService partidaTimerService;

    public void setPartidaTimerService(PartidaTimerService partidaTimerService) {
        this.partidaTimerService = partidaTimerService;
    }

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
        Partida partida = partidaRepository.getPartida();
        if (partida == null) {
            return;
        }

        Jugador jugador = partida.getJugadorById(request.getJugadorId());
        if (jugador == null) {
            return;
        }

        // 1. Mapear DTOs a Entidades de Dominio
        List<Nave> navesDominio = request.getNaves().stream().map(nDTO
                -> new Nave(
                        nDTO.getTipo(),
                        new Coordenada(nDTO.getCoordenadaInicial().getFila(), nDTO.getCoordenadaInicial().getColumna()),
                        nDTO.getOrientacion()
                )
        ).collect(Collectors.toList());

        // 2. Guardar en el dominio
        partida.posicionarNaves(jugador.getId(), navesDominio);
        jugador.setEstado(EstadoJugador.LISTO);

        System.out.println("Servidor: Jugador " + jugador.getNombre() + " está LISTO.");

        // 3. VERIFICAR SI AMBOS ESTÁN LISTOS
        boolean j1Listo = partida.getJugador1().getEstado() == EstadoJugador.LISTO;
        boolean j2Listo = partida.getJugador2() != null && partida.getJugador2().getEstado() == EstadoJugador.LISTO;

        if (j1Listo && j2Listo) {
            System.out.println("Servidor: ¡AMBOS JUGADORES LISTOS! Iniciando batalla...");
            iniciarBatalla(partida);
        } else {
            // Opcional: Notificar al otro que estamos esperando
            System.out.println("Servidor: Esperando al otro jugador...");
        }

        partidaRepository.guardar(partida);
    }

    private void iniciarBatalla(Partida partida) {
        partida.setEstado(EstadoPartida.EN_BATALLA);

        if (partida.getTurnoActual() == null) {
            partida.setTurnoActual(partida.getJugador1().getId());
        }
        partida.iniciarTurno();

        JugadorDTO j1DTO = new JugadorDTO(partida.getJugador1().getId(), partida.getJugador1().getNombre(), partida.getJugador1().getColor(), null, null);
        JugadorDTO j2DTO = new JugadorDTO(partida.getJugador2().getId(), partida.getJugador2().getNombre(), partida.getJugador2().getColor(), null, null);

        PartidaIniciadaResponse response = new PartidaIniciadaResponse(
                partida.getId().toString(),
                j1DTO,
                j2DTO,
                partida.getEstado(),
                partida.getTurnoActual()
        );

        EventMessage evento = new EventMessage("PartidaIniciada", gson.toJson(response));
        eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, evento);

        if (partidaTimerService != null) {
            partidaTimerService.startTurnoTimer(partidaRepository, eventPublisher);
        }
    }

    /**
     * Setter para inyectar el servicio de inicio de partida.
     *
     * @param crearPartidaVsIAService El servicio para iniciar la lógica del
     * juego.
     */
//    public void setCrearPartidaVsIAService(CrearPartidaVsIAService crearPartidaVsIAService) {
//        this.crearPartidaVsIAService = crearPartidaVsIAService;
//    }
}
