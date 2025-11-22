
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import com.itson.equipo2.communication.dto.EventMessage;

/**
 *
 * @author PC Gamer
 */
public class PartidaIniciadaHandler implements IMessageHandler {

    private final ViewController viewController;
    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public PartidaIniciadaHandler(ViewController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "PartidaIniciada".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Evento PartidaIniciada recibido!");

        PartidaIniciadaResponse response = gson.fromJson(message.getPayload(), PartidaIniciadaResponse.class);

        partidaModel.iniciarPartida(response);

        System.out.println("PartidaModel actualizado. cambiando a vista 'disparar'");
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("disparar");
        });
    }
}
