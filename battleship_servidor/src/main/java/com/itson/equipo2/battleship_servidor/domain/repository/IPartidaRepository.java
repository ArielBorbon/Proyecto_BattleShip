/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.repository;

import com.itson.equipo2.battleship_servidor.domain.model.Partida;

/**
 *
 * @author Cricri
 */
public interface IPartidaRepository {

    Partida getPartida();

    void guardar(Partida partida);
}
