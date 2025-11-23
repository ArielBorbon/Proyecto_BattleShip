/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class PartidaCanceladaHandler implements IMessageHandler {

    private final ViewController viewController;
    private final PartidaModel partidaModel;

    public PartidaCanceladaHandler(ViewController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaCancelada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Cliente: La sala fue cancelada por el host.");

        partidaModel.reiniciarPartida();

        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("lobby");
        });
    }
}
