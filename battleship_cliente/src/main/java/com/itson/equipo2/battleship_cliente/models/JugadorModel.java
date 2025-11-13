/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;

import java.util.List;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;

/**
 *
 * @author skyro
 */
public class JugadorModel {

    private String id;
    private String nombre;
    private boolean listo;       // si ya posicionó sus naves
    private TableroModel tablero; // su tablero 

    private List<CoordenadaDTO> disparos;

    public JugadorModel() {
    }

    public JugadorModel(String id, String nombre, boolean listo, TableroModel tablero, List<CoordenadaDTO> disparos) {
        this.id = id;
        this.nombre = nombre;
        this.listo = listo;
        this.tablero = tablero;
        this.disparos = disparos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isListo() {
        return listo;
    }

    public void setListo(boolean listo) {
        this.listo = listo;
    }

    public TableroModel getTablero() {
        return tablero;
    }

    public void setTablero(TableroModel tablero) {
        this.tablero = tablero;
    }

    public List<CoordenadaDTO> getDisparos() {
        return disparos;
    }

    public void setDisparos(List<CoordenadaDTO> disparos) {
        this.disparos = disparos;
    }

    @Override
    public String toString() {
        return "JugadorModel{" + "id=" + id + ", nombre=" + nombre + ", listo=" + listo + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }

}
