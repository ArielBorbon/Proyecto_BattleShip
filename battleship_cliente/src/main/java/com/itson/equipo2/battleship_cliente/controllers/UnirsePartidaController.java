/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import mx.itson.equipo_2.common.enums.AccionPartida;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 *
 * @author Alberto Jimenez
 */
public class UnirsePartidaController {
    
    private final RegistrarJugadorService service;
    private final PartidaModel partidaModel;

    public UnirsePartidaController(RegistrarJugadorService service, PartidaModel partidaModel) {
        this.service = service;
        this.partidaModel = partidaModel;
    }

    public void solicitarAcceso(AccionPartida accion) {
        
        partidaModel.reiniciarPartida();
        
        String nombre = partidaModel.getYo().getNombre();
        String id = partidaModel.getYo().getId();
        ColorJugador color = partidaModel.getYo().getColor();

        if (nombre == null) {
            System.err.println("Error: Faltan datos locales.");
            return;
        }
        service.registrar(nombre, color, accion, id);
    }
    
}
