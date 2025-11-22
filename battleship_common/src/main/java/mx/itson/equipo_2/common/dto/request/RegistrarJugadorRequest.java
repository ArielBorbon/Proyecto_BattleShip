/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.common.dto.request;

import java.io.Serializable;
import mx.itson.equipo_2.common.enums.AccionPartida;

/**
 *
 * @author Cricri
 */
public class RegistrarJugadorRequest implements Serializable {
    private String nombre;
    private AccionPartida accion; // CREAR o UNIRSE

    public RegistrarJugadorRequest() {
    }
    
        public RegistrarJugadorRequest(String nombre, AccionPartida accion) {
        this.nombre = nombre;
        this.accion = accion;
    }
    

    public RegistrarJugadorRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AccionPartida getAccion() {
        return accion;
    }

    public void setAccion(AccionPartida accion) {
        this.accion = accion;
    }
    
    
    
    
    
}
