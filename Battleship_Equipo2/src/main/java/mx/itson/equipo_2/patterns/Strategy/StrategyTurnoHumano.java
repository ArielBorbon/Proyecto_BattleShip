/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.patterns.Strategy;

import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.models.PartidaModel;

/**
 *
 * @author PC Gamer
 */
public class StrategyTurnoHumano implements StrategyTurno {

    @Override
    public void ejecutarTurno(PartidaModel partidaModel, PartidaController partidaController) {
        // Para un jugador humano, la estrategia es simplemente esperar la entrada de la UI.
        // El controlador se encargará de habilitar los controles necesarios.
        System.out.println("Turno del jugador humano. Esperando acción...");
    }
}
