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

        // 1. Deserializar el mensaje
        PartidaIniciadaResponse response = gson.fromJson(message.getPayload(), PartidaIniciadaResponse.class);

        // 2. DELEGAR LÓGICA DE NEGOCIO AL MODELO
        //    El modelo se actualizará y notificará a las vistas (Observer)
        partidaModel.iniciarPartida(response);

        // 3. LA LÓGICA DE VISTA SE QUEDA EN EL HANDLER (CONTROLADOR)
        //    Esto es correcto, el handler decide cuándo cambiar la pantalla.
        System.out.println("PartidaModel actualizado. cambiando a vista 'disparar'");
        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("disparar");
        });
    }
}
