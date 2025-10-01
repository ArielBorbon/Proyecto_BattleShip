/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.controllers;

import java.util.Random;
import javax.swing.Timer;
import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.dto.JugadorDTO;
import mx.itson.equipo_2.mapper.CoordenadaMapper;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.TableroModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.TableroObserver;
import mx.itson.equipo_2.views.DispararView;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class PartidaController {

    private final PartidaModel partidaModel;
    private final Random random = new Random();

    public PartidaController(PartidaModel partidaModel) {
        this.partidaModel = partidaModel;
    }

    public void iniciarPartida() {
        partidaModel.iniciarTurno();
    }

    //iffy
    // NUEVO: Método completo para gestionar el turno de la IA
//    private void gestionarTurnoIA() {
//        System.out.println("Turno de la IA...");
//
//        //////////////////////////////////////////////////////////////////////////////////////////// TODO ESTO SE UTILIZA SOLO PARA SIMULAR QUE LA IA 
//        //////////////////////////////////////////////////////////////////////////////////////////// PIENSA Y REALIZA SU TURNO COMO UN JUGADOR REAL, LUEGO SE BORRARA
//        tiempoRestante = DURACION_TURNO - 1;
//
//        int segundosDeEspera = random.nextInt(5) + 3;
//        int momentoDelDisparo = DURACION_TURNO - segundosDeEspera;
//
//        dispararView.actualizarTimer(tiempoRestante, jugadorAI.getNombre());
//
//        turnoTimer = new Timer(1000, e -> {
//            tiempoRestante--;
//            dispararView.actualizarTimer(tiempoRestante, jugadorAI.getNombre());
//
//            if (tiempoRestante <= momentoDelDisparo) {
//                ((Timer) e.getSource()).stop(); // Detiene el temporizador.
//                ejecutarDisparoIA();          // La IA realiza su acción.
//            }
//        });
//        turnoTimer.start();
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//    }
//    private void ejecutarDisparoIA() {
//        if (partidaModel.partidaFinalizada()) {
//            return;
//        }
//
//        Coordenada coordDisparoIA;
//        do {
//            int fila = random.nextInt(10);
//            int col = random.nextInt(10);
//            coordDisparoIA = new Coordenada(fila, col);
//        } while (haSidoDisparadaPorIA(coordDisparoIA));
//
//        System.out.println("IA dispara en: (" + coordDisparoIA + ")");
//        ResultadoDisparo resultado = realizarDisparo(jugadorAI, coordDisparoIA);
//
//        if (partidaModel.partidaFinalizada()) {
//            return;
//        }
//
//        if (resultado == ResultadoDisparo.AGUA) {
//            System.out.println("IA falló el disparo. Turno para el jugador.");
//            pasarTurno();
//        } else {
//            System.out.println("IA acertó. Vuelve a 'pensar' para el siguiente disparo.");
//            gestionarTurnoIA();
//        }
//    }
    // NUEVO: Método auxiliar para la IA
//    private boolean haSidoDisparadaPorIA(Coordenada c) {
//        return jugadorAI.getDisparos().stream()
//                .anyMatch(disparo -> disparo.getCoordenada().equals(c));
//    }
    
    // Método público de conveniencia para main o pruebas sin UI
    public ResultadoDisparo realizarDisparo(Jugador jugador, CoordenadaDTO coord) throws IllegalStateException {

        ResultadoDisparo resultado = partidaModel.realizarDisparo(jugador, CoordenadaMapper.toEntity(coord));

        // 3. Comprobar condiciones de fin de partida.
        if (partidaModel.partidaFinalizada()) {
            System.out.println("¡Partida terminada! Ganador: " + jugador.getNombre());  
        }
        return resultado;
    }
}
