/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.service;

import com.google.gson.Gson;
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
    
    // Métodos
    
    public void disparar(int columna, int fila) {
        RealizarDisparoRequest req = new RealizarDisparoRequest(columna, fila);
        String payload = gson.toJson(req);
        
        EventMessage message = new EventMessage("realizar_disparo", payload);
        publisher.publish("battleship", message);
    }
    
    public void posicionarNave(CoordenadaDTO[] coordenadas, TipoNave tipoNave) {
        PosicionarNaveRequest req = new PosicionarNaveRequest(coordenadas, tipoNave);
        String payload = gson.toJson(req);
        
        EventMessage message = new EventMessage("posicionar_nave", payload);
    }
}
