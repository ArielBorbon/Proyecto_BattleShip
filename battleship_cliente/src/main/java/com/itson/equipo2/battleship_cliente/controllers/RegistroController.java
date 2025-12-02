/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 *
 * @author Cricri
 */
public class RegistroController {

    private final PartidaModel partidaModel;

    public RegistroController(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    public void guardarDatosJugador(String nombre, ColorJugador color) {
        partidaModel.registrarJugador(nombre, color);
        
    }

 

}
