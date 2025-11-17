/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.handler;

import com.google.gson.Gson;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.broker.IMessageHandler;
import com.itson.equipo2.communication.dto.EventMessage;
import mx.itson.equipo_2.common.dto.JugadorDTO;

public class JugadorRegistradoHandler implements IMessageHandler {

    private final ViewController viewController;
    private final PartidaModel partidaModel;
    private final Gson gson = new Gson();

    public JugadorRegistradoHandler(ViewController viewController, PartidaModel partidaModel) {
        this.viewController = viewController;
        this.partidaModel = partidaModel;
    }

    @Override
    public boolean canHandle(EventMessage message) {
        return "JugadorRegistrado".equals(message.getEventType());
    }

    @Override
    public void onMessage(EventMessage message) {
        System.out.println("Cliente: Recibido evento 'JugadorRegistrado'!");

        
        JugadorDTO jugadorInfo = gson.fromJson(message.getPayload(), JugadorDTO.class);

        
        JugadorModel miModelo = partidaModel.getYo();
        
        if (miModelo == null) {
             System.err.println("Error: partidaModel.getYo() es null. Asegúrate de inicializarlo en el main.");
             miModelo = new JugadorModel();
             partidaModel.setYo(miModelo);
        }

 
        miModelo.setId(jugadorInfo.getId());
        miModelo.setNombre(jugadorInfo.getNombre());
        
        
        if (miModelo.getTablero() != null) {
            miModelo.getTablero().setIdJugaodr(jugadorInfo.getId());
        } else {
            System.err.println("Advertencia: El JugadorModel no tenía un TableroModel inicializado.");
        }

        System.out.println("Cliente: ¡Registrado! Mi nombre es " + miModelo.getNombre() + " y mi ID es " + miModelo.getId());
        System.out.println("Cliente: Mi color elegido localmente es: " + miModelo.getColor());

        
        javax.swing.SwingUtilities.invokeLater(() -> {
            viewController.cambiarPantalla("lobby");
        });
    }
}