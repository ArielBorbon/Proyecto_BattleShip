/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.service;

import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.PartidaRepository;
import mx.itson.equipo_2.common.dto.request.RealizarDisparoRequest;

/**
 *
 * @author skyro
 */
public class RealizarDisparoService {
    
    private final PartidaRepository repo;

    public RealizarDisparoService(PartidaRepository repo) {
        this.repo = repo;
    }
    
    public void realizarDisparo(RealizarDisparoRequest req) {
        Partida partida = repo.getPartida();
        partida.realizarDisparo(req.getJugadorId(), req.getCoordenada());
        repo.guardar(partida);
    }
    
}
