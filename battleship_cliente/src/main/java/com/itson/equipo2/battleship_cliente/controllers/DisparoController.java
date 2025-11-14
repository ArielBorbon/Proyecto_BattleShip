package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.pattern.strategy.StrategyTurno;
import com.itson.equipo2.battleship_cliente.service.RealizarDisparoService;
import java.util.HashMap;
import java.util.Map;
import mx.itson.equipo_2.common.dto.JugadorDTO;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class DisparoController {

    private final RealizarDisparoService tableroService;
    private final Map<JugadorDTO, StrategyTurno> estrategias;

    public DisparoController(RealizarDisparoService tableroService) {
        this.tableroService = tableroService;
        this.estrategias = new HashMap<>();
    }

    public DisparoController(RealizarDisparoService tableroService, Map<JugadorDTO, StrategyTurno> estrategias) {
        this.tableroService = tableroService;
        this.estrategias = estrategias;
    }
    
    
    

    public void registrarEstrategia(JugadorDTO jugador, StrategyTurno estrategia) {
        estrategias.put(jugador, estrategia);
    }

    public void disparar(int columna, int fila) {
        tableroService.disparar(columna, fila);
        System.out.println("3");
    }

}
