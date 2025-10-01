/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import mx.itson.equipo_2.models.entitys.Coordenada;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;
import mx.itson.equipo_2.models.entitys.Partida;
import mx.itson.equipo_2.models.enums.ResultadoDisparo;
import mx.itson.equipo_2.patterns.observer.PartidaObserver;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class PartidaModel {

    private final Partida partida;
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final TableroModel tableroModel1;
    private final TableroModel tableroModel2;

    private Timer turnoTimer;
    private int tiempoRestante;
    private final int DURACION_TURNO = 31;

    private List<PartidaObserver> observers;

    public PartidaModel(Jugador j1, Jugador j2) {
        this.partida = new Partida(j1, j2);

        // --- MODIFICADO: Inicialización directa ---
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.tableroModel1 = new TableroModel(j1.getTablero());
        this.tableroModel2 = new TableroModel(j2.getTablero());
        
        this.observers = new ArrayList<>();
    }

    public void addObserver(PartidaObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(PartidaObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (PartidaObserver to : observers) {
            to.onChange(this);
        }
    }

    public boolean verificarTurno(Jugador j) {
        return partida.getJugadorEnTurno().equals(j);
    }

    public void iniciarTurno() {
        // Reiniciar tiempo
        tiempoRestante = DURACION_TURNO;

        // Cancelar cualquier timer previo
        if (turnoTimer != null && turnoTimer.isRunning()) {
            turnoTimer.stop();
        }

        // Crear un nuevo Timer que dispare cada 1 segundo
        turnoTimer = new Timer(1000, e -> {
            tiempoRestante--;

            if (tiempoRestante <= 0) {
                turnoTimer.stop();
                System.out.println("Tiempo de turno agotado!");

                // Forzar cambio de turno
                pasarTurno();
                notifyObservers();
            }

            notifyObservers();
        });

        turnoTimer.start();
    }

    public void pasarTurno() {

        if (partidaFinalizada()) {
            return; // No cambiar turno si la partida terminó
        }

        // Cambiar turno dentro de la entidad Partida
        partida.cambiarTurno();

        // Notificar a los observadores que el turno ha cambiado
        notifyObservers();
    }

    public void cambiarTurno() {
        if (partidaFinalizada()) {
            return;
        }

        // Detener cualquier timer que aún esté corriendo.
        if (turnoTimer != null) {
            turnoTimer.stop();
        }

        partida.cambiarTurno();
    }

    // --- MODIFICADO: Devuelve la entidad Jugador ---
    public Jugador obtenerOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? jugador2 : jugador1;
    }

    // --- NUEVO: Método para obtener el TableroModel del oponente ---
    public TableroModel obtenerTableroOponente(Jugador atacante) {
        return atacante.equals(jugador1) ? tableroModel2 : tableroModel1;
    }

    public ResultadoDisparo realizarDisparo(Jugador atacante, Coordenada coord) throws IllegalStateException {
        if (!verificarTurno(atacante)) {
            throw new IllegalStateException("No es tu turno");
        }

        TableroModel oponenteTablero = obtenerTableroOponente(atacante);

        if (!oponenteTablero.validarCoordenada(coord)) {
            throw new IllegalArgumentException("Coordenada inválida: " + coord);
        }

        ResultadoDisparo resultado = oponenteTablero.recibirDisparo(coord);
        Disparo disparo = new Disparo(resultado, coord);
        atacante.agregarDisparo(disparo);

        return resultado;
    }

    public void repetirTurno() {
        if (partidaFinalizada()) {
            return; // No hacer nada si la partida terminó
        }

        // Detener cualquier timer activo
        if (turnoTimer != null && turnoTimer.isRunning()) {
            turnoTimer.stop();
        }

        // Reiniciar el tiempo del turno
        tiempoRestante = DURACION_TURNO;

        // Notificar a los observadores que el turno se repite
        notifyObservers();

        // Crear un nuevo timer para el turno
        turnoTimer = new Timer(1000, e -> {
            tiempoRestante--;

            if (tiempoRestante <= 0) {
                turnoTimer.stop();
                System.out.println("Tiempo de turno agotado!");

                // Cambiar turno automáticamente
                pasarTurno();
            }

            notifyObservers();
        });

        turnoTimer.start();
    }

    public boolean partidaFinalizada() {

        return tableroModel1.todasNavesHundidas() || tableroModel2.todasNavesHundidas();

    }

    public Jugador getJugadorEnTurno() {
        return partida.getJugadorEnTurno();
    }

    public void setJugadorEnTurno(Jugador jugador) {
        partida.setJugadorEnTurno(jugador);
    }

    public Partida getPartida() {
        return partida;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public TableroModel getTableroModel1() {
        return tableroModel1;
    }

    public TableroModel getTableroModel2() {
        return tableroModel2;
    }

    public Timer getTurnoTimer() {
        return turnoTimer;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

}
