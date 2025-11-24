/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import javax.swing.JOptionPane;

/**
 *
 * @author sonic
 */
public class PosicionamientoHandler implements IMessageHandler {

    private final ViewController viewController;

    public PosicionamientoHandler(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "InicioPosicionamiento".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Cliente (Handler): ¡Todos listos! Iniciando fase de posicionamiento.");

        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("posicionar");
        });
    }
}
