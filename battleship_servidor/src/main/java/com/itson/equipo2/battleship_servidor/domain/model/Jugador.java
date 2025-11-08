/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;



/**
 *
 * @author Cricri
 */
public class Jugador {
    
    private final String id;
    
   
    private String nombre;

    
    public Jugador(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

  
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
