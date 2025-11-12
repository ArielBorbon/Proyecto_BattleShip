/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.communication.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.utils.AppContext;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class DisparoRealizadoHandler implements IMessageHandler {

    // Ya no se inyectan en el constructor, se obtienen de AppContext
    // private final PartidaModel partidaModel; 
    private final Gson gson = new Gson();

    public DisparoRealizadoHandler() {
        // Constructor vacío
    }
    
    // Constructor anterior (ya no es necesario si se usa AppContext)
    // public DisparoRealizadoHandler(PartidaModel partidaModel) {
    //    this.partidaModel = partidaModel;
    // }
    
    @Override
    public boolean canHandle(EventMessage message) {
        return "DisparoRealizado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Handler 'DisparoRealizado' activado.");
        
        // Obtener los modelos desde AppContext
        PartidaModel partidaModel = AppContext.getPartidaModel();
        TableroModel tableroPropio = AppContext.getTableroPropio();
        TableroModel tableroEnemigo = AppContext.getTableroEnemigo();

        ResultadoDisparoReponse response = gson.fromJson(message.getPayload(), ResultadoDisparoReponse.class);

        TableroModel tableroAfectado;

        // 1. Determinar qué tablero actualizar
        if (response.getJugadorId().equals(partidaModel.getYo().getId())) {
            // Fui yo quien disparó -> actualizar tablero enemigo
            System.out.println("Actualizando tablero ENEMIGO.");
            tableroAfectado = tableroEnemigo;
        } else {
            // Disparó el enemigo -> actualizar mi tablero
            System.out.println("Actualizando tablero PROPIO.");
            tableroAfectado = tableroPropio;
        }

        // 2. Actualizar la celda en el modelo de tablero
        if (tableroAfectado != null) {
            // Este método (actualizarCelda) debe notificar a sus propios observadores (la Vista)
            tableroAfectado.actualizarCelda(response.getCoordenada(), response.getResultado(), response.getCoordenadasBarcoHundido());
        } else {
            System.err.println("Error: Tablero afectado es nulo.");
        }
        
        // 3. Actualizar el estado de la partida (TURNO y ESTADO)
        partidaModel.setTurnoDe(response.getTurnoActual());
        partidaModel.setEstado(response.getEstadoPartida());
        
        // 4. Notificar a los observadores (DispararView)
        // ¡Esto ahora funciona porque PartidaModel tiene el método! (Problema 4 resuelto)
        partidaModel.notifyObservers();
    }
}