
package com.itson.equipo2.battleship_cliente.pattern.mediator;

import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;

/**
 *
 * @author 
 * Ariel Eduardo Borbon Izaguirre   00000252116
* Sebastián Bórquez Huerta          00000252115
* Alberto Jiménez García            00000252595
* José Eduardo Aguilar García       00000252049
* José Luis Islas Molina            00000252574
 */
public interface ViewManager {
    void registrarPantalla(String nombre, ViewFactory factory);
    void cambiarPantalla(String nombre);
}
