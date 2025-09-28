/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;

import java.util.Arrays;
import mx.itson.equipo_2.models.enums.EstadoPartida;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class Partida {

    private Jugador jugadorEnTurno;
    private Jugador jugador1;
    private Jugador jugador2;
    private EstadoPartida estado;

    public Partida() {
    }
    
    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;       
        this.jugador2 = jugador2;        
        this.jugadorEnTurno = jugador1;  
        this.estado = EstadoPartida.CONFIGURACION;
    }



    public Jugador getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setJugadorEnTurno(Jugador jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public void cambiarTurno() {
        if (jugadorEnTurno.equals(jugador1)) {
            jugadorEnTurno = jugador2;
        } else {
            jugadorEnTurno = jugador1;   
        }
    }

    @Override
    public String toString() {
        return "Partida{" + "jugadorEnTurno=" + jugadorEnTurno + ", jugador1=" + jugador1 + ", jugador2=" + jugador2 + ", estado=" + estado + '}';
    }

}
