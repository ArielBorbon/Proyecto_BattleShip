/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_servidor.domain.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import mx.itson.equipo_2.common.enums.ColorJugador;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author Cricri
 */
public class Jugador {

    private final String id;
    private String nombre;
    private ColorJugador color;
    private List<Disparo> disparos;

    public Jugador(String id, String nombre, ColorJugador color) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.disparos = new ArrayList<>();
    }

    public Jugador(String id, String nombre, ColorJugador color, List<Disparo> disparos) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.disparos = disparos;
    }

    public void addDisparo(int c, int f, ResultadoDisparo r) {
        this.disparos.add(new Disparo(new Coordenada(c, f), r));
        disparos.forEach((t) -> System.out.println(t));
    }

    public String getId() {
        return id;
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

    public List<Disparo> getDisparos() {
        return disparos;
    }

    public void setDisparos(List<Disparo> disparos) {
        this.disparos = disparos;
    }

    @Override
    public String toString() {
        return "Jugador{" + "id=" + id + ", nombre=" + nombre + ", disparos=" + disparos + '}';
    }

}
