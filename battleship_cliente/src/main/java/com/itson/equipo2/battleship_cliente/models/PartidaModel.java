/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;
import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.dto.JugadorDTO;
import mx.itson.equipo_2.common.dto.PartidaDTO;
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
    private String idJugador1; //ID del Host

    private String nombreJugador1;
    private String nombreJugador2;

    private boolean soyHost = false;

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

    /**
     * Actualiza los datos de la sala basándose en el DTO global del servidor.
     *
     * @param dto
     */
    public void sincronizarDatosSala(PartidaDTO dto) {
        this.estado = dto.getEstado();

        if (dto.getJugador1() != null) {
            this.nombreJugador1 = dto.getJugador1().getNombre();
            this.idJugador1 = dto.getJugador1().getId(); 
        }

        if (dto.getJugador2() != null) {
            this.nombreJugador2 = dto.getJugador2().getNombre();
        } else {
            this.nombreJugador2 = "Esperando...";
        }

        if (this.yo != null && this.yo.getId() != null && this.idJugador1 != null) {

            if (this.yo.getId().equals(this.idJugador1)) {
                // Soy Host entonces  Mi enemigo es el J2 (si existe)
                if (dto.getJugador2() != null) {
                    actualizarEnemigo(dto.getJugador2());
                }
            } else if (dto.getJugador2() != null && this.yo.getId().equals(dto.getJugador2().getId())) {
                // Soy Guest entonces Mi enemigo es el J1
                actualizarEnemigo(dto.getJugador1());
            }
        }

        notifyObservers();
    }

    private void actualizarEnemigo(JugadorDTO enemigoDTO) {
        if (this.enemigo == null) {
            this.enemigo = new JugadorModel();
        }
        this.enemigo.setId(enemigoDTO.getId());
        this.enemigo.setNombre(enemigoDTO.getNombre());
        this.enemigo.setColor(enemigoDTO.getColor());
        this.enemigo.setEstado(EstadoJugador.POSICIONANDO);

        if (this.enemigo.getTablero() == null) {
            this.enemigo.setTablero(new TableroModel(enemigoDTO.getId()));
        }
    }

    public void procesarResultadoDisparo(ResultadoDisparoReponse response) {

        TableroModel tableroAfectado;

        if (response.getJugadorId().equals(this.getYo().getId())) {
            tableroAfectado = this.getEnemigo().getTablero();
        } else {
            tableroAfectado = this.getYo().getTablero();
        }

        if (tableroAfectado != null) {
            tableroAfectado.actualizarCelda(
                    response.getCoordenada(),
                    response.getResultado(),
                    response.getCoordenadasBarcoHundido()
            );
        }

        
        this.setTurnoDe(response.getTurnoActual()); 
        this.setEstado(response.getEstadoPartida());


        notifyObservers();
    }

    public void iniciarPartida(PartidaIniciadaResponse response) {

        this.setId(response.getPartidaId());

        JugadorDTO yoDTO = response.getYo(getYo().getId());
        JugadorDTO enemigoDTO = response.getEnemigo(getYo().getId());

        this.setTurnoDe(response.getTurnoActual());
        this.setEstado(response.getEstado());
    }

    public void actualizarTick(TurnoTickResponse response) {

        this.turnoDe = response.getJugadorEnTurnoId();
        this.segundosRestantes = response.getTiempoRestante();

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

    public boolean isSoyHost() {
        return yo != null && yo.getId() != null && idJugador1 != null && yo.getId().equals(idJugador1);
    }

    public void setSoyHost(boolean soyHost) {
        this.soyHost = soyHost;
    }

    public boolean intentarPosicionarNavePropia(TipoNave tipo, int fila, int col, boolean esHorizontal) {

        TableroModel tableroPropio = this.getTableroPropio();
        boolean exito = tableroPropio.agregarNave(tipo, fila, col, esHorizontal); //

        if (exito) {
            this.notifyObservers();
        }

        return exito;
    }

    public TableroModel getTableroPropio() {
        if (this.yo.getTablero() == null) {
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

    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public void setNombreJugador1(String nombreJugador1) {
        this.nombreJugador1 = nombreJugador1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public void setNombreJugador2(String nombreJugador2) {
        this.nombreJugador2 = nombreJugador2;
    }

}
