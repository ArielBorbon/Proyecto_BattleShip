/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 *
 * @author Cricri
 */
public class RegistroController {
    private final RegistrarJugadorService registrarJugadorService;
    private final PartidaModel partidaModel; 

    public RegistroController(RegistrarJugadorService registrarJugadorService, PartidaModel partidaModel) {
        this.registrarJugadorService = registrarJugadorService;
        this.partidaModel = partidaModel;
    }

    
    public void registrar(String nombre, ColorJugador color) {
        
    
        if (partidaModel.getYo() != null) {
            partidaModel.getYo().setColor(color);
            System.out.println("Color local del jugador establecido en: " + color);
        } else {
             
            System.err.println("Error: partidaModel.getYo() es null en RegistroController.");
        }
        
       
        registrarJugadorService.registrar(nombre);
    }
}
