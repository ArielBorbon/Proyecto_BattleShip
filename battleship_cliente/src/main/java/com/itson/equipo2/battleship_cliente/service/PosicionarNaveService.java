/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.PosicionarNaveRequest;
import mx.itson.equipo_2.common.enums.TipoNave;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class PosicionarNaveService {
 
    private final IMessagePublisher publisher;
    private final JugadorModel jugadorModel;
    private final Gson gson = new Gson();

    public PosicionarNaveService(IMessagePublisher publisher, JugadorModel jugadorModel) {
        this.publisher = publisher;
        this.jugadorModel = jugadorModel;
    }

    
    public void posicionarNave(CoordenadaDTO[] coordenadas, TipoNave tipoNave) {
        PosicionarNaveRequest req = new PosicionarNaveRequest(jugadorModel.getId(), coordenadas, tipoNave);
        String payload = gson.toJson(req);

        EventMessage message = new EventMessage("PosicionarNave", payload);

        publisher.publish("battleship:comandos", message);
    }
}
