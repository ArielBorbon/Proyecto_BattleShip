
package mx.itson.equipo_2.models;

import java.util.List;
import mx.itson.equipo_2.models.entitys.Disparo;
import mx.itson.equipo_2.models.entitys.Jugador;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public class JugadorModel {
    
    private Jugador jugador;

    public JugadorModel(Jugador jugador) {
        this.jugador = jugador;
    }
    
    public boolean registrarDisparo(Disparo d) {
        jugador.agregarDisparo(d);
        return true;
    }
    
    public List<Disparo> obtenerDisparos() {
        return jugador.getDisparos();
    }
 
    public Jugador getJugador() {
        return jugador;
    }    
    
}
