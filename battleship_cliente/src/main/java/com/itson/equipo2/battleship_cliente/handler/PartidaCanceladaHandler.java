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
 * @author PC Gamer
 */
public class PartidaCanceladaHandler implements IMessageHandler {

    private final VistaController viewController;
    private final PartidaModel partidaModel;

    public PartidaCanceladaHandler(VistaController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaCancelada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("La partida ha sido cancelada por el servidor.");
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, 
                "La partida ha sido cancelada (el rival se desconecto o abandono).", 
                "Partida Terminada", 
                JOptionPane.INFORMATION_MESSAGE);
            
            partidaModel.reiniciarPartida();
            viewController.cambiarPantalla("menu");
        });
    }
}
