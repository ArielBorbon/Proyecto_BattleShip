/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.models;

import java.util.List;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author skyro
 */
public class JugadorModel {
    
    private Jugador jugador;
    
    public boolean registrarDisparo(Disparo d) {
        jugador.agregarDisparo(d);
        return true;
    }
    
    public List<Disparo> obtenerDisparos() {
        return jugador.getDisparos();
    }
}
