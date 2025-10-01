package mx.itson.equipo_2.patterns.Strategy;


import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.models.PartidaModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC Gamer
 */
public interface StrategyTurno {
    /**
     * Define la lógica para ejecutar un turno.
     * @param partidaModel El estado actual de la partida.
     * @param partidaController El controlador para poder ejecutar acciones como disparar.
     */
    void ejecutarTurno(PartidaModel partidaModel, PartidaController partidaController);
}
