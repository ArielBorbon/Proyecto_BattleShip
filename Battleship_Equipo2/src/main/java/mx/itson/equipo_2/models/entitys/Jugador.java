/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models.entitys;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author skyro
 * 
 */
public class Jugador {

    private String nombre;
    private Color color;
    private Tablero tablero;
    private List<Disparo> disparos;

    public Jugador() {
    }
    

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.color = Color.BLUE;
        this.tablero = new Tablero();
        this.disparos = new ArrayList<>();
    }

    public Jugador(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
        this.tablero = new Tablero();
        this.disparos = new ArrayList<>();
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public List<Disparo> getDisparos() {
        return disparos;
    }

    public void agregarDisparo(Disparo disparo) {
        disparos.add(disparo);
    }

    @Override
    public String toString() {
        return "JugadorEntity{" + "nombre=" + nombre + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }
    
    
}


