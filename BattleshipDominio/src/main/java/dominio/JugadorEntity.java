/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author skyro
 * 
 */
public class JugadorEntity {

    private String nombre;
    private TableroEntity tablero;
    private List<DisparoEntity> disparos;

    public JugadorEntity() {
    }
    

    public JugadorEntity(String nombre) {
        this.nombre = nombre;
        this.tablero = new TableroEntity();
        this.disparos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public TableroEntity getTablero() {
        return tablero;
    }

    public List<DisparoEntity> getDisparos() {
        return disparos;
    }

    public void agregarDisparo(DisparoEntity disparo) {
        disparos.add(disparo);
    }

    @Override
    public String toString() {
        return "JugadorEntity{" + "nombre=" + nombre + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }
    
    
}


