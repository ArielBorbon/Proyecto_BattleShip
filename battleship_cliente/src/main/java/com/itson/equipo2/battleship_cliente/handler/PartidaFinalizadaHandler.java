/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import javax.swing.JOptionPane;
import mx.itson.equipo_2.common.dto.response.PartidaFinalizadaResponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author Alberto Jimenez
 */
public class PartidaFinalizadaHandler implements IMessageHandler{
    
    private final ViewController viewController;
    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public PartidaFinalizadaHandler(ViewController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaFinalizada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        PartidaFinalizadaResponse response = gson.fromJson(message.getPayload(), PartidaFinalizadaResponse.class);
        
        // 1. Actualizar modelo (para que deje de contar tiempo, etc.)
        partidaModel.setEstado(EstadoPartida.FINALIZADA);
        partidaModel.setTurnoDe(response.getGanadorId());

        // 2. EJECUTAR LA LÓGICA DE UI (Lo que antes hacía la Vista)
        javax.swing.SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, 
                "¡Juego Terminado!\nGanador: " + response.getGanadorId() + 
                "\nMotivo: " + response.getMotivo());
            
            // Navegar al menú
            viewController.cambiarPantalla("menu");
        });
    }
}
