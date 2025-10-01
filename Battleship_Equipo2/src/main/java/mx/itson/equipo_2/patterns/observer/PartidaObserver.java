/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.patterns.observer;

import mx.itson.equipo_2.models.PartidaModel;

/**
 *
 * @author skyro
 */
public interface PartidaObserver {
    
    void onChange(PartidaModel model);
    
}
