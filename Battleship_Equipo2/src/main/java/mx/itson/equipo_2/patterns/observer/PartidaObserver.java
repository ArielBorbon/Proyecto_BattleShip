
package mx.itson.equipo_2.patterns.observer;

import mx.itson.equipo_2.models.PartidaModel;

/**
 *
 * @author skyro
 */
public interface PartidaObserver {
    
    void onChange(PartidaModel model);
    
}
