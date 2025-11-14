/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;
import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.response.PartidaIniciadaResponse;
import mx.itson.equipo_2.common.dto.response.ResultadoDisparoReponse;
import mx.itson.equipo_2.common.dto.response.TurnoTickResponse;
import mx.itson.equipo_2.common.enums.EstadoPartida;

/**
 *
 * @author skyro
 */
public class PartidaModel {

    private String id;
    private JugadorModel yo;
    private JugadorModel enemigo;
    private boolean enCurso;
    private String turnoDe; // id del jugador con turno actual
    private Integer segundosRestantes;
    private EstadoPartida estado;

    private final transient List<PartidaObserver> observers = new ArrayList<>();

    public void addObserver(PartidaObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PartidaObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        System.out.println("PartidaModel notificando a " + observers.size() + " observadores...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (PartidaObserver observer : new ArrayList<>(observers)) {
                observer.onChange(this);
            }
        });
    }

    public void procesarResultadoDisparo(ResultadoDisparoReponse response) {

        // 1. Determinar qué tablero actualizar (Lógica movida aquí)
        TableroModel tableroAfectado;

        // Usamos 'this.getYo().getId()' directamente
        if (response.getJugadorId().equals(this.getYo().getId())) {
            // Fui yo quien disparó -> Actualizo el tablero del enemigo (mis marcas)
            tableroAfectado = this.getEnemigo().getTablero();
        } else {
            // Fue el otro -> Actualizo mi tablero (sus impactos)
            tableroAfectado = this.getYo().getTablero();
        }

        // 2. Actualizar la celda (Delegando al sub-modelo)
        if (tableroAfectado != null) {
            tableroAfectado.actualizarCelda(
                    response.getCoordenada(),
                    response.getResultado(),
                    response.getCoordenadasBarcoHundido()
            );
        }

        // 3. Actualizar datos de la partida
        this.setTurnoDe(response.getTurnoActual()); // (Asegúrate que este setter NO notifique todavía)
        this.setEstado(response.getEstadoPartida());

        // 4. ¡NOTIFICACIÓN ATÓMICA!
        // Avisamos a la vista UNA sola vez después de que TODO cambió.
        // Así la vista no se repinta 3 veces seguidas.
        notifyObservers();
    }

    public void iniciarPartida(PartidaIniciadaResponse response, TableroModel tableroPropio, TableroModel tableroEnemigo) {

        // 1. Sincronizar el ID de la partida
        this.setId(response.getPartidaId());

        // 2. Sincronizar al enemigo
        JugadorDTO j1 = response.getJugador1();
        JugadorDTO j2 = response.getJugador2();
        JugadorModel yo = this.getYo();

        if (yo.getId().equals(j1.getId())) {
            this.setEnemigo(new JugadorModel(j2.getId(), j2.getNombre(), j2.getColor(), true, tableroEnemigo, null));
        } else {
            this.setEnemigo(new JugadorModel(j1.getId(), j1.getNombre(), j1.getColor(), true, tableroEnemigo, null));
        }

        // 3. Sincronizar el tablero propio
        yo.setTablero(tableroPropio);

        // 4. Sincronizar el estado de la partida
        this.setTurnoDe(response.getTurnoActual());
        this.setEstado(response.getEstado());
    }

    public void actualizarTick(TurnoTickResponse response) {

        // 1. Actualizamos los campos privados directamente
        //    (Evitamos los setters para no disparar 2 notificaciones)
        this.turnoDe = response.getJugadorEnTurnoId();
        this.segundosRestantes = response.getTiempoRestante();

        // 2. Notificamos a los observadores UNA SOLA VEZ
        //    con el estado ya actualizado.
        this.notifyObservers();
    }

    public PartidaModel() {
    }

    public PartidaModel(String id, JugadorModel yo, JugadorModel enemigo, boolean enCurso, String turnoDe, Integer segundosRestantes, EstadoPartida estado) {
        this.id = id;
        this.yo = yo;
        this.enemigo = enemigo;
        this.enCurso = enCurso;
        this.turnoDe = turnoDe;
        this.segundosRestantes = segundosRestantes;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JugadorModel getYo() {
        return yo;
    }

    public void setYo(JugadorModel yo) {
        this.yo = yo;
    }

    public JugadorModel getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(JugadorModel enemigo) {
        this.enemigo = enemigo;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
        
    }

    public String getTurnoDe() {
        return turnoDe;
    }

    public void setTurnoDe(String turnoDe) {
        this.turnoDe = turnoDe;
       
    }

    public Integer getSegundosRestantes() {
        return segundosRestantes;
    }

    public void setSegundosRestantes(Integer segundosRestantes) {
        this.segundosRestantes = segundosRestantes;
      
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
        
    }

    @Override
    public String toString() {
        return "PartidaModel{" + "id=" + id + ", yo=" + yo + ", enemigo=" + enemigo + ", enCurso=" + enCurso + ", turnoDe=" + turnoDe + ", segundosRestantes=" + segundosRestantes + ", estado=" + estado + '}';
    }

}
