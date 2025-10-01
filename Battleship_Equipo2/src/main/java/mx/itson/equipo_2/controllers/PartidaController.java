/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.controllers;


import mx.itson.equipo_2.dto.CoordenadaDTO;
import mx.itson.equipo_2.mapper.CoordenadaMapper;
import mx.itson.equipo_2.models.JugadorModel;
import java.util.Random;
import javax.swing.Timer;
import mx.itson.equipo_2.models.PartidaModel;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Tablero;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.TableroObserver;
import mx.itson.equipo_2.views.DispararView;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class PartidaController implements TableroObserver {

    private final PartidaModel partidaModel;
    private final DispararView dispararView;
    private final Jugador jugadorHumano;
    private final Jugador jugadorAI;
    private final Random random = new Random();

    private Timer turnoTimer;
    private int tiempoRestante;
    private final int DURACION_TURNO = 31;

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

//<<<<<<< HEAD
//    public PartidaController() {
//        this.partidaModel = new PartidaModel(j1, j2); 
//    }

  //  public ResultadoDisparo realizarDisparo(Jugador atacante, CoordenadaDTO c) {
   //     try {
    //        ResultadoDisparo resultado = partidaModel.realizarDisparo(atacante, CoordenadaMapper.toEntity(c));
//=======
    public void iniciarPartida() {
        System.out.println("¡La partida ha comenzado!");
        iniciarTurno(this.jugadorHumano);
    }

    // Método privado que recibe coordenadas desde la vista
    private void handleDisparoHumano(Coordenada coord) {
        // Evitar que el jugador dispare si no es su turno
        if (partidaModel.getJugadorEnTurno() != jugadorHumano) {
            System.out.println("Espera, no es tu turno.");
            return;
        }

        if (turnoTimer != null) {
            turnoTimer.stop();
        }

        // Realizar el disparo del humano
        ResultadoDisparo resultado = realizarDisparo(jugadorHumano, coord);

        // NUEVO: Después del disparo del humano, comprobar si es el turno de la IA
        if (resultado == ResultadoDisparo.AGUA) {
            System.out.println("¡Agua! Turno para la IA.");
            pasarTurno();
        } else {
            System.out.println("¡Impacto! Tienes otro turno.");
            iniciarTurno(jugadorHumano);
        }
    }

    //iffy
    // NUEVO: Método completo para gestionar el turno de la IA
    private void gestionarTurnoIA() {
        System.out.println("Turno de la IA...");

        //////////////////////////////////////////////////////////////////////////////////////////// TODO ESTO SE UTILIZA SOLO PARA SIMULAR QUE LA IA 
        //////////////////////////////////////////////////////////////////////////////////////////// PIENSA Y REALIZA SU TURNO COMO UN JUGADOR REAL, LUEGO SE BORRARA
        tiempoRestante = DURACION_TURNO - 1;

        int segundosDeEspera = random.nextInt(5) + 3;
        int momentoDelDisparo = DURACION_TURNO - segundosDeEspera;

        dispararView.actualizarTimer(tiempoRestante, jugadorAI.getNombre());

        turnoTimer = new Timer(1000, e -> {
            tiempoRestante--;
            dispararView.actualizarTimer(tiempoRestante, jugadorAI.getNombre());

            if (tiempoRestante <= momentoDelDisparo) {
                ((Timer) e.getSource()).stop(); // Detiene el temporizador.
                ejecutarDisparoIA();          // La IA realiza su acción.
            }
        });
        turnoTimer.start();

        ///////////////////////////////////////////////////////////////////////////////////////////
    }

    private void ejecutarDisparoIA() {
        if (partidaModel.partidaFinalizada()) {
            return;
        }

        Coordenada coordDisparoIA;
        do {
            int fila = random.nextInt(10);
            int col = random.nextInt(10);
            coordDisparoIA = new Coordenada(fila, col);
        } while (haSidoDisparadaPorIA(coordDisparoIA));

        System.out.println("IA dispara en: (" + coordDisparoIA + ")");
        ResultadoDisparo resultado = realizarDisparo(jugadorAI, coordDisparoIA);

        if (partidaModel.partidaFinalizada()) {
            return;
        }

        if (resultado == ResultadoDisparo.AGUA) {
            System.out.println("IA falló el disparo. Turno para el jugador.");
            pasarTurno();
        } else {
            System.out.println("IA acertó. Vuelve a 'pensar' para el siguiente disparo.");
            gestionarTurnoIA();
        }
    }

    private void iniciarTurno(Jugador jugador) {
        // Reiniciar tiempo
        tiempoRestante = DURACION_TURNO;

        // Cancelar cualquier timer previo
        if (turnoTimer != null && turnoTimer.isRunning()) {
            turnoTimer.stop();
        }

        // Crear un nuevo Timer que dispare cada 1 segundo
        turnoTimer = new Timer(1000, e -> {
            tiempoRestante--;

            if (dispararView != null) {
                dispararView.actualizarTimer(tiempoRestante, jugador.getNombre());
            }

            if (tiempoRestante <= 0) {
                turnoTimer.stop();
                System.out.println("Tiempo agotado para " + jugador.getNombre());

                // Forzar cambio de turno
                pasarTurno();
            }
        });

        turnoTimer.start();
    }

    private void pasarTurno() {
        if (partidaModel.partidaFinalizada()) {
            return;
        }

        // Detener cualquier timer que aún esté corriendo.
        if (turnoTimer != null) {
            turnoTimer.stop();
        }

        Jugador actual = partidaModel.getJugadorEnTurno();
        Jugador siguiente = (actual == jugadorHumano) ? jugadorAI : jugadorHumano;

        partidaModel.setJugadorEnTurno(siguiente);
        System.out.println("Cambiando turno. Ahora juega: " + siguiente.getNombre());

        if (siguiente == jugadorAI) {
            gestionarTurnoIA();
        } else {
            iniciarTurno(siguiente);
        }
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
