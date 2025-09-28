/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.itson.equipo_2.patterns.observer;

/**
 *
 * @author skyro
 */
public interface Subject<T> {
    void addObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers();
}
