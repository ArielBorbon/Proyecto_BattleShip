
package com.itson.equipo2.battleship_cliente.pattern.observer;

import com.itson.equipo2.battleship_cliente.models.TableroModel;
import mx.itson.equipo_2.common.dto.DisparoDTO;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */

public interface TableroObserver {

    void onDisparo(TableroModel model, DisparoDTO disparo);
}
