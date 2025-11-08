package com.itson.equipo2.battleship_cliente.pattern.strategy;

import java.util.Random;
import javax.swing.Timer;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import com.itson.equipo2.battleship_cliente.controllers.DisparoController;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;

public class StrategyTurnoIA implements StrategyTurno {

    private final JugadorModel jugadorAI;
    private final Random random = new Random();

    public StrategyTurnoIA(JugadorModel jugadorAI) {
        this.jugadorAI = jugadorAI;
    }

    @Override
    public void ejecutarTurno(PartidaModel partidaModel, DisparoController partidaController) {
        System.out.println("IA está 'pensando' su movimiento...");

        int segundosDeEspera = random.nextInt(4) + 2; 
        Timer timer = new Timer(segundosDeEspera * 1000, e -> {

            if (!partidaModel.isEnCurso()) {
                return;
            }

            CoordenadaDTO coordDisparoIA;
            do {
                int fila = random.nextInt(10);
                int col = random.nextInt(10);
                coordDisparoIA = new CoordenadaDTO(fila, col);
            } while (haSidoDisparadaPorIA(coordDisparoIA));

            System.out.println("IA dispara en: (" + coordDisparoIA.getFila() + ", " + coordDisparoIA.getColumna() + ")");
            
//            partidaController.disparar(jugadorAI, coordDisparoIA);
        });

        timer.setRepeats(false);
        timer.start();
    }

    private boolean haSidoDisparadaPorIA(CoordenadaDTO c) {
        
        return this.jugadorAI.getDisparos().stream()
                .anyMatch(disparo -> 
                        disparo.getFila() == c.getFila() &&
                                disparo.getColumna() == c.getColumna() );
    }
}
