/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.patterns.Strategy;

import java.util.Random;
import javax.swing.Timer;
import mx.itson.equipo_2.controllers.PartidaController;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.mapper.CoordenadaMapper;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;

public class StrategyTurnoIA implements StrategyTurno {

    private final Jugador jugadorAI;
    private final Random random = new Random();

    public StrategyTurnoIA(Jugador jugadorAI) {
        this.jugadorAI = jugadorAI;
    }

    @Override
    public void ejecutarTurno(PartidaModel partidaModel, PartidaController partidaController) {
        System.out.println("IA está 'pensando' su movimiento...");

        // Simula un retraso para que el disparo no sea instantáneo
        int segundosDeEspera = random.nextInt(4) + 2; // Espera entre 2 y 5 segundos
        Timer timer = new Timer(segundosDeEspera * 1000, e -> {

            if (partidaModel.partidaFinalizada()) {
                return;
            }

            Coordenada coordDisparoIA;
            do {
                int fila = random.nextInt(10);
                int col = random.nextInt(10);
                coordDisparoIA = new Coordenada(fila, col);
            } while (haSidoDisparadaPorIA(partidaModel, coordDisparoIA));

            System.out.println("IA dispara en: (" + coordDisparoIA.getFila() + ", " + coordDisparoIA.getColumna() + ")");
            
            partidaController.solicitarDisparo(jugadorAI, coordDisparoIA);
        });

        timer.setRepeats(false); // El timer se ejecuta solo una vez
        timer.start();
    }

    private boolean haSidoDisparadaPorIA(PartidaModel model, Coordenada c) {
        // Obtenemos al oponente del jugador 1, que es la IA (jugador 2)
        
       // Jugador ia = model.getJugador2();
        //return ia.getDisparos().stream()
        //        .anyMatch(disparo -> disparo.getCoordenada().equals(c));
        
        return this.jugadorAI.getDisparos().stream()
                .anyMatch(disparo -> disparo.getCoordenada().equals(c));
        
        
    }
}
