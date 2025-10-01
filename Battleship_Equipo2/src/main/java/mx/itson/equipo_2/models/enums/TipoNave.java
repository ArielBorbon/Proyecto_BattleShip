
package mx.itson.equipo_2.models.enums;

import mx.itson.equipo_2.models.entitys.*;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public enum TipoNave {
    PORTA_AVIONES(5),
    CRUCERO(4),
    SUBMARINO(3),
    BARCO(2);

    private final int tamanio;

    TipoNave(int tamanio) {
        this.tamanio = tamanio;
    }

    public int getTamanio() {
        return tamanio;
    }
}
