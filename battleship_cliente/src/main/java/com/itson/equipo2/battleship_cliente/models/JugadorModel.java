/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.models;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.enums.ColorJugador;
import mx.itson.equipo_2.common.enums.EstadoJugador;

/**
 *
 * @author skyro
 */
public class JugadorModel {

    private String id;
    private String nombre;
    private ColorJugador color;
    private EstadoJugador estado;       // si ya posicionó sus naves
    private TableroModel tablero; // su tablero 

    private List<CoordenadaDTO> disparos;

    public JugadorModel() {
    }

    public JugadorModel(String nombre, ColorJugador color) {
        this.id= UUID.randomUUID().toString();
        this.nombre = nombre;
        this.color = color;
        this.disparos=new ArrayList();
    }
    
    

    public JugadorModel(String id, String nombre, ColorJugador color, EstadoJugador estado, TableroModel tablero, List<CoordenadaDTO> disparos) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.estado = estado;
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

    public ColorJugador getColor() {
        return color;
    }

    public void setColor(ColorJugador color) {
        this.color = color;
    }

    public EstadoJugador getEstado() {
        return estado;
    }

    public void setEstado(EstadoJugador estado) {
        this.estado = estado;
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
        return "JugadorModel{" + "id=" + id + ", nombre=" + nombre + ", color=" + color + ", estado=" + estado + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }

}
