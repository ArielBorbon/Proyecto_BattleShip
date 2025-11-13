/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class PartidaIniciadaHandler implements IMessageHandler {

    private final ViewController viewController;
    private final PartidaModel partidaModel;
    private final TableroModel tableroPropio;
    private final TableroModel tableroEnemigo;
    private final Gson gson = new Gson();

    public PartidaIniciadaHandler(ViewController viewController, PartidaModel partidaModel, TableroModel tableroPropio, TableroModel tableroEnemigo) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
        this.tableroPropio = tableroPropio;
        this.tableroEnemigo = tableroEnemigo;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaIniciada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Evento PartidaIniciada recibido!");
        PartidaIniciadaResponse response = gson.fromJson(message.getPayload(), PartidaIniciadaResponse.class);

        partidaModel.setId(response.getPartidaId());
        
        // buscamos y el yo y el enemigo
        JugadorDTO j1 = response.getJugador1();
        JugadorDTO j2 = response.getJugador2();
        JugadorModel yo = partidaModel.getYo(); // Ya tiene el ID y nombre
        
        if (yo.getId().equals(j1.getId())) {
            partidaModel.setEnemigo(new JugadorModel(j2.getId(), j2.getNombre(), true, tableroEnemigo, null));
        } else {
            partidaModel.setEnemigo(new JugadorModel(j1.getId(), j1.getNombre(), true, tableroEnemigo, null));
        }

        partidaModel.setTurnoDe(response.getTurnoActual());
        partidaModel.setEstado(response.getEstado());
        partidaModel.getYo().setTablero(tableroPropio); // Asignar el modelo de tablero
        
        System.out.println("PartidaModel actualizado. cambiando a vista 'disparar'");
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("disparar");
        });
    }
}