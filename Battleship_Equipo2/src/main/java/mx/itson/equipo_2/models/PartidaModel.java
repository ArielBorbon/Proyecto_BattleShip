/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Partida;

/**
 *
 * @author skyro
 */
public class PartidaModel {
    
    private Partida partida;
    
    private JugadorModel jugador1;
    private JugadorModel jugador2;
    
    public Boolean verificarTurno(Jugador j) {
        return partida.getJugadorEnTurno() == j;
    }
    
    public Boolean cambiarTurno(Jugador j) {
        partida.setJugadorEnTurno(j);
        return true;
    }
}
