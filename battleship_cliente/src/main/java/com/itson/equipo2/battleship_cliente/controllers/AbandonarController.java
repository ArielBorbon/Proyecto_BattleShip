/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.service.AbandonarPartidaService;

/**
 *
 * @author Alberto Jimenez
 */
public class AbandonarController {
    
    private final AbandonarPartidaService service;

    public AbandonarController(AbandonarPartidaService service) {
        this.service = service;
    }
    
    public void abandonar() {
        service.abandonar();
    }
    
}
