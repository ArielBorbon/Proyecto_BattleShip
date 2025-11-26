/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.pattern.observer;

/**
 *
 * @author skyro
 */
public interface PartidaSubject {
    
    void addObserver(PartidaObserver observer);
    void removeObserver(PartidaObserver observer);
    void notifyObservers();
}
