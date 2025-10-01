
package mx.itson.equipo_2.models.entitys;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
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


