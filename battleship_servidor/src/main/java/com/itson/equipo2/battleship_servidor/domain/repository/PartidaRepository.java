/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.repository;

import com.itson.equipo2.battleship_servidor.domain.model.Partida;

/**
 *
 * @author skyro
 */
public class PartidaRepository implements IPartidaRepository {
    
    private Partida partida;

    public PartidaRepository(Partida partida) {
        this.partida = partida;
    }
    

    @Override
    public Partida getPartida() {
        return partida;
    }

    @Override
    public void guardar(Partida partida) {
        this.partida = partida;
    }
}
