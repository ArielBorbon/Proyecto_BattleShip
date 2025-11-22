/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.service.SalaService;

/**
 *
 * @author PC Gamer
 */
public class SalaController {

    private final SalaService salaService;
    private final ViewController viewController;

    public SalaController(SalaService salaService, ViewController viewController) {
        this.salaService = salaService;
        this.viewController = viewController;
    }

    public void iniciarJuego() {
        salaService.enviarInicioJuego();
    }

    public void volverAlMenu() {
        viewController.cambiarPantalla("menu");
    }

    public void volverAlLobby() {
        viewController.cambiarPantalla("lobby");
    }
}
