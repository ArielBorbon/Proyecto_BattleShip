/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.model.Tablero;
import com.itson.equipo2.battleship_servidor.infrastructure.redis.RedisConfig;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class AIService {

    private final Random random = new Random();
    private final Gson gson = new Gson();

    public void solicitarTurnoIA(Partida partida, IMessagePublisher publisher) {
        // Añade un retraso para simular el "pensamiento"
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Tablero tableroEnemigo = (partida.getTurnoActual().equals(partida.getJugador1().getId()))
                        ? partida.getTableroJugador2()
                        : partida.getTableroJugador1();

                CoordenadaDTO coordIA = generarDisparo(tableroEnemigo);

                RealizarDisparoRequest aiRequest = new RealizarDisparoRequest(
                        partida.getTurnoActual(), // El ID del jugador IA
                        coordIA
                );

                System.out.println("IA disparando en: " + coordIA);

                EventMessage message = new EventMessage("RealizarDisparo", gson.toJson(aiRequest));
                publisher.publish(RedisConfig.CHANNEL_COMANDOS, message);
            }
        }, 2000); // 2 segundos de retraso
    }

    private CoordenadaDTO generarDisparo(Tablero tableroEnemigo) {
        int fila, col;
        do {
            fila = random.nextInt(Tablero.FILAS);
            col = random.nextInt(Tablero.COLUMNAS);
        } while (tableroEnemigo.getCelda(fila, col).getEstado() == EstadoCelda.DISPARADA);

        return new CoordenadaDTO(fila, col);
    }
}
