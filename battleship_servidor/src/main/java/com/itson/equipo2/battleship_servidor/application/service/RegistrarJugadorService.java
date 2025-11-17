/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application.service;


import com.google.gson.Gson;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import java.util.UUID;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;

public class RegistrarJugadorService {

    private final IMessagePublisher eventPublisher;
    private final Gson gson;

    public RegistrarJugadorService(IMessagePublisher eventPublisher, Gson gson) {
        this.eventPublisher = eventPublisher;
        this.gson = gson;
    }

    public void execute(RegistrarJugadorRequest request) {
        try {

            String nuevoId = UUID.randomUUID().toString();

           
            JugadorDTO jugadorDto = new JugadorDTO();
            jugadorDto.setId(nuevoId);
            jugadorDto.setNombre(request.getNombre());
            jugadorDto.setColor(null); 

            
            System.out.println("Jugador registrado: " + jugadorDto.getNombre() + " (ID: " + jugadorDto.getId() + ")");
            EventMessage evento = new EventMessage("JugadorRegistrado", gson.toJson(jugadorDto));
            eventPublisher.publish(BrokerConfig.CHANNEL_EVENTOS, evento);

        } catch (Exception e) {
            System.err.println("Error al registrar jugador: " + e.getMessage());
        }
    }
}