/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.infrastructure.persistence;

import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Cricri
 */
public class InMemoryPartidaRepository implements IPartidaRepository {
    
    private final Map<UUID, Partida> almacenDePartidas;

    public InMemoryPartidaRepository() {
  
        this.almacenDePartidas = new java.util.concurrent.ConcurrentHashMap<>();
    }

    @Override
    public Partida buscarPorId(UUID id) {
        System.out.println("Buscando partida: " + id);
        return almacenDePartidas.get(id);
    }


    @Override
    public void guardar(Partida partida) {
        if (partida == null) {
            return;
        }
        System.out.println("Guardando partida: " + partida.getId());
        almacenDePartidas.put(partida.getId(), partida);
    }

    
    @Override
    public void eliminar(UUID id) {
        System.out.println("Eliminando partida: " + id);
        almacenDePartidas.remove(id);
    }
}
