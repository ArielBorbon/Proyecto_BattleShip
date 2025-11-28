/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.communication.service.NetworkService;

/**
 *
 * @author Alberto Jimenez
 */
public class ConfiguracionController {
    
    private final NetworkService networkService;
    private final PartidaModel partidaModel;

    public ConfiguracionController(NetworkService networkService, PartidaModel partidaModel) {
        this.networkService = networkService;
        this.partidaModel = partidaModel;
    }

    public boolean intentarConexion(String ip) {
        try {
            networkService.conectarAServidor(ip);
            
            partidaModel.setIpServidor(ip); 
            
            return true;
        } catch (Exception e) {
            System.err.println("Fallo al conectar con " + ip + ": " + e.getMessage());
            return false;
        }
    }
    
}
