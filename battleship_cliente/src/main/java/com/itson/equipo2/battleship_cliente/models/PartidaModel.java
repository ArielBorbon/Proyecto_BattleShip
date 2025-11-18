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
import mx.itson.equipo_2.common.enums.EstadoJugador;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.TipoNave;

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

    public void iniciarPartida(PartidaIniciadaResponse response) {

        // 1. Sincronizar el ID de la partida
        this.setId(response.getPartidaId());

        // 2. Sincronizar al enemigo
        JugadorDTO yoDTO = response.getYo(getYo().getId());
        JugadorDTO enemigoDTO = response.getEnemigo(getYo().getId());

        // 3. Sincronizar el tablero propio
//        yo.setTablero(yoDTO.getTablero());
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

    public boolean intentarPosicionarNavePropia(TipoNave tipo, int fila, int col, boolean esHorizontal) {

        // 1. Delega la petición al modelo "trabajador"
        TableroModel tableroPropio = this.getTableroPropio();
        boolean exito = tableroPropio.agregarNave(tipo, fila, col, esHorizontal); //

        // 2. Si el trabajador tuvo éxito, notifica a los observadores
        if (exito) {
            this.notifyObservers();//
        }

        return exito;
    }

    public TableroModel getTableroPropio() {
        // Asumimos que 'this.yo' NUNCA es nulo aquí. 
        // 'this.yo' debe ser asignado cuando la partida se crea o el jugador se une.
        if (this.yo.getTablero() == null) {
            // Si el jugador 'yo' no tiene un tablero, se lo creamos.
            this.yo.setTablero(new TableroModel(this.yo.getId()));
        }
        return this.yo.getTablero();
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
