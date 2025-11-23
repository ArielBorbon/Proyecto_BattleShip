/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.service.RegistrarJugadorService;
import com.itson.equipo2.communication.impl.RedisConnection;
import java.util.UUID;
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
        System.out.println("Limpiando estado de partida anterior...");
        partidaModel.reiniciarPartida();

        String miNuevoId = UUID.randomUUID().toString();

        if (partidaModel.getYo() == null) {
            partidaModel.setYo(new JugadorModel());
        }
        partidaModel.getYo().setId(miNuevoId);
        partidaModel.getYo().setNombre(nombre);

        ColorJugador colorElegido = partidaModel.getYo().getColor();

        service.registrar(nombre, colorElegido, accion, miNuevoId);
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
        service.configurarRed(ipHost);
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
