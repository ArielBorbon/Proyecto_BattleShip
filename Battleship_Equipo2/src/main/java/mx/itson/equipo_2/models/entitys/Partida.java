/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;


import java.util.Arrays;
import mx.itson.equipo_2.models.enums.EstadoPartida;

/**
 *
 * @author Jose Eduardo Aguilar Garcia
 */
public class Partida {
  
    private Jugador jugadorEnTurno;
    private int contadorTurno;
    private Jugador[] jugadores;
    private EstadoPartida estado;

  
    public Partida() {
    }

 
    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugadores = new Jugador[]{jugador1, jugador2};
        this.jugadorEnTurno = jugador1;
        this.contadorTurno = 0;
        this.estado = EstadoPartida.CONFIGURACION;
    }

 
    public Jugador getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    public int getContadorTurno() {
        return contadorTurno;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

 
    public void setJugadorEnTurno(Jugador jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
    }

    public void setContadorTurno(int contadorTurno) {
        this.contadorTurno = contadorTurno;
    }

    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

 
    public void cambiarTurno() {
        contadorTurno++;
        jugadorEnTurno = jugadores[contadorTurno % jugadores.length];
    }


    @Override
    public String toString() {
        return "PartidaEntity{" +
                "jugadorEnTurno=" + jugadorEnTurno +
                ", contadorTurno=" + contadorTurno +
                ", jugadores=" + Arrays.toString(jugadores) +
                ", estado=" + estado +
                '}';
    }
}
