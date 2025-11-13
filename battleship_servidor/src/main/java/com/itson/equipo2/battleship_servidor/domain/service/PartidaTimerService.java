/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import java.util.Timer;
import java.util.TimerTask;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class PartidaTimerService {

    private Timer timer;
    private final Gson gson = new Gson();

    /**
     * Inicia un nuevo temporizador de turno. Cancela cualquier timer anterior.
     */
    public void startTurnoTimer(IPartidaRepository repo, IMessagePublisher publisher) {
        cancelCurrentTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Partida partida = repo.getPartida();
                    if (partida == null || partida.getEstado() != EstadoPartida.EN_BATALLA) {
                        cancel(); 
                        return;
                    }

                    int tiempo = partida.decrementarYObtenerTiempo();
                    repo.guardar(partida); 

                    TurnoTickResponse tick = new TurnoTickResponse(partida.getTurnoActual(), tiempo);
                    publisher.publish(RedisConfig.CHANNEL_EVENTOS, new EventMessage("TurnoTick", gson.toJson(tick)));

                    if (tiempo <= 0) {
                        System.out.println("¡Tiempo agotado! Forzando cambio de turno.");
                        cancel(); 

                        partida.cambiarTurno(); 
                        repo.guardar(partida);

                        TurnoTickResponse timeoutTick = new TurnoTickResponse(partida.getTurnoActual(), partida.getTiempoRestante());
                        publisher.publish(RedisConfig.CHANNEL_EVENTOS, new EventMessage("TurnoTick", gson.toJson(timeoutTick)));
                        
                        startTurnoTimer(repo, publisher);
                    }
                } catch (Exception e) {
                    System.err.println("Error en PartidaTimerService: " + e.getMessage());
                    e.printStackTrace();
                    cancel();
                }
            }
        }, 1000, 1000);
    }

    /**
     * Detiene el temporizador actual (ej. cuando se recibe un disparo).
     */
    public void cancelCurrentTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
