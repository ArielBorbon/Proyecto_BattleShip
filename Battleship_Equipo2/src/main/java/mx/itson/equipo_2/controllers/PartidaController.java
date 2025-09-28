/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.controllers;

import java.util.Random;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.TableroModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.TableroObserver;
import mx.itson.equipo_2.views.DispararView;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class PartidaController implements TableroObserver {

    private final PartidaModel partidaModel;
    private final DispararView dispararView; 
    private final Jugador jugadorHumano;
    private final Jugador jugadorAI;
    private final Random random = new Random();

    public PartidaController(PartidaModel partidaModel, DispararView dispararView, Jugador jugadorHumano, Jugador jugadorAI) {
        this.partidaModel = partidaModel;
        this.dispararView = dispararView;
        this.jugadorHumano = jugadorHumano;
        this.jugadorAI = jugadorAI;

        if (dispararView != null) {
            // Conectar la vista con el manejador de disparos del humano
            dispararView.setListenerDisparo(this::handleDisparoHumano);

            // NUEVO: Mostrar el tablero inicial del jugador humano
            dispararView.mostrarTableroPropio(jugadorHumano.getTablero());
        }

        partidaModel.getTableroModel1().addObserver(this);
        partidaModel.getTableroModel2().addObserver(this);
    }

    // Método privado que recibe coordenadas desde la vista
    private void handleDisparoHumano(Coordenada coord) {
        // Evitar que el jugador dispare si no es su turno
        if (partidaModel.getJugadorEnTurno() != jugadorHumano) {
            System.out.println("Espera, no es tu turno.");
            return;
        }

        // Realizar el disparo del humano
        realizarDisparo(jugadorHumano, coord);

        // NUEVO: Después del disparo del humano, comprobar si es el turno de la IA
        if (partidaModel.getJugadorEnTurno() == jugadorAI && !partidaModel.partidaFinalizada()) {
            // Pausar ligeramente para dar una sensación de que la IA "piensa"
            // (Opcional, pero mejora la experiencia)
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Espera 1 segundo
                } catch (InterruptedException e) {
                }
                gestionarTurnoIA();
            }).start();
        }
    }

    // NUEVO: Método completo para gestionar el turno de la IA
    private void gestionarTurnoIA() {
        System.out.println("Turno de la IA...");
        // La IA seguirá disparando mientras sea su turno (si acierta, repite)
        while (partidaModel.getJugadorEnTurno() == jugadorAI && !partidaModel.partidaFinalizada()) {
            Coordenada coordDisparoIA;

            // Lógica para encontrar una coordenada no repetida
            do {
                int fila = random.nextInt(Tablero.TAMANIO);
                int col = random.nextInt(Tablero.TAMANIO);
                coordDisparoIA = new Coordenada(fila, col);
            } while (haSidoDisparadaPorIA(coordDisparoIA));

            System.out.println("IA dispara en: (" + coordDisparoIA.getFila() + "," + coordDisparoIA.getColumna() + ")");

            // La IA realiza el disparo
            realizarDisparo(jugadorAI, coordDisparoIA);

            // Pausa breve entre disparos si la IA tiene turnos consecutivos
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Fin del turno de la IA. Turno de: " + partidaModel.getJugadorEnTurno().getNombre());
    }

    // NUEVO: Método auxiliar para la IA
    private boolean haSidoDisparadaPorIA(Coordenada c) {
        return jugadorAI.getDisparos().stream()
                .anyMatch(disparo -> disparo.getCoordenada().equals(c));
    }

    // Método público de conveniencia para main o pruebas sin UI
    public ResultadoDisparo realizarDisparo(Jugador jugador, Coordenada coord) {
        try {

            ResultadoDisparo resultado = partidaModel.realizarDisparo(jugador, coord);

            // 3. Comprobar condiciones de fin de partida.
            if (partidaModel.partidaFinalizada()) {
                System.out.println("¡Partida terminada! Ganador: " + jugador.getNombre());
                if (dispararView != null) {
                    // Aquí podrías llamar a un método en la vista para mostrar un diálogo de victoria.
                    // Ej: dispararView.mostrarDialogoGanador(jugador.getNombre());
                }
            }

            return resultado;

        } catch (Exception e) {
            // 4. Manejar errores (como disparar a una celda repetida).
            System.err.println("Error al realizar disparo: " + e.getMessage());
            if (dispararView != null) {
                // Notificar al usuario del error a través de la vista.
                // Ej: dispararView.mostrarError(e.getMessage());
            }
            return null;
        }
    }

// EN PartidaController.java
    @Override
    public void onCeldaDisparada(TableroModel tableroAfectado, int fila, int columna, ResultadoDisparo resultado) {
        if (dispararView == null) {
            return;
        }

        // --- LÓGICA CORREGIDA ---
        // Obtenemos una referencia fija al tablero de la IA
        TableroModel tableroAI = partidaModel.getTableroModel2(); // Asumiendo que jugador2 es la IA

        // Comparamos el tablero afectado con el de la IA
        if (tableroAfectado == tableroAI) {
            // Si el tablero modificado es el de la IA, significa que el HUMANO disparó.
            // Actualizamos la vista del enemigo.
            dispararView.actualizarCeldaEnemigo(fila, columna, resultado);
        } else {
            // Si no es el tablero de la IA, entonces es el del humano. La IA disparó.
            // Actualizamos la vista del tablero propio.
            dispararView.actualizarCeldaPropia(fila, columna, resultado);
        }
    }
}
