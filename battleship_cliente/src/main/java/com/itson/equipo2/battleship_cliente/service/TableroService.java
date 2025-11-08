/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
import java.util.UUID;
import mx.itson.equipo_2.common.broker.IMessagePublisher;
import mx.itson.equipo_2.common.dto.CeldaDTO;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.request.PosicionarNaveRequest;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;
import mx.itson.equipo_2.common.enums.TipoNave;
import mx.itson.equipo_2.common.message.EventMessage;

/**
 *
 * @author skyro
 */
public class TableroService {
   private final IMessagePublisher publisher;
    private final Gson gson = new Gson();

    public TableroService(IMessagePublisher publisher) {
        this.publisher = publisher;
    }

    
    public void disparar(UUID partidaId, UUID jugadorId, int columna, int fila) {
        
       
        CoordenadaDTO coordenada = new CoordenadaDTO(columna, fila);
        
 
        RealizarDisparoRequest req = new RealizarDisparoRequest(partidaId, jugadorId, coordenada);
        
       
        String payload = gson.toJson(req);
        
 
        EventMessage message = new EventMessage("RealizarDisparo", payload);
        
        
        publisher.publish("battleship:comandos", message);
    }

    public void posicionarNave(CoordenadaDTO[] coordenadas, TipoNave tipoNave) {
        PosicionarNaveRequest req = new PosicionarNaveRequest(coordenadas, tipoNave);
        String payload = gson.toJson(req);

        EventMessage message = new EventMessage("PosicionarNave", payload);
        
        // (Asegúrate de publicar este también)
        // publisher.publish("battleship:comandos", message);
    }
}
