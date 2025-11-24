/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.communication.impl.NetworkService;

/**
 *
 * @author Alberto Jimenez
 */
public class ConfiguracionController {
    
    private final NetworkService networkService;

    public ConfiguracionController(NetworkService networkService) {
        this.networkService = networkService;
    }

    public boolean intentarConexion(String ip) {
        try {
            networkService.conectarAServidor(ip);
            return true;
        } catch (Exception e) {
            System.err.println("Fallo al conectar con " + ip + ": " + e.getMessage());
            return false;
        }
    }
    
}
