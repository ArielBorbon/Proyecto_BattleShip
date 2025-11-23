/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import com.itson.equipo2.communication.impl.RedisConnection;
import mx.itson.equipo_2.common.enums.AccionPartida;
import mx.itson.equipo_2.common.enums.ColorJugador;

/**
 *
 * @author Cricri
 */
public class RegistroController {

    private final PartidaModel partidaModel;
    private final RegistrarJugadorService service;
    private AccionPartida accionActual;

    public RegistroController(PartidaModel partidaModel, RegistrarJugadorService service) {
        this.partidaModel = partidaModel;
        this.service = service;
    }

    public void registrar(String nombre) {
        this.registrar(nombre, this.accionActual);
    }

    public void registrar(String nombre, AccionPartida accion) {
        ColorJugador colorElegido = partidaModel.getYo().getColor();
        
        service.registrar(nombre, colorElegido, accion);
    }

    public void guardarDatosJugador(String nombre, ColorJugador color) {
        JugadorModel yo = partidaModel.getYo();
        if (yo == null) {
            yo = new JugadorModel();
            partidaModel.setYo(yo);
        }
        yo.setNombre(nombre);
        yo.setColor(color);
        
        partidaModel.notifyObservers();
    }

    public void configurarConexion(String ipHost) {
        if (ipHost != null && !ipHost.trim().isEmpty()) {

            RedisConnection.setHost(ipHost);

            try {
                RedisConnection.getJedisPool();
                System.out.println("RegistroController: Conexión reconfigurada a " + ipHost);
            } catch (Exception e) {
                System.err.println("RegistroController: Error al intentar conectar con " + ipHost + ": " + e.getMessage());
            }
        }
    }

    public PartidaModel getPartidaModel() {
        return partidaModel;
    }

    public RegistrarJugadorService getService() {
        return service;
    }

    public AccionPartida getAccionActual() {
        return accionActual;
    }

    public void setAccionActual(AccionPartida accionActual) {
        this.accionActual = accionActual;
    }

}
