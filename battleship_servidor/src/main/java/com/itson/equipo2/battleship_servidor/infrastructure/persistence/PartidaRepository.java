
package com.itson.equipo2.battleship_servidor.infrastructure.persistence;

import com.itson.equipo2.battleship_servidor.domain.model.Partida;
import com.itson.equipo2.battleship_servidor.domain.repository.IPartidaRepository;

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
    
    @Override
    public void eliminarPartida() {
        this.partida = null;
        System.out.println("Repositorio: La partida ha sido eliminada.");
    }
    
    
}
