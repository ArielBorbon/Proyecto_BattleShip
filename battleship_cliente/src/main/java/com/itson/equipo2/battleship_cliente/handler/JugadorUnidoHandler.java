package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.JugadorDTO;

public class JugadorUnidoHandler implements IMessageHandler {

    private final VistaController viewController;
    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public JugadorUnidoHandler(VistaController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "JugadorRegistrado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        JugadorDTO jugadorInfo = gson.fromJson(message.getPayload(), JugadorDTO.class);
        JugadorModel miModelo = partidaModel.getYo();

        if (miModelo == null || miModelo.getId() == null) {
            return; 
        }

        if (!miModelo.getId().equals(jugadorInfo.getId())) {
            System.out.println("Cliente: Ignorando evento de registro ajeno (ID no coincide).");
            return;
        }

        System.out.println("Cliente: ¡Confirmación de registro recibida para MI!");

        miModelo.setColor(jugadorInfo.getColor());

        if (miModelo.getTablero() != null) {
            miModelo.getTablero().setIdJugaodr(miModelo.getId());
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("salaPartida");
        });
    }
}
