package com.itson.equipo2.battleship_cliente.pattern.observer;

/**
 *
 * @author skyro
 * @param <T>
 */
public interface IObserver<T> {

    void onChange(T observable);

}
