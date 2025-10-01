/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.patterns.mediator;

import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author PC Gamer
 */
public class GameMediator {

    private PartidaController controller;

    // El Main usará este método para registrar el controlador con el mediador
    public void setController(PartidaController controller) {
        this.controller = controller;
    }

    /**
     * La Vista llama a este método cuando el usuario quiere disparar. El
     * Mediador reenvía la solicitud al Controlador.
     */
    public void notificarDisparo(Jugador jugador, Coordenada coordenada) {
        if (controller != null) {
            controller.solicitarDisparo(jugador, coordenada);
        }
    }
}
