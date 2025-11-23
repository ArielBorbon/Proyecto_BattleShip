
package com.itson.equipo2.battleship_servidor.application.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.PartidaDTO;
import mx.itson.equipo_2.common.dto.request.AbandonarPartidaRequest;
import mx.itson.equipo_2.common.dto.response.PartidaFinalizadaResponse;
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
        Partida partida = partidaRepository.getPartida();
        if (partida == null) {
            return;
        }

        if (partida.getEstado() == EstadoPartida.CONFIGURACION) {
            manejarSalidaLobby(partida, request.getJugadorId());
            return;
        }

        timerService.cancelCurrentTimer();
        partida.finalizarPartida(request.getJugadorId());
        partidaRepository.guardar(partida);
        timerService.cancelCurrentTimer();

        // 1. Finalizar en el modelo
        partida.finalizarPartida(request.getJugadorId());
        partidaRepository.guardar(partida);

        PartidaFinalizadaResponse response = new PartidaFinalizadaResponse(
                partida.getTurnoActual(), // El ganador 
                "El oponente ha abandonado la partida." // El motivo
        );

        // 2. Avisar al cliente que el estado ahora es FINALIZADA
        publisher.publish(
                BrokerConfig.CHANNEL_EVENTOS,
                new EventMessage("PartidaFinalizada", new Gson().toJson(response))
        );
    }

    public void execute(AbandonarPartidaRequest request) {
        Partida partida = partidaRepository.getPartida();
        if (partida == null) {
            return;
        }

        if (partida.getEstado() == EstadoPartida.CONFIGURACION) {

            if (partida.getJugador1().getId().equals(request.getJugadorId())) {
                System.out.println("El Host canceló la sala. Destruyendo partida...");
                partidaRepository.eliminarPartida();

                EventMessage msg = new EventMessage("PartidaCancelada", "El host ha cerrado la sala.");
                publisher.publish(BrokerConfig.CHANNEL_EVENTOS, msg);
            } 
            else if (partida.getJugador2() != null && partida.getJugador2().getId().equals(request.getJugadorId())) {
                System.out.println("El invitado salió de la sala. Liberando slot...");
                partida.setJugador2(null); 
                enviarActualizacionSala(partida);
            }
            return;
        }
    }


    private void manejarSalidaLobby(Partida partida, String jugadorId) {
        if (partida.getJugador1().getId().equals(jugadorId)) {
            System.out.println("El Host canceló la sala. Destruyendo partida...");
            partidaRepository.eliminarPartida(); 

            EventMessage msg = new EventMessage("PartidaCancelada", "El host ha cerrado la sala.");
            publisher.publish(BrokerConfig.CHANNEL_EVENTOS, msg);
        } 
        else if (partida.getJugador2() != null && partida.getJugador2().getId().equals(jugadorId)) {
            System.out.println("El invitado salió de la sala. Liberando slot...");
            partida.setJugador2(null); 
            partidaRepository.guardar(partida);

            enviarActualizacionSala(partida);
        }
    }

    private void enviarActualizacionSala(Partida partida) {
        JugadorDTO j1DTO = new JugadorDTO(
                partida.getJugador1().getId(),
                partida.getJugador1().getNombre(),
                partida.getJugador1().getColor(), null, null
        );

        JugadorDTO j2DTO = null; //se acaba de ir asi que lo ponemos nuull

        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setJugador1(j1DTO);
        partidaDTO.setJugador2(j2DTO);
        partidaDTO.setEstado(partida.getEstado());

        publisher.publish(BrokerConfig.CHANNEL_EVENTOS,
                new EventMessage("PartidaActualizada", gson.toJson(partidaDTO)));
    }

}
