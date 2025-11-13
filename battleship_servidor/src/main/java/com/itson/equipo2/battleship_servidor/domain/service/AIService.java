/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.model.Tablero;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class AIService implements IMessageHandler {

    private final Random random = new Random();
    private final Gson gson = new Gson();
    private final IPartidaRepository partidaRepository; 
    private final IMessagePublisher publisher; 
    private final String IA_PLAYER_ID = "JUGADOR_IA_01"; 

    public AIService(IPartidaRepository partidaRepository , IMessagePublisher publisher ) {
        this.partidaRepository = partidaRepository;
        this.publisher = publisher; 
    }

    public void solicitarTurnoIA() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Partida partidaActual = partidaRepository.getPartida();
                if (partidaActual == null) {
                    System.err.println("AIService: No se encontró partida.");
                    return;
                }

                if (partidaActual.getEstado() != EstadoPartida.EN_BATALLA
                        || !partidaActual.getTurnoActual().equals("JUGADOR_IA_01")) { // Usar el ID Fijo

                    System.out.println("AIService: El turno cambió o la partida terminó. Abortando disparo de IA.");
                    return;
                }

                Tablero tableroEnemigo = (partidaActual.getTurnoActual().equals(partidaActual.getJugador1().getId()))
                        ? partidaActual.getTableroJugador2()
                        : partidaActual.getTableroJugador1();

                CoordenadaDTO coordIA = generarDisparo(tableroEnemigo);

                RealizarDisparoRequest aiRequest = new RealizarDisparoRequest(
                        partidaActual.getTurnoActual(), 
                        coordIA
                );

                System.out.println("AIService: IA disparando en: " + coordIA);

                EventMessage message = new EventMessage("RealizarDisparo", gson.toJson(aiRequest));
                publisher.publish(RedisConfig.CHANNEL_COMANDOS, message);
            }
        }, 2000); 
    }

    private CoordenadaDTO generarDisparo(Tablero tableroEnemigo) {
        int fila, col;
        do {
            fila = random.nextInt(Tablero.FILAS);
            col = random.nextInt(Tablero.COLUMNAS);
        } while (tableroEnemigo.getCelda(fila, col).getEstado() == EstadoCelda.DISPARADA);

        return new CoordenadaDTO(fila, col);
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "TurnoTick".equals(message.getEventType());
    }

/**
     * Este método es llamado por el suscriptor de Redis
     * cuando llega un evento "TurnoTick".
     */
    @Override
    public void onMessage(EventMessage message) {
        TurnoTickResponse tick = gson.fromJson(message.getPayload(), TurnoTickResponse.class);
        
        if (tick.getJugadorEnTurnoId().equals(IA_PLAYER_ID)) {
            if (tick.getTiempoRestante() > 28) { 
                 System.out.println("AIService (Handler): Detectado inicio de turno de IA. Solicitando disparo...");
                 solicitarTurnoIA();
            }
        }
    }
}
