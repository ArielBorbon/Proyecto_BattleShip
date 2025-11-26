/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import java.util.UUID;
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
        System.out.println("RegistroLocal: Guardando datos...");

        JugadorModel yo = partidaModel.getYo();
        if (yo == null) {
            yo = new JugadorModel();
            partidaModel.setYo(yo);
        }
        yo.setId(UUID.randomUUID().toString());
        yo.setNombre(nombre);
        yo.setColor(color);

        
    }

 

}
