package mx.itson.equipo_2.patterns.Strategy;


import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.models.PartidaModel;

/**
 *
 * @author PC Gamer
 */
public interface StrategyTurno {

    void ejecutarTurno(PartidaModel partidaModel, PartidaController partidaController);
}
