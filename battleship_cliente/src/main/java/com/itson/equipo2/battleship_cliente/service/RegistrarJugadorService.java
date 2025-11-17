/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import com.itson.equipo2.communication.broker.IMessagePublisher;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.broker.BrokerConfig;
import mx.itson.equipo_2.common.dto.request.RegistrarJugadorRequest;

/**
 *
 * @author Cricri
 */
public class RegistrarJugadorService {

    private final IMessagePublisher publisher;
    private final Gson gson = new Gson();

    public RegistrarJugadorService(IMessagePublisher publisher) {
        this.publisher = publisher;
    }


    public void registrar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return;
        }

        RegistrarJugadorRequest req = new RegistrarJugadorRequest(nombre);
        String payload = gson.toJson(req);
        EventMessage message = new EventMessage("RegistrarJugador", payload);

        System.out.println("Cliente: Enviando comando 'RegistrarJugador' con nombre: " + nombre);
        publisher.publish(BrokerConfig.CHANNEL_COMANDOS, message);
    }
      }