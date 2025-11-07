/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author skyro
 */
public class JugadorDTO {
    
    private String nombre;
    private Color color;
    private TableroDTO tablero;
    private List<DisparoDTO> disparos;

    public JugadorDTO() {
    }

    public JugadorDTO(String nombre, Color color, TableroDTO tablero, List<DisparoDTO> disparos) {
        this.nombre = nombre;
        this.color = color;
        this.tablero = tablero;
        this.disparos = disparos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public TableroDTO getTablero() {
        return tablero;
    }

    public void setTablero(TableroDTO tablero) {
        this.tablero = tablero;
    }

    public List<DisparoDTO> getDisparos() {
        return disparos;
    }

    public void setDisparos(List<DisparoDTO> disparos) {
        this.disparos = disparos;
    }

    @Override
    public String toString() {
        return "JugadorDTO{" + "nombre=" + nombre + ", color=" + color + ", tablero=" + tablero + ", disparos=" + disparos + '}';
    }
}
