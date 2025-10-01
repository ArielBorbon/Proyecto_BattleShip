package mx.itson.equipo_2.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.enums.EstadoCelda;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.Strategy.StrategyTurno;
import mx.itson.equipo_2.patterns.mediator.GameMediator;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class PartidaController {

    private final PartidaModel partidaModel;
    private final Map<Jugador, StrategyTurno> estrategias;
    private Timer turnoTimer;

    public PartidaController(PartidaModel partidaModel, GameMediator mediator) {
        this.partidaModel = partidaModel;
        this.estrategias = new HashMap<>();
        mediator.setController(this);
    }

    public void registrarEstrategia(Jugador jugador, StrategyTurno estrategia) {
        estrategias.put(jugador, estrategia);
    }

    public void iniciarPartida() {
        gestionarSiguienteTurno();
    }

    private void gestionarSiguienteTurno() {
        if (partidaModel.partidaFinalizada()) {
            System.out.println("La partida ha finalizado.");
            return;
        }

        if (turnoTimer != null && turnoTimer.isRunning()) {
            turnoTimer.stop();
        }

        partidaModel.iniciarTurno();

        turnoTimer = new Timer(1000, e -> {
            partidaModel.decrementarTiempo();

            if (partidaModel.getTiempoRestante() <= 0) {
                turnoTimer.stop();
                System.out.println("TIMEOUT: El controlador detectó tiempo agotado.");

                partidaModel.pasarTurno();
                gestionarSiguienteTurno();
            }
        });
        turnoTimer.start();

        Jugador jugadorEnTurno = partidaModel.getJugadorEnTurno();
        StrategyTurno estrategiaActual = estrategias.get(jugadorEnTurno);
        if (estrategiaActual != null) {
            estrategiaActual.ejecutarTurno(partidaModel, this);
        }
    }

   public void solicitarDisparo(Jugador jugador, Coordenada coordenada) {
    if (turnoTimer != null) {
        turnoTimer.stop();
    }

    try {
        ResultadoDisparo resultado = partidaModel.realizarDisparo(jugador, coordenada);

        if (partidaModel.partidaFinalizada()) {
            partidaModel.notifyObservers();
            return;
        }

        if (resultado == ResultadoDisparo.AGUA) {
            partidaModel.pasarTurno();
        } else {
            partidaModel.repetirTurno();
        }

        gestionarSiguienteTurno();

    } catch (IllegalStateException | IllegalArgumentException e) {
        partidaModel.setUltimoError(e.getMessage());
        turnoTimer.start();
    }
}


}
