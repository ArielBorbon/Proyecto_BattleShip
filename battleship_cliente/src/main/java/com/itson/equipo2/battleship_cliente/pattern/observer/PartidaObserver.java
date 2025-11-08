
package com.itson.equipo2.battleship_cliente.pattern.observer;

import com.itson.equipo2.battleship_cliente.models.PartidaModel;

/**
 *
 * @author skyro
 */
public interface PartidaObserver {
    
    void onChange(PartidaModel model);
    
}
