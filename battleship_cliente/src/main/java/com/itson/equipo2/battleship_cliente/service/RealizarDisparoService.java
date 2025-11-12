/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.communication.RedisConfig;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class RealizarDisparoService {

    private final IMessagePublisher publisher;
    private final JugadorModel jugadorModel;
    private final Gson gson = new Gson();

    public RealizarDisparoService(IMessagePublisher publisher, JugadorModel jugadorModel) {
        this.publisher = publisher;
        this.jugadorModel = jugadorModel;
    }

    public void disparar(int columna, int fila) {

        CoordenadaDTO coordenada = new CoordenadaDTO(fila, columna);

        RealizarDisparoRequest req = new RealizarDisparoRequest(jugadorModel.getId(), coordenada);

        String payload = gson.toJson(req);

        EventMessage message = new EventMessage("RealizarDisparo", payload);

        publisher.publish(RedisConfig.CHANNEL_COMANDOS, message);
    }

    
}
