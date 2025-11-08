/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.util.UUID;

/**
 *
 * @author Cricri
 */
public class Jugador {
    
    private final UUID id;
    
   
    private String nombre;

    
    public Jugador(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

  
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
