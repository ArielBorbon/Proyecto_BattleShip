/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import javax.swing.JOptionPane;

/**
 *
 * @author sonic
 */
public class PosicionamientoHandler implements IMessageHandler {

    private final VistaController viewController;

    public PosicionamientoHandler(VistaController viewController) {
        this.viewController = viewController;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "InicioPosicionamiento".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("posicionar");
        });
    }
}
