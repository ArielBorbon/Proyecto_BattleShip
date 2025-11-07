/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.application;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import mx.itson.equipo_2.common.broker.IMessageHandler;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author Cricri
 */
public class PartidaApplicationService implements IMessageHandler {
    private final IPartidaRepository partidaRepository;
    private final IMessagePublisher eventPublisher;
    private final Gson gson; 

    public PartidaApplicationService(IPartidaRepository partidaRepository, IMessagePublisher eventPublisher, Gson gson) {
        this.partidaRepository = partidaRepository;
        this.eventPublisher = eventPublisher;
        this.gson = gson;
    }

    @Override
    public void onMessage(EventMessage eventMessage) {
        try {
            String tipo = eventMessage.getEventType(); 
            
            if (tipo == null) {
                System.err.println("Comando inválido, no contiene 'type': " + eventMessage);
                return;
            }
            
            System.out.println("Procesando comando tipo: " + tipo);

            switch (tipo) {
                case "RegistrarJugador":
                    System.out.println("Lógica de 'RegistrarJugador' iría aquí.");
                    break;

                case "RealizarDisparo":
                    System.out.println("Lógica de 'RealizarDisparo' iría aquí.");
                    break;
                
                case "PosicionarNave":
                    System.out.println("Lógica de 'PosicionarNave' iría aquí.");
                    break;
                
                default:
                    System.err.println("Tipo de comando desconocido: " + tipo);
            }

        } catch (Exception e) {
            System.err.println("Error inesperado al manejar comando: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
