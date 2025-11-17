/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
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

    
    public void registrar(String nombre, ColorJugador color) {
        
        if (partidaModel.getYo() == null) {
             System.err.println("Error: partidaModel.getYo() es null en RegistroController.");
             return;
        }

        
        String idLocal = UUID.randomUUID().toString();

       
        partidaModel.getYo().setId(idLocal);
        partidaModel.getYo().setNombre(nombre);
        partidaModel.getYo().setColor(color);
        
     
        if (partidaModel.getYo().getTablero() != null) {
            partidaModel.getYo().getTablero().setIdJugaodr(idLocal);
        }

        System.out.println("--- Jugador registrado localmente (Flujo Local) ---");
        System.out.println("  ID: " + partidaModel.getYo().getId());
        System.out.println("  Nombre: " + partidaModel.getYo().getNombre());
        System.out.println("  Color: " + partidaModel.getYo().getColor());
        
            }
}
