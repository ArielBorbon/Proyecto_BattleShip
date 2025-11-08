package com.itson.equipo2.battleship_cliente.controllers;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.pattern.strategy.StrategyTurno;
import com.itson.equipo2.battleship_cliente.service.TableroService;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import mx.itson.equipo_2.common.dto.JugadorDTO;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class DisparoController {

    private final TableroService tableroService;
    private final Map<JugadorDTO, StrategyTurno> estrategias;

    public DisparoController(TableroService tableroService) {
        this.tableroService = tableroService;
        this.estrategias = new HashMap<>();
    }

    public void registrarEstrategia(JugadorDTO jugador, StrategyTurno estrategia) {
        estrategias.put(jugador, estrategia);
    }

    public void disparar(int columna, int fila) {
        tableroService.disparar(columna, fila);
        System.out.println("3");
    }
//    public void iniciarPartida() {
//        gestionarSiguienteTurno();
//    }

//    private void gestionarSiguienteTurno() {
//        if (partidaModel.partidaFinalizada()) {
//            System.out.println("La partida ha finalizado.");
//            return;
//        }
//
//        if (turnoTimer != null && turnoTimer.isRunning()) {
//            turnoTimer.stop();
//        }
//
//        partidaModel.iniciarTurno();
//
//        turnoTimer = new Timer(1000, e -> {
//            partidaModel.decrementarTiempo();
//
//            if (partidaModel.getTiempoRestante() <= 0) {
//                turnoTimer.stop();
//                System.out.println("TIMEOUT: El controlador detectó tiempo agotado.");
//
//                partidaModel.pasarTurno();
//                gestionarSiguienteTurno();
//            }
//        });
//        turnoTimer.start();
//
//        JugadorModel jugadorEnTurno = partidaModel.getJugadorEnTurno();
//        StrategyTurno estrategiaActual = estrategias.get(jugadorEnTurno);
//        if (estrategiaActual != null) {
//            estrategiaActual.ejecutarTurno(partidaModel, this);
//        }
//    }

//    public void disparar(JugadorModel jugador, CoordenadaDTO coordenada) {
//        
//        if (turnoTimer != null) {
//            turnoTimer.stop();
//        }
//
//        try {
//            ResultadoDisparo resultado = partidaModel.realizarDisparo(jugador, CoordenadaMapper.toEntity(coordenada));
//
//            // Si ya se hundieron todas las naves
//            if (partidaModel.partidaFinalizada()) {
//                partidaModel.notifyObservers();
//                return;
//            }
//
//            // Si el disparo va a una celda ya disparada            
//            if (jugador.getTablero().getCelda(coordenada.getFila(), coordenada.getColumna()).getEstado() == EstadoCelda.DISPARADA) {
//                turnoTimer.start();
//                return;
//            }
//
//            // Resultados
//            if (resultado == ResultadoDisparo.AGUA) {
//                partidaModel.pasarTurno();
//            } else {
//                partidaModel.repetirTurno(); 
//            }
//
//            gestionarSiguienteTurno();
//
//        } catch (IllegalStateException | IllegalArgumentException e) {
//            partidaModel.setUltimoError(e.getMessage());
//            turnoTimer.start();
//        }
//    }

}
